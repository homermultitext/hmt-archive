# Specifications for the Homer Multitext project archive, version @version@ #

The code in this repository is used to manage the archival data of the Homer Multitext project (HMT).

Validation and verification of data are done in other repositories:  when material passes a defined level of validation, it is added to the data sources here.  The principal function of this repository is to assemble material for published releases of the archive.

How the archive:

- assembles <a concordion:run="concordion" href="texts/Texts.html">editions of texts</a>
- assembles catalogs of digital images
- assembles <a concordion:run="concordion" href="collections/Collections.html">data sets in structured collections</a>
- assembles indexing and cross references
- generates RDF representations of all the above
- <a concordion:run="concordion"  href="packages/Packages.html">groups and packages of data sets for publication</a>






## Relation to other code packages ##

The code in `hmt-archive` relies on a number of [other packages](dependencies/Dependencies.html)
