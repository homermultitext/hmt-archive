---
layout: page
title: HMT archive
---

The github repository at <https://github.com/homermultitext/hmt-archive> is the central repository for archival data from the Homer Multitext project (HMT).


## Unified publication system

Beginning in 2018, we are releasing published versions of the HMT archive as a single plain-text file in [CEX format](https://cite-architecture.github.io/citedx/CEX-spec-3.0.1/).  These are committed in the `releases-cex` directory.

The CEX-format releases are automatically assembled from source files in the `archive` directory.  Working with [CITE architecture code libraries](https://cite-architecture.github.io/) that can directly serialize our data models to CEX format, we assemble CEX files for individual parts of our work (e.g., catalogs of binary images, models of ordered pages in a codex, an diplomatic editions of texts).  CEX allows us simply to concatenate the individual CEX files into a single, syntactically valid composite.


## Earlier build systems

Prior to 2018, we used a gradle build system for assembling packages of published material that has passed final editorial review and verification into a unified representation in RDF.  That system is no longer used but is documented [here](priorwork/groovy_builder).
