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


vb = mss[7]
oops1_1 = mss[2]
omega_1_12 = mss[3]

function findbad(pagelist)
    bad = []
    for pg in pagelist
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
    bad
end

oopsbad = findbad(oops1_1)
open(joinpath("notes-ms-photography", "upsilon_1_1-bad.txt"), "w") do io
    write(io, join(oopsbad,"\n"))
end

#=
open("vb-bad.txt", "w") do io
    write(io, join(bad,"\n"))
end
=#



omega_1_12 = mss[3]
ω1_12bad = findbad(omega_1_12)
open(joinpath("notes-ms-photography", "omega_1_12-bad.txt"), "w") do io
    write(io, join(ω1_12bad,"\n"))
end