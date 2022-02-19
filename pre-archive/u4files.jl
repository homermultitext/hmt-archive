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

# sequence|image|urn|rv|label