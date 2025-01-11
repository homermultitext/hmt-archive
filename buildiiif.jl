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



msT = mss[1]
burneymanifest =  "https://raw.githubusercontent.com/homermultitext/hmt-archive/refs/heads/master/iiif/burney86.json"
burneyconfig = iiifconfig(burneymanifest)
burneyjson = iiifmanifest(msT, burneyconfig, imgsvc)

isdir(outdir)
burneyoutfile = joinpath(outdir, "burney86.json")
open(burneyoutfile,"w") do io
    write(io, burneyjson)
end



#= NFG
e3 = mss[2]
e3manifest =  "https://raw.githubusercontent.com/homermultitext/hmt-archive/refs/heads/master/iiif/escorial_upsilon_1_1.json"
e3config = iiifconfig(e3manifest)

e3json = iiifmanifest(e3, e3config, imgsvc)

e3outfile = joinpath(outdir, "upsilon_1_1.json")
open(e3outfile,"w") do io
    write(io, e3json)
end


e4 = mss[3]
e4manifest =  "https://raw.githubusercontent.com/homermultitext/hmt-archive/refs/heads/master/iiif/escorial_omega_1_12.json"
e4config = iiifconfig(e4manifest)

e4json = iiifmanifest(e4, e4config, imgsvc)

e4outfile = joinpath(outdir, "omega_1_12.json")
open(e4outfile,"w") do io
    write(io, e4json)
end
=#
mss


m841 = mss[5]
m841manifest =  "https://raw.githubusercontent.com/homermultitext/hmt-archive/refs/heads/master/iiif/marciana841.json"
m841config = iiifconfig(m841manifest)

m841json = iiifmanifest(m841, m841config, imgsvc)

m841outfile = joinpath(outdir, "marciana_841.json")
open(m841outfile,"w") do io
    write(io, m841json)
end
