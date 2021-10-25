# Run this from root of hmt-archive.
using Kanones.FstBuilder
using Kanones
using CitableParserBuilder
using CitableText, CitableCorpus
using PolytonicGreek, Orthography, ManuscriptOrthography


# Build scholia parser
function scholiaparser(kdir, lexdir)
    fstsrc  =  joinpath(kdir, "fst")
    coreinfl = joinpath(kdir, "datasets", "core-infl")
    corevocab = joinpath(kdir, "datasets", "core-vocab")
    lysias = joinpath(kdir, "datasets", "lysias")
    
    scholia = joinpath(lexdir, "kdata", "scholia")
    datasets = [coreinfl, corevocab, lysias, scholia]
    kd = Kanones.Dataset(datasets)

    tgt = joinpath(tempdir(),  "kanones")
    buildparser(kd,fstsrc, tgt; force = true)
end

# Parse corpus and write results to CEX file
function reparse(tkncorpus, parser)
    parsed = parsecorpus(tkncorpus, parser)
    outfile = joinpath(pwd(), "analyses", "scholia_parsed.cex")
    open(outfile,"w") do io
        write(io, delimited(parsed))
    end
    @info("Wrote analyses to ", outfile)
end

# Force-rebuild parser, reparse everything
function rebuild(kanonesrepo, lexiconrepo, tknized)
    p = scholiaparser(kanonesrepo, lexiconrepo)
    reparse(tknized, p)
end


# Load corpus of scholia, tokenize:
f = joinpath(pwd(), "releases-cex", "hmt-2020i.cex")
c = corpus_fromcex(read(f); delimiter = "#")
scholiaurn = CtsUrn("urn:cts:greekLit:tlg5026:")
scholia = filter(psg -> urncontains(scholiaurn, psg.urn),  c.passages) |> CitableTextCorpus
ortho =  msGreek()
tknized = tokenizedcorpus(scholia,ortho)


# Default expectation: Kanones and lexicon are checked out in adjacent directories.
krepo = joinpath(dirname(pwd()), "Kanones.jl")
lexrepo = joinpath(dirname(pwd()), "hmt-lexicon")
p = scholiaparser(krepo, lexrepo)




# Repeat this as needed:
rebuild(krepo, lexrepo, tknized)

# Round trip:
tokenfile = joinpath(pwd(), "analyses", "scholia_parsed.cex")

tkns = read(f, String) |> CitableParserBuilder.analyzedtokens_fromabbrcex

