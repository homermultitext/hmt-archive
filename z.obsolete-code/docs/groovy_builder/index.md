---
layout: page
title: HMT archive
---

The github repository at <https://github.com/homermultitext/hmt-archive> is the central repository for archival data from the Homer Multitext project.

The repository includes a gradle build system for assembling packages of published material that has passed final editorial review and verification.

## Versions and branches

The master branch normally maintains the most recent *published* release of the archive (currently, `2014-1`).  Work on the next publication is normally in a development branch.  

The next release is planned for August, 2016 (edition `2016.1`), and material for that release is now in the master branch of the repository.

## Organization of subprojects and subdirectories ##

### Data

- `archive`:  the archival data set.  All material in this archive has passed initial draft status, and is provisionally accepted for publication.


### Subprojects for assembling a publishable release ###


- `hmtmgr`:  a code library for assembling the various individual components (such as book-by-book editions of different versions of the *Iliad*) into composite objects (such as a complete edition of one version of the *Iliad*).
- `ctsbldr` and `citebldr`:  build systems constructing a set of generic CITE repositories that can be managed with the [CITE Manager library](http://cite-architecture.github.io/citemgr/).
- `textmgr`: a build system for analyzing the contents of the CTS repository created by `ctsbldr`.  
- `graphs`:  a build system for unifying CITE and CTS repositories in an RDF graph.
- `publisher`:  a build system that collects archival material in zip files, and publishes them to a nexus repository.


See more information about [how a publishable release is assembled](building).


## Other ##


- `confs`: configuration settings for all subprojects
- `schemas`:  local copies of CTS and CITE schemas
