# Assembling data sets in structured collections #


Data sets are kept as `.csv` files in the [`cite/collections`](https://github.com/homermultitext/hmt-archive/tree/master/cite/collections) directory, and cataloged in an XML document validating against the Relax NG standard for CITE Collection inventories.

Collections of scholia are the one exception.  `.csv` files recording all scholia are in the [`cite/scholiaInventory`](https://github.com/homermultitext/hmt-archive/tree/master/cite/scholiaInventory) directory grouped by book of the *Iliad*.  The gradle `compileScholiaCollection` task creates composite collections from these files, and writes the result in  `build/collections/scholiaInventory.csv`.
