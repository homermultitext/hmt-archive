# Build composite editions from multiple sources.
using CitableTeiReaders
using CitableText

iliadurn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001.msA:")
iliaddir = dirname(pwd()) * "/archive/iliad/"


iliadfiles = filter(f -> endswith(f, "xml"), readdir(iliaddir))
fullpath = map(f -> iliaddir * f, iliadfiles)
corpora = [] 
for f in fullpath
	contents = open(f) do file
		read(file, String)
	end
	try 
		push!( corpora, divAbReader(contents, iliadurn))
	catch e
		println("ERROR ON $(f) : $(e)")
	end
end


#=
f= repo.root * "/" * repo.editions * "/" *	row[1,:file]
		contents = open(f) do file
			read(file, String)
		end
        contents
=#