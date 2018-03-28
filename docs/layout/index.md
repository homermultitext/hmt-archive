---
layout: page
title: Organization of the archive
---


## Release metadata

In the root `archive` directory, the file `library.cex` contains the summary description and identifier for the release currently in development.


## Archival data in CEX format

The following subdirectories contain archival data in [CEX format](https://cite-architecture.github.io/citedx/CEX-spec-3.0.1/).


-   `images`: catalogs of [citable images, following the CITE architecture model](http://cite-architecture.github.io/imagemodel/).
    -   TBA:  (these collections also implement the CITE architecture model of binary image data)
-   `codices`:  collections documenting the sequence of pages of a manuscript, following the more general [CITE model of text-bearing surfaces](http://cite-architecture.github.io/tbsmodel/)
-   `dse`:  the relations of text, text-bearing surface and documentary image that make up a [full diplomatic scholarly edition](https://cite-architecture.github.io/dse/)
-   `editions`:  editions of cataloged [citable texts in the OHCO2 model](https://cite-architecture.github.io/ohco2/).  These are automatically generated from the archival XML editions listed below.
-   `commentaries-annotations`: TBA


## Archival editions in TEI-compliant XML


-   `iliad`: Separate XML files organized by manuscript and book of the *Iliad*.  In the build process, these files are assembled into a single XML edition for each manuscript.  The composite is written to the `iliad-composites` directory, where cataloging information in CEX format is already available.  The composite XML files are used to generate all CEX editions, which are written to the `editions` directory (as noted above).
-   `scholia`:  Separate XML files organized by manuscript and book of the *Iliad*.  In the build process, these files are assembled into a single XML edition for each scholia document per manuscript.  (For example, for the Venetus A manuscript, the build process assembles eight distinct scholia documents defined by the distinct zones in the manuscript's layout.)  The composite edition is written to the `scholia-composites` directory, where cataloging information in CEX format is already available.  The composite XML files are used to generate all CEX editions, which are written to the `editions` directory (as noted above).
