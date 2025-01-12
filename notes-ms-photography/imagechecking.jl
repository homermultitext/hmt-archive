pwd()

using HmtArchive.Analysis
using CitablePhysicalText
using CitableImage

imgsvc_baseurl = "http://www.homermultitext.org/iipsrv"
imgsvc_root = "/project/homer/pyramidal/deepzoom"
imgsvc = IIIFservice(imgsvc_baseurl, imgsvc_root)


src = hmt_cex()
mss = hmt_codices(src)

#vbimglist = map(pg -> pg.image.urn, mss[end].pages)

bad = []
vb = mss[7]
for pg in vb.pages[8:end]
    #@info("Testing $(pg.image)")
    try 
        CitablePhysicalText.imageinfo(pg, imgsvc)
        @info("Success on $(pg.image)")
    catch e
        push!(bad, pg.image)
        @warn("Failed on $(pg.image)")
        @warn(e)
    end
end

open("vb-bad.txt", "w") do io
    write(io, join(bad,"\n"))
end