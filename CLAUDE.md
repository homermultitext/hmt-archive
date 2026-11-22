

## Overview

`hmt-archive` is a github repository for managing archival data from the Homer Multiext project. It uses the Julia library `HmtArchive` to build releases in a single plain-text file.


## Functionality

The `HmtArchive.jl` package will be used to periodically write release candidates to the `release-candidates` directory as corrections are made.


## Architecture & Directory Structure

- `quarto`, `docs`, `README.md`, `releases.md`, are all user-readable content owned by me; Claude should not touch them
- `notes` is for documentation and notes that Claude writes, includes session summaries so I can reliably pick up working with Claude from different machines
- `archive` and `iiif` are the two directories that are used to build the release data that is written to `release-candidates`

I will manage all github interactions (commits, pushes): CLAUDE should never do that.