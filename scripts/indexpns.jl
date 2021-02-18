# Run this script from the `scripts` directory:
# it activates the environment configured there.
using Pkg
Pkg.activate(".")
Pkg.instantiate

using HmtArchive
using CitableText
using Unicode
using EzXML

hmt = Archive("..")
scholia = scholiaxmlcorpus(hmt)

entries = []
for cn in scholia.corpus
    doc = parsexml(cn.text)
    pns = findall("//persName", doc)
    for pn in pns
        try 
            push!(entries, (cn.urn,pn["n"] ))
        catch e
            oneliner = replace(string(pn), "[ \t\n]+" => " ")
            push!(entries, (cn.urn, "BAD ENTRY $(oneliner)"))
        end
    end
end

strs = map(entry -> entry[1].urn * "|" * entry[2], entries)

open("pnindex.cex", "w") do f
    write(f, join(strs,"\n"))
end