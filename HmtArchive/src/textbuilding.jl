
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
    fullpath = map(f -> iliaddir * f, iliadfiles)
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
    composite_array(corpora)
end