# Activate the environment in the root of this repository
# before using this script.
using CitableText
using CitableCorpus
using EditorsRepo
using CiteEXchange

archiveroot = string(pwd(), "/archive")
repo = repository(archiveroot; editions= "tei-editions", dse="dse-data", config="textconfigs")

citation = citation_df(repo)

string(repo.editions, "/catalog.cex")
# Create a citable corpus of archival text in a repo
function archivalcorpus(r::EditingRepository, citesdf)
    urns = citesdf[:, :urn]

    corpora = []
    for u in urns
        # 1. Read the source text (here, XML)
        src = textsourceforurn(r, u)
        if isnothing(src)
            # skip it
        else
            # 2. get the EditionBuilder for the urn
            reader = ohco2forurn(citesdf, u)
            # 3. create citable corpus of the archival version
            push!(corpora, reader(src, u))
        end
    end
    CitableCorpus.composite_array(corpora)
end

archivaltexts = archivalcorpus(repo, citation)

texts = texturns(repo)
println(join(texts,"\n"))
normednodes = []
for t in texts
    nds = normalizednodes(repo, t)
    push!(normednodes, nds)        
end
normed = filter(nodelist -> ! isnothing(nodelist), normednodes) |> Iterators.flatten |> collect |> CitableTextCorpus
normed.corpus |> length

nonempty = filter(cn -> ! isempty(cn.text), normed.corpus)
iliadlines = filter(cn -> contains(cn.urn.urn, "tlg0012"),  nonempty)
schnodes = filter(cn -> contains(cn.urn.urn, "tlg5026"),  nonempty)
schcomments = filter(cn -> endswith(passagecomponent(cn.urn),"comment"), schnodes)
reff = filter(cn -> endswith(passagecomponent(cn.urn), "ref"), normed.corpus)

alldse = dse_df(repo)


ilb = filter(cn -> contains(workcomponent(cn.urn),"msB") ,iliadlines)
ilsch = filter(cn -> contains(workcomponent(cn.urn),"msB") ,schnodes)
