using HmtArchive, HmtArchive.Analysis
using CitablePhysicalText
using CitableImage

repo = pwd()

#=
# Ultimately, we'll generate manifests for all codices
#modelled in the published release, namely:
"burney86pages"
"e3pages"
"e4pages"
"laur32pages"
"u4pages"
"msA"
"msB"
=#


src = hmt_cex()
mss = hmt_codices(src)

using CitableObject
va = filter(mss) do ms
    collectionid(ms.pages[1].urn) == "msA"
end[1]

imgsvc_baseurl = "http://www.homermultitext.org/iipsrv"
imgsvc_root = "/project/homer/pyramidal/deepzoom"
imgsvc = IIIFservice(imgsvc_baseurl, imgsvc_root)

vamanifest =  "https://raw.githubusercontent.com/homermultitext/hmt-archive/refs/heads/master/iiif/venetusA.json"

vaconfig = iiifconfig(vamanifest)

vajson = iiifmanifest(va, vaconfig, imgsvc)
outdir = joinpath(repo, "iiif")
isdir(outdir)
vaoutfile = joinpath(outdir, "venetusA.json")
open(vaoutfile,"w") do io
    write(io, vajson)
end