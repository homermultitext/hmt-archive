# Run this script from the `scripts` directory:
# it activates the environment configured there.
using Pkg
Pkg.activate(".")
Pkg.instantiate

using HmtArchive
using CitableText
using Unicode

hmt = Archive("..")
normed = scholianormed(hmt)
lc = map(cn -> CitableNode(cn.urn, lowercase(cn.text)), normed.corpus)
stripped = map(cn -> CitableNode(cn.urn, Unicode.normalize(cn.text,stripmark=true)), lc)
corp = CitableCorpus(stripped)
println(cex(corp, "\t"))