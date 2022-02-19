f = joinpath(pwd(), "u4-filenames.txt")
isfile(f)

lns = readlines(f)

prs = []
for ln in lns
    pg = replace(ln, r"U4[0]*([^_]+).+" => s"U4_\1")
    tidier = replace(lowercase(pg), r"([rv])n" => s"\1")
    push!(prs, (ln, tidier))
end
natlight = filter(pr -> ! occursin("uv", pr[2]),  prs)
uvlist =  filter(pr -> occursin("uv", pr[2]),  prs)
pagelist = natlight[1:388]

imgurnbase =  "urn:cite2:hmt:u4img.2019a:"
pgurnbase =  "urn:cite2:hmt:u4pages.v1:"
i = 0
cex  = []
for pr in pagelist
    i = i + 1
    imgurn = imgurnbase * pr[1]
    pgside = replace(pr[2], "u4_" => "")
    pgurn = pgurnbase  * pgside
    pg = replace(pgside, r"[rv]" => "")
    rv = endswith(pgside, "r") ? "recto" : "verso"
    lbl = "Marciana 841, folio $(pg), $(rv)."
    # sequence|image|urn|rv|label
    cexline = join([i, imgurn,pgurn, rv, lbl ], "|")
    push!(cex, cexline)
end

open("u4codex.cex", "w") do io
    write(io, join(cex,"\n"))
end

#=FILES TO CHECK OR RENAME:
√ u4_19rv: renamed
√ u4_77rv: renamed
√ u4_191rr BAD IMAGE: STRIKE
=#
