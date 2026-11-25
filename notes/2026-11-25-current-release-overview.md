# Overview of the current release: 2024c

Counts below were taken directly from `releases-cex/hmt-current.cex` on 2026-11-25. That file is identical to `releases-cex/hmt-2024c.cex`.

## The file

- Release **2024c** ("Corrects errors in indexing of scholia to Iliad passages"), built with `HmtArchive` 0.14.2.
- A single CEX file (CEX version 3.0.2): about 24.4 MB, 192,930 lines.
- Library URN `urn:cite2:hmt:publications.cex.2024c:all`; license CC Attribution, Non-Commercial 4.0.
- Block structure: 1 library declaration, 1 text catalog, 2 `ctsdata` blocks (diplomatic and normalized editions), and CITE blocks for data models, collections and relation sets (28 `datamodels` blocks, 18 `citecollections` blocks, 13 `citerelationset` blocks).

## Texts

The catalog lists 16 versions; 12 are online in this release (each in a diplomatic and a normalized edition, 40,485 passages per edition). Four *Iliad* versions are catalogued but not online: Escorial Ω 1.12, Marciana 841, Geneva 44 and Laurentian 32.3.

| Text | Passages per edition | DSE records |
|---|---|---|
| *Iliad*, Venetus A | 15,645 | 15,618 |
| *Iliad*, Venetus B (book 9 only) | 710 | **0** |
| *Iliad*, Escorial Υ 1.1 (books 8-10) | 1,694 | 1,693 |
| Selected poems, Venetus A | 31 | 31 |
| Scholia, Venetus A main | 9,461 | 3,994 |
| Scholia, Venetus A interlinear | 4,674 | 1,874 |
| Scholia, Venetus A intermarginal | 3,446 | 1,496 |
| Scholia, Venetus A interior | 2,214 | 985 |
| Scholia, Venetus A exterior | 702 | 317 |
| Scholia, Venetus A outside five zones | 6 | 2 |
| Scholia, Venetus B | 648 | **0** |
| Scholia, Escorial Υ 1.1 | 1,254 | 654 |

Scholia are cited by book, scholion and part, so passage counts exceed the number of scholia and are not directly comparable with DSE records, which are per scholion.

## Indexing

- **DSE records:** 26,666 in total, linking text passages to image regions and manuscript pages. Two stray records use obsolete edition IDs (`msAmeta.hmt`, `msAimLater.hmt`).
- **Scholia to *Iliad*:** 11,204 pairs.
- **Name indexes:** 15,594 personal-name, 2,903 place-name and 3,034 ethnic-group references.
- ***Iliad* lines to pages:** Venetus B 15,687; Escorial Ω 1.12 15,633; Marciana 841 6,795; Burney 86 632.
- **Paragraphing of *Iliad* lines:** Escorial Ω 1.12 (612), Burney 86 (47).
- **Venetus A page regions of interest:** 631 entries; Comparetti facsimile images to Venetus A pages: 652 entries.

## Manuscripts and images

Seven codices are modelled as sequences of pages: Venetus A (660), Venetus B (680), Escorial Υ 1.1 (660), Escorial Ω 1.12 (437), Marciana 841 (388), Burney 86 (562) and Laurentian 32.3 (862).

Image catalogs (approximate row counts): Venetus A about 970; Comparetti facsimile about 680; Venetus B bifolios 205; Escorial Υ 1.1 bifolios about 520; Escorial Ω 1.12 about 870; Marciana 841 about 780; Burney 86 about 1,120; Geneva 44 about 800; Laurentian 32.3 about 1,720.

## Authority lists

1,300 persons, 482 places, 13 astronomical entities, 8 cited works.

## Data models in use

DSE, codex, citable image, commentary, named-entity index, text-on-page, paragraphing and page-region models. Declarations are listed in the release's `datamodels` blocks.

## Gaps visible in the data

- Venetus B (book 9) has text for both *Iliad* and scholia but no DSE records.
- Many Venetus A scholia have no DSE record, and some DSE records point to text that is not in the editions. `scratch/dse-check.csv` has the detailed report (2,937 rows).
