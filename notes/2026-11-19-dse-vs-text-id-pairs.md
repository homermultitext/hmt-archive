# DSE records with no text vs. text with no DSE: candidate ID pairs

Date: 2026-11-19. Source report: `scratch/dse-check.csv`. Read-only analysis; no data files were changed.

Method: for each "DSE record for passage not in edition" row (120), looked for "text passage missing from DSE" rows in the same edition and book with a similar ID, then checked against `archive/tei-editions/scholia/VenetusA-Scholia-NN.xml` and `archive/dse-data/*.cex`. The main test: does the page of the DSE record equal the page of the *Iliad* line the TEI scholion refers to (line-to-page taken from the Venetus A *Iliad* DSE records)? Image ROIs were not examined.

Summary: about 20 of the 120 rows pair up. About 100 have no partner (see end).

## 1. Strong (page-verified)

### 1a. Venetus A main scholia, book 16, 209v: underscore inconsistency (7 pairs)
- TEI `msA` book 16 uses `209v1`, then `209v_2` ... `209v_8`.
- DSE (`dse-va-scholia16.cex`) uses `209v1` ... `209v8`, matching the `209v1` style (also used by `msAint` 209v1/209v2).
- All 7 pairs: TEI scholion refers to an *Iliad* line on 209v (16.179-16.188) and the DSE record is on 209v.
- Suggested fix: rename TEI IDs `209v_2`..`209v_8` to `209v2`..`209v8` (the DSE side already has ROIs and is consistent with `209v1`).
- Clears 14 report rows (7 stray DSE + 7 unindexed text).

### 1b. Intermarginal (`msAim`) book 13, A-series: DSE numbered 3 too high (10 records)
- TEI `msAim` book 13 has A1..A26 (plus A5a-c, A16bis). DSE has A1..A16bis, then A20..A29. DSE has no A17-A19; TEI has no A27-A29.
- Shifting DSE numbers down by 3 (DSE A20-A29 -> TEI A17-A26) matches the page of the referred *Iliad* line **in all 10 cases** (178r, 178v, 179r, 179r, 179r, 179r, 179v, 179v, 180r, 180r).
- Under the current IDs, DSE A20-A26 match IDs that *exist* in TEI, but 6 of 7 land on a different page than the TEI scholion's line (A20, A21, A23, A24, A25, A26; only A22 matches, by coincidence). So these are silently misindexed, and the report cannot see this.
- Suggested fix: renumber DSE records A20..A29 to A17..A26 in a single mapping (not one at a time; IDs collide). Alternative: renumber TEI instead. DSE side carries the ROIs, so editing DSE IDs is the smaller change.
- Clears 6 report rows (3 unindexed A17-A19 + 3 stray A27-A29) and corrects 7 wrong alignments.

## 2. Likely (one candidate, page consistent; confirm against images)
| DSE record (no text) | Text passage (no DSE) | Page evidence |
|---|---|---|
| `msAim` 10.3556 (133r) | 10.3557 (refs 10.376, on 133r) | Neighbours 3554/3555 also on 133r |
| `msAext` 23.2112 (302v) | 23.2116 (refs 23.485, on 302v) | Only text ID nearby |
| `msA` 24.93 (323r) | 24.97 (refs 24.611, on 323r) | Other candidates 96b/97b/98b/99b not checked |
| `msA` 13.164r6 (164r) | 13.164r6bis (refs 13.6, on 164r) | TEI `msA` has `164r6bis` but no `164r6` |
| `msAint` 16.210v11 (210v) | `msAint` 16.210v1 (refs 16.239, on 210v) | `msA` 210v11 is already correctly indexed; `msAint` TEI also has 210v2, 210v3 (refs not read) so v2/v3 are alternatives |

## 3. Ambiguous
- `msAil` 10.1543 (129r): candidates 10.1540 (refs 10.153) and 10.1542 (refs 10.160) are both on 129r; cannot tell which.

## 4. Withdrawn / rejected after checking
- `msAim` 24.D1-D4 vs text X1-X4, and `msAext` 24.A1 vs X1: DSE pages (312v, 310v) differ from the TEI line pages (313v/314r). Not supported.
- `msAext` 7.1067 vs 7.1047: pages differ (95v vs 94v). `msAil` 6.A17 vs A27: pages differ (85v vs 90v).
- Iliad 16.381/451/689/690 vs 16.28x-29x: pages differ.

## 5. Unresolved
- `msA` 14.D19, D20 (DSE, 189r). TEI `msA` book 14 has IDs D, D1..D17; the unnumbered `D` (unindexed) may be a truncated ID. DSE has D1..D17, D19, D20 and no D18. Needs a look at the TEI.
- Iliad 16.614, 16.615 (DSE, 218r) vs unindexed 16.627, 16.628 (218r): line numbering?

## 6. No partner found (roughly 100 rows)
- Escorial (`e3`) book 10 scholia: 125v_1-10, 126r_1-11, e3_132v_4-9. No text missing in that book: the scholia text is not in the edition.
- Venetus A *Iliad* `title` lines (4, 6, 7, 10, 21): DSE records exist, no title passage in text.
- Book 14 D6-D9 on 189r (four zones); old edition IDs `msAmeta.hmt` (2.133), `msAimLater.hmt` (2.281); misc single records (19.2015office4_4-6, 21.277v_6, etc.).

## Idea for a new check (not in the report)
The A-series case shows that DSE records can sit on the wrong text while both IDs exist. A check that compares each scholion's DSE page with the page of the *Iliad* line it comments on would find these. Not implemented; would need a decision on where it should live (HmtArchive.jl).

## Follow-up: page-vs-line check implemented in HmtArchive.jl (not yet run)
Written in `../HmtArchive.jl` (uncommitted, not executed: no Julia available in the session):
- `src/validation/scholia_pagecheck.jl`: `scholiapagecheck(...)`, exported from `HmtArchive.Analysis`.
- `test/validation/test_scholiapagecheck.jl`, included from `test/runtests.jl`. Expected values (76 flagged: 51 at distance 1, 7 at distance 2, 18 beyond) were computed with a Python port of the same algorithm on `test/data/hmt-2024c.cex`. If the Julia run disagrees, check the Julia first, then the port.
- `scripts/scholiapagecheck.jl`: writes `scratch/scholia-page-check.csv`.
- Prototype on the current archive (TEI refs vs DSE pages): about 6,850 scholia on the same page as their Iliad line; 51 one page away, 14 two pages away, 42 further. The renumbered 13.A20-A26 records should appear in the distance 1-2 groups.
