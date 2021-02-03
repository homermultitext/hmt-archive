
diplbuilder = MidDiplomaticBuilder("Diplomatic edition", "dipl")
normbuilder = MidNormalizedBuilder("Normalized edition", "normed")

# scholiasrc = dirname(pwd()) * "/archive/scholia/"
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

function groupscholia()
    bookxp = "/ns:TEI/ns:text/ns:group"
    docxp = "/ns:TEI/ns:text/ns:group/ns:text"
    nodexp = "/ns:TEI/ns:text/ns:group/ns:text/ns:body/ns:div"
    scholiagroup = CtsUrn("urn:cts:greekLit:tlg5026:")
    scholiasrc = dirname(pwd()) * "/archive/scholia/"
    scholiafiles = filter(f -> endswith(f, "xml"), readdir(scholiasrc))
    fullpath = map(f -> scholiasrc * f, scholiafiles)
    docs = [] 
    for f in fullpath
        try 
            booklevel = findall(bookxp, doc,["ns"=> teins]) 
            book = booklevel[1]["n"]
            scholiadocs = findall(docxp, doc,["ns"=> teins]) 
            sigla = map(root -> root["n"], scholiadocs)
            # nodexp
            # Now find nodes for each doc
            # Use method call directly in CitableTei lib to convert xml node to 
            # Citable node
            
        catch e
            throw(DomainError("ERROR ON $(f) : $(e)"))
        end
    end


    
   
    return reverse!(docs)
end