


teins = "http://www.tei-c.org/ns/1.0"

iliadxml = iliadxmlcorpus()


function persnames(c::CitableCorpus)
    pnlist = []
    for cn in c.corpus
        push!(pnlist, persnames(cn))
    end
    pnlist
end

"Find persName elements in a CitableNode from XML edition"
function persnames(cn::CitableNode)
    pnlist = []
    doc = parsexml(cn.text)
    #xp = "/ns:TEI/ns:text/ns:body//ns:persName"
    xp = "//persName"
    pns = findall(xp, doc.root,["ns"=> teins])
    for pn in pns
        try
            urn = Cite2Urn(pn["n"])
            push!(pnlist, (cn.urn, urn))
        catch e
            push!(pnlist, (cn.urn, "FAILED with content $(pn.content):  $(e)"))
        end
    end
    pnlist
end