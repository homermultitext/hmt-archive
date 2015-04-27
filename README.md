# hmtarchive #

This is the central repository for archival data from the Homer Multitext project.

It is organized in the following main directories:

- `archive`:  the archival data set.  All material in this archive has passed initial draft status, and is provisionally accepted for publication.
- `hmtmgr`:  a code library for assembling the various individual components (such as book-by-book editions of different versions of the *Iliad*) into single composite objects (such as a complete edition of one version of the *Iliad*).
- `ctsbldr` and `citebldr`:  build systems using the library in `hmtmgr` to construct a set of generic CITE repositories that can be managed with the [CITE Manager library](http://cite-architecture.github.io/citemgr/).
- `publisher`:  a build system that collects archival material in zip files, and publishes them to a nexus repository.

