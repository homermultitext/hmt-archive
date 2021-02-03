
diplbuilder = MidDiplomaticBuilder("Diplomatic edition", "dipl")
normbuilder = MidNormalizedBuilder("Normalized edition", "normed")

function iliaddipl()
    edition(normbuilder, iliadxmlcorpus())
end

"Compile a single citable edition of Venetus A Iliad."
function iliadxmlcorpus()
    vailiad = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:")
    iliadsrc = dirname(pwd()) * "/archive/iliad/"
    iliadfiles = filter(f -> endswith(f, "xml"), readdir(iliadsrc))
    fullpath = map(f -> iliadsrc * f, iliadfiles)
    corpora = [] 
    for f in fullpath
        contents = open(f) do file
            read(file, String)
        end
        try 
            push!( corpora, divAbReader(contents, vailiad))
        catch e
            println("ERROR ON $(f) : $(e)")
        end
    end
    composite_array(reverse!(corpora))
end


"Make a CitableCorpus for one scholia document in one book."
function scholiaforbookdoc(docroot, bk)
    wrkcomponent = "tlg5026." *  docroot["n"] * ".hmt"
    baseurn = CtsUrn("urn:cts:greekLit:$(wrkcomponent):")
    body = elements(docroot)[1]
    scholiadivs = elements(body)
    #println("Found ", length(scholiadivs), " scholia for ", baseurn, " in book ", bk)
    citableNodes =  Array{CitableNode}(undef, 0)
    for s in scholiadivs
        scholid = "$(bk)." * s["n"]
        for div in eachelement(s)
            cn = CitableTeiReaders.citeNAttr(div, baseurn, scholid)       
            push!(citableNodes, cn)
        end
    end
    CitableCorpus(citableNodes)
end

"Compile a single citable edition of all Venetus A schola."
function scholiaxmlcorpus()
    # XPaths for finding the parts of the document we need:
    bookxp = "/ns:TEI/ns:text/ns:group"
    docxp = "/ns:TEI/ns:text/ns:group/ns:text"
        
    # collect files:
    scholiasrc = dirname(pwd()) * "/archive/scholia/"
    scholiafiles = filter(f -> endswith(f, "xml"), readdir(scholiasrc))
    fullpath = map(f -> scholiasrc * f, scholiafiles)

    docs = [] 
    allscholia = []
    for f in fullpath
        try 
            doc = readxml(f).root
            # One book per file: use the book-containing element
            # to save the book value we'll need for passage URNs.
            booklevel = findall(bookxp, doc,["ns"=> teins]) 
            book = booklevel[1]["n"]

            # Scholia documents for the book in this file,
            # and their sigla, which we'll use for text ID 
            # in URNs
            scholiadocs = findall(docxp, doc,["ns"=> teins]) 
            sigla = map(root -> root["n"], scholiadocs)

            for sdoc in scholiadocs
                scholia = scholiaforbookdoc(sdoc, book)
                push!(allscholia, scholia)
            end
            
        catch e
            throw(DomainError("ERROR ON $(f) : $(e)"))
        end 
    end
    CitableText.composite_array(allscholia)
end