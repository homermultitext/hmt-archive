# Usage of `choice` in the TEI editions

Survey of every `.xml` file under `archive/tei-editions` and its subdirectories (57 files: 28 in `iliad`, 28 in `scholia`, 1 in `other`), run on 2026-11-27. All files parse as well-formed XML. For each `choice` element I counted its immediate child elements by name; order is ignored, so `sic`+`corr` and `corr`+`sic` count together.

## Result

There are 5,671 `choice` elements. **Every one has exactly two immediate children**, and only three pairs occur:

| Pair | Occurrences | In `scholia` | In `iliad` |
|---|---|---|---|
| `abbr` + `expan` | 4,722 | 4,659 | 63 |
| `corr` + `sic` | 725 | 561 | 164 |
| `orig` + `reg` | 224 | 37 | 187 |
| **Total** | **5,671** | **5,257** | **414** |

The `other` directory has no `choice` elements.

## Nested choices

Five `choice` elements contain another `choice` further down inside one of their two children. These are not violations of the two-children rule, but are worth knowing about:

| File | Outer pair | Inner pair |
|---|---|---|
| `scholia/VenetusA-Scholia-12.xml` | `abbr`+`expan` | `corr`+`sic` |
| `scholia/VenetusA-Scholia-21.xml` (twice) | `sic`+`corr` | `abbr`+`expan` |
| `scholia/VenetusA-Scholia-22.xml` | `abbr`+`expan` | `sic`+`corr` |
| `iliad/VenetusA-Iliad-21.xml` | `sic`+`corr` | `abbr`+`expan` |

## Method

Parsed each file with Python's `xml.etree.ElementTree`, matched elements by local name (ignoring the TEI namespace), and counted only element children, so text and comments between children are not counted. Read-only: nothing under `archive` was changed.
