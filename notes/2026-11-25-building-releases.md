# Building a release candidate and a published release

Summary for end users. Based on `HmtArchive.jl` (`src/publish.jl`, `src/archive.jl`), `quarto/tutorials/publish.qmd` in that repository, and the layout and history of this repository. Steps marked *(convention)* are inferred from how past releases were committed; no code performs them.

## Prerequisites

- Julia, with the dependencies of `HmtArchive.jl` installed (from its `test` directory: `julia --project=.. -e 'using Pkg; Pkg.instantiate()'`).
- `hmt-archive` and `HmtArchive.jl` cloned side by side in the same parent directory.
- Internet access: the authority lists (persons, places, astronomy, cited works) are always fetched from the `hmt-authlists` repository on GitHub, even when building locally.

## What goes into a build

The build reads from `archive/`: TEI editions (`tei-editions`), DSE records (`dse-data`), text catalog and configuration (`textconfigs`), `datamodels/datamodels.cex`, `library/header.cex`, and the pre-formatted CEX in `codices`, `images` and `relations`. It composes diplomatic and normalized editions of all texts, the scholia-to-*Iliad* index, and indexes of person, place and ethnic-group names. Nothing in `archive/` is changed by a build.

## 1. Build a release candidate

From the `HmtArchive.jl` directory:

```julia
using HmtArchive
hmt = adjacent()                 # finds ../hmt-archive/archive
writerc(hmt, "2025a")            # writes ../hmt-archive/release-candidates/hmt-2025a.cex
```

- Release IDs follow `YYYYC`: four-digit year plus a letter that increases with each release in that year (the last release is `2024c`, so the next would be `2025a`).
- The build takes a while and logs progress. Read the warnings, especially the list of scholia that could not be mapped to *Iliad* passages.
- Release candidates are git-ignored (`release-candidates/hmt*cex`), so they are never committed.
- If the repositories are not side by side, use `Archive("/full/path/to/hmt-archive/archive")` instead of `adjacent()`.

## 2. Validate the candidate

Load the candidate as text and run the library's checks, for example:

```julia
using HmtArchive.Analysis
src = read("../hmt-archive/release-candidates/hmt-2025a.cex", String)
hmt_releaseinfo(src)             # confirm the release ID
missingtexts(src)                # DSE records pointing to text that is not in the editions
dsec = hmt_dse(src)[1]
missingdse(hmt_normalized(src), dsec)   # text passages with no DSE record
scholiapagecheck(src)            # scholia indexed to a different page than their Iliad line
```

`scholiapagecheck` is new (uncommitted when this note was written). `scripts/analyzeindexing.jl` in `HmtArchive.jl` gives a per-page report for the Venetus A. Fix problems in `archive/`, then rebuild the candidate.

## 3. Publish

```julia
publish(hmt, "2025a")            # writes ../hmt-archive/releases-cex/hmt-2025a.cex
```

Then, by convention:

1. Copy the new file to `releases-cex/hmt-current.cex`. The library's `hmt_cex()` and the Quarto guides read this file from GitHub `master`.
2. File the previous release in its year folder if needed (for example `releases-cex/2024/hmt-2024c.cex`).
3. Add a line to `releases-cex/releases.md` describing the release.
4. Commit and push. The functions above never touch git; that is deliberate.

Recent releases (2022 onward) have no `-corrigenda.md` or `-summary` folder, although `releases-cex/README.md` describes them; nothing in `HmtArchive.jl` writes those.

## 4. After publishing

`buildiiif.jl` (in the root of this repository, run from here) builds IIIF manifests from the current release **downloaded from GitHub**, so run it only after the new `hmt-current.cex` is pushed to `master`. It writes manifest JSON files into `iiif/`; the Escorial manifests are commented out in the script.
