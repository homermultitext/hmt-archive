# Overview of the current release: 2024c

Counts below were taken directly from `releases-cex/hmt-current.cex` on 2026-11-25. That file is identical to `releases-cex/hmt-2024c.cex`.

## The file

- Release **2024c** ("Corrects errors in indexing of scholia to Iliad passages"), built with `HmtArchive` 0.14.2.
- A single CEX file (CEX version 3.0.2): about 24.4 MB, 192,930 lines.
- Library URN `urn:cite2:hmt:publications.cex.2024c:all`; license CC Attribution, Non-Commercial 4.0.
- Block structure: 1 library declaration, 1 text catalog, 2 `ctsdata` blocks (diplomatic and normalized editions), and CITE blocks for data models, collections and relation sets (28 `datamodels` blocks, 18 `citecollections` blocks, 13 `citerelationset` blocks).

*Blocks used:* `#!cexversion` and `#!citelibrary` for the version, name, URN and license. The release ID and module version come from the `//` comment lines at the very top, which are not a block. The block counts come from tallying every `#!` header in the file.

## Texts

The catalog lists 16 versions; 12 are online in this release, each in a diplomatic and a normalized edition (40,485 passages per edition). Four *Iliad* versions are catalogued but not online: Escorial Ω 1.12, Marciana 841, Geneva 44 and Laurentian 32.3.

| Text | Passages per edition | DSE records |
|---|---|---|
| *Iliad*, Venetus A | 15,645 | 15,618 |
| *Iliad*, Venetus B (book 9 only) | 710 | **0** |
| *Iliad*, Escorial Υ 1.1 (books 8-10) | 1,694 | 1,693 |
| Selected poems, Venetus A | 31 | 31 |

### Scholia

DSE records index scholia at the level of the whole *scholion*, but the editions publish each scholion in parts: a `lemma` passage and a `comment` passage (22,405 passages for 11,211 scholia). A DSE record therefore covers one or two published passages, and the table counts scholia, not passages.

| Scholia of | Scholia | Published passages | Scholia with a DSE record | Scholia without | DSE records for no scholion in the text |
|---|---|---|---|---|---|
| Venetus A, main | 4,732 | 9,461 | 3,962 | 770 | 32 |
| Venetus A, interlinear | 2,343 | 4,674 | 1,864 | 479 | 10 |
| Venetus A, intermarginal | 1,724 | 3,446 | 1,480 | 244 | 15 |
| Venetus A, interior | 1,107 | 2,214 | 978 | 129 | 7 |
| Venetus A, exterior | 351 | 702 | 305 | 46 | 12 |
| Venetus A, outside five zones | 3 | 6 | 2 | 1 | 0 |
| Venetus B | 324 | 648 | 0 | 324 | 0 |
| Escorial Υ 1.1 | 627 | 1,254 | 627 | 0 | 27 |
| **Total** | **11,211** | **22,405** | **9,218** | **1,993** | **103** |

- Every scholion has a `comment` passage. A `lemma` passage exists for all but 16 scholia, but its text is empty for 5,702 of the 11,195: only 5,493 scholia have lemma text. Lemma text is largely confined to the main scholia of the Venetus A (4,545), the Venetus B (324) and the Escorial (599); the other Venetus A zones have almost none.
- Two further DSE records use obsolete edition IDs (`msAmeta.hmt`, `msAimLater.hmt`) and match no catalogued version.

*Blocks used:* `#!ctscatalog` for the 16 versions and their `online` flag; the two `#!ctsdata` blocks (diplomatic and normalized editions) for passage counts, collapsing each scholion's `lemma` and `comment` passages to one scholion; the `#!citerelationset` with URN `urn:cite2:hmt:hmtdse.v1:all` for DSE records per text.

## Indexing

- **DSE records:** 26,666 in total, linking text passages to image regions and manuscript pages. Two stray records use obsolete edition IDs (`msAmeta.hmt`, `msAimLater.hmt`).
- **Scholia to *Iliad*:** 11,204 pairs.
- **Name indexes:** 15,594 personal-name, 2,903 place-name and 3,034 ethnic-group references.
- ***Iliad* lines to pages:** Venetus B 15,687; Escorial Ω 1.12 15,633; Marciana 841 6,795; Burney 86 632.
- **Paragraphing of *Iliad* lines:** Escorial Ω 1.12 (612), Burney 86 (47).
- **Venetus A page regions of interest:** 631 entries; Comparetti facsimile images to Venetus A pages: 652 entries.

*Blocks used:* `#!citerelationset` blocks, one per index: `hmtdse.v1:all`, `commentary.v1:all`, `persnameindex.v1:all`, `placenameindex.v1:all`, `ethnicgroupindex.v1:all`, `iliadindex.v1` (`msB`, `e4`, `u4`, `burney86`), `paragraphing.v1` (`e4`, `burney86`), `pagerois.v1:msA` and `comparetti.v1:msA`. Counts are the data rows of each.

## Manuscripts and images

Seven codices are modelled as sequences of pages: Venetus A (660), Venetus B (680), Escorial Υ 1.1 (660), Escorial Ω 1.12 (437), Marciana 841 (388), Burney 86 (562) and Laurentian 32.3 (862).

Image catalogs (approximate row counts): Venetus A about 970; Comparetti facsimile about 680; Venetus B bifolios 205; Escorial Υ 1.1 bifolios about 520; Escorial Ω 1.12 about 870; Marciana 841 about 780; Burney 86 about 1,120; Geneva 44 about 800; Laurentian 32.3 about 1,720.

*Blocks used:* `#!citecollections` for the collection names and descriptions, and `#!citedata` blocks for the rows: page rows of each codex (`msA.v1`, `msB.v1`, `e3pages.v1`, `e4pages.v1`, `u4pages.v1`, `burney86pages.v1`, `laur32pages.v1`) and image rows of each image collection (`vaimg`, `compimg`, `vbbifolio`, `e3bifolio`, `e4img`, `u4img`, `burney86imgs`, `gen44imgs`, `laur32imgs`).

## Authority lists

1,300 persons, 482 places, 13 astronomical entities, 8 cited works.

*Blocks used:* `#!citedata` blocks for the collections `pers.v1`, `place.v1`, `astro.v1` and `citedworks.v1` (rows counted), declared in `#!citecollections`.

## Data models in use

DSE, codex, citable image, commentary, named-entity index, text-on-page, paragraphing and page-region models. Declarations are listed in the release's `datamodels` blocks.

*Blocks used:* the 28 `#!datamodels` blocks, which pair each collection or relation set with its model, and the `datamodels.v1` collection in `#!citedata`.

## Gaps visible in the data

- Venetus B (book 9) has text for both *Iliad* and scholia but no DSE records.
- 1,993 of 11,211 scholia have no DSE record: all 324 in the Venetus B and 1,669 of 10,260 (16%) in the Venetus A. Conversely, 105 DSE records for scholia point to scholia that are not in the editions (103 in catalogued versions, 2 with obsolete edition IDs). `scratch/dse-check.csv` has the detailed report (2,937 rows, covering the *Iliad* as well).

*Blocks used:* the `#!ctsdata` blocks compared with the `urn:cite2:hmt:hmtdse.v1:all` `#!citerelationset`, plus `scratch/dse-check.csv`, which is not part of the release.
