---
title: Building a publishable release of the archive
layout: page
---

## Building the RDF graph ##

`:graph:ttl` combines separate RDF graphs generated for

- texts (`graph:ctsttl`)
- structured data collections (`graph:ccttl`)
- indexing data (`graph:idxttl`)
- images collections (`graph:imgttl`)



### Texts ###

1. `:ctsbldr:cts` creates a CTS repository for all texts that `citemgr` library can use
2. `:textmgr:ctstab` tabulates this repository
3. `:graph:ctsttl` creates RDF from tabulated texts



## Packaging other data sets ##

TBA


## Publication as maven artifacts

TBA
