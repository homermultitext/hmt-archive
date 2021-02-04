#=
Functions for debugging pieces   of the archive.
=#


"Compile normalized text for scholia to 1 book of Iliad."
function scholianormbook(booknum::Int)
    corpus = scholiaxmlbook(booknum)
    edition(normbuilder,corpus)
end

"Compile a single citable edition of all Venetus A scholia in a given book."
function scholiaxmlbook(booknum::Int)
    # XPaths for finding the parts of the document we need:
    bookxp = "/ns:TEI/ns:text/ns:group"
    docxp = "/ns:TEI/ns:text/ns:group/ns:text"
        
    # collect files:
    scholiasrc = dirname(pwd()) * "/archive/scholia/"
    scholiafiles = filter(f -> endswith(f, "xml"), readdir(scholiasrc))
    fullpath = map(f -> scholiasrc * f, scholiafiles)

    docs = [] 
    allscholia = []
    f = fullpath[booknum]
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
    CitableText.composite_array(allscholia)
end
    
