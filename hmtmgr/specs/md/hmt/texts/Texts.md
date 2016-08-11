# Assembling editions of texts #


TEI-conformant XML editions follow the HMT project [guide to editorial policies](http://homermultitext.github.io/hmt-editors-guide/).  Editions of material from manuscripts are grouped in separate files by text or document and book of the *Iliad*.   For the Venetus A manuscript, we treat the carefully distinguished sets of scholia as distinct documents. All these XML files are found in the [`editions`](https://github.com/homermultitext/hmt-archive/tree/master/editions) directory of the repository.


The gradle `editions` task collects material from the separate files and creates a single composite encompassing all books of the *Iliad* for both *Iliad* and scholia.   For these composites, it draws information for the TEI `header` element from the directory[ `teiHeaders`](https://github.com/homermultitext/hmt-archive/tree/master/teiHeaders):  any TEI header information in the work by individual books is ignored.

