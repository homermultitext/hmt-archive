# The Homer Multitext project archive

This is the central repository for archival data from the Homer Multitext project.


## What's in this repository

Published releases are machine-generated and validated from data in the [archive](./archive) directory.  The contents of the `archive` directory are compiled into a [CITE library](https://cite-architecture.github.io/scm/), which can be written as a single, plain-text file in the [CEX format](https://github.com/cite-architecture/citedx).  Once a library has been validated, its CEX file is committed to the [releases-cex](./releases-cex) directory. You'll find notes on the contents and level of validation applied to each release in [releases-cex/releases.md](./releases-cex/releases.md)

The [scripts](./scripts) directory includes a Scala script that can be used to build a release.

The [src](./src) directory tree includes a Scala library  (`org.homermultitext.hmtcexbuilder`) that understands the organization of this repository, and can construct a unified CITE library from the contents of the `archive` directory.  (You'll find release nodes on the code library in [code-releases.md](./code-releases.md).)

Other directories include work in progress and older work being updated to conform with current HMT project standards.





## Guide to HMT project publications

-   See a [guide to understanding HMT project publications](overview.md) (2018).


## Contributors

Contributors to releases published in 2018 are [listed here](contributors/2018.md).  If you know of any name that is missing, please get in touch with us.  (You may file an issue directly on our issue tracker if you're a github user.)
