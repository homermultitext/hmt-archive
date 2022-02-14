# HMT project command-line utilities #

Some helpful utilities in managing and upgrading legacy work in HMT to current formats or other requirements.

`teiToCsv.groovy`
: Convert tables in a TEI XML document csv format

`expandScholiaReff.groovy`
: Cycles through a TEI edition of scholia looking for `p` elements within `div`s of type `ref`, and prepends `urn:cts:greekLit:tlg0012.tlg001.msA:` to text contents.