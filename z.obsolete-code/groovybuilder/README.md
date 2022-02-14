# hmtarchive #

This is the central repository for archival data from the Homer Multitext project.

For details about how the project is organized and managed, including how published datasets are released, see the web site at <http://homermultitext.github.io/hmt-archive/>.  The documentation there is up to date for the current planned release, `2016.1`.

## Brief overview

To build a complete RDF graph of the archive:

1. `gradle graph:ctsttl` (builds `graphs/build/ttl/cts.ttl`)
2. `gradle graph:ccttl` (builds `graphs/build/ttl/collections.ttl`)
3. `gradle graph:imgttl` (builds `graphs/build/ttl/citeimgs.ttl`)
4. `gradle graph:idxttl` (builds `graphs/build/ttl/indices.ttl`)
5. `gradle graph:ttl` (builds `graphs/build/ttl/all.ttl`)
