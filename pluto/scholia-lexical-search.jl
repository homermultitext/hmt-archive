### A Pluto.jl notebook ###
# v0.16.4

using Markdown
using InteractiveUtils

# This Pluto notebook uses @bind for interactivity. When running this notebook outside of Pluto, the following 'mock version' of @bind gives bound variables a default value (instead of an error).
macro bind(def, element)
    quote
        local el = $(esc(element))
        global $(esc(def)) = Core.applicable(Base.get, el) ? Base.get(el) : missing
        el
    end
end

# ╔═╡ a6bfa46c-d7d6-4ef8-b9b3-4b971c877b60
begin
	using CitableParserBuilder
	using CitableText
	using CitableCorpus
	using Kanones
	using Orthography
	using PolytonicGreek
	using DataFrames
	
	using PlutoUI
end

# ╔═╡ 8568c8fc-35bf-11ec-345e-25f2feeb26f0
md"# Lexical search of *scholia*"

# ╔═╡ bcacc767-6709-4851-92ad-8a9823ca26fc
@bind loadem Button("Reload data")

# ╔═╡ c0c0954c-9b08-46f0-8dda-bd6771a37d69
md">UI"

# ╔═╡ c7fd9f27-5c75-4765-a8ff-8d967ba358d4
css = html"""
<style>
.hilited {
background-color: yellow;
}
</style>
"""

# ╔═╡ 51a3fb01-aa28-4b9c-9814-9d3efe304cd8
md"> Data"

# ╔═╡ 0c420aef-135d-4743-9703-bdf61e9e7d30
tkns = begin
	loadem
	f = joinpath(dirname(pwd()), "analyses", "scholia_parsed.cex")
	read(f, String) |> CitableParserBuilder.analyzedtokens_fromabbrcex
end

# ╔═╡ 3a11e0c0-feed-4e71-8991-8aef9ccaf481
lexids = lexemes(tkns)

# ╔═╡ 3ed4f761-8f12-46a2-8d87-864b029e334d
lsjlabels = Kanones.lsjdict_fromurl()

# ╔═╡ 646ee9a2-c6fc-4a42-aba2-7a407036a5b9
lexmenu = begin
	lexdict = Pair{String, String}[]
	for l in lexids
		if haskey(lsjlabels, l)
			push!(lexdict, l =>  join([lsjlabels[l], " (", l, ")"])   )
		end
	end
	sort(lexdict, lt = (x,y) -> x[2] < y[2])
end

# ╔═╡ e9b9a397-7cf8-410a-a4c0-5e2d8ae46b15
md"""*Choose a lexeme*: $(@bind lexchoice Select(lexmenu))"""

# ╔═╡ 768a5e5d-3817-413f-a472-8e9dcecb67f5
function formatpassage(scholurn,  num)

	scholtokens = filter(t -> urncontains(scholurn, t.passage.urn), tkns)
	tokenhtml = ["<li> <b><i>$(num)</i></b>. <b> $(workcomponent(scholurn)), $(passagecomponent(scholurn))</b>"]
	
	for t in scholtokens
		if isempty(t.analyses)
			push!(tokenhtml, t.passage.text * "</li>")
		else
			if string(t.analyses[1].lexeme) == lexchoice
				push!(tokenhtml, "<span class=\"hilited\">$(t.passage.text)</span></li>")
			else
				push!(tokenhtml, t.passage.text * "</li>")
			end
		end
	end
	
	join(tokenhtml, " ")
end

# ╔═╡ 95971c5c-e0fc-4b0e-bf55-8701d2bfe1b9
lexpassages = CitableParserBuilder.passagesforlexeme(tkns,lexchoice)

# ╔═╡ aa50f476-17aa-45e5-8379-cc2a58c556ce
scholiapassages = map(u -> collapsePassageBy(u,2), lexpassages) |> unique

# ╔═╡ 8ed31067-b737-4558-a056-1d14f8c19eeb
md"""For *$(lexchoice)*, **$(length(lexpassages))** tokens in **$(length(scholiapassages))** passages."""

# ╔═╡ 50042fea-d0a4-49b3-a6fb-4e6a15afd03d
begin
	psglimit = length(scholiapassages) > 40 ? 40 : length(scholiapassages)
	md"""*Max. passages to show*: $(@bind maxpassages Slider(1:10:psglimit; default=10, show_value=true)) *, starting from*: $(@bind startpoint NumberField(1:10:length(scholiapassages)))"""
end


# ╔═╡ 457d2fad-5524-4a3a-80ac-55368c198d8d
begin
	maxendpoint = startpoint + maxpassages
	endpoint = maxendpoint < length(scholiapassages) ? maxendpoint : length(scholiapassages)

	#items = join(map(p -> formatpassage(p), lexpassages))
	items = []
	count = startpoint - 1
	for psg in scholiapassages[startpoint:endpoint]
		count = count + 1
		push!(items, formatpassage(psg, count))
	end
	HTML(join(["<ul>", join(items), "</ul>"]))
end

# ╔═╡ c2cfc47e-8e65-48a3-86f7-ac820e86116c
# http://folio2.furman.edu/lsj/?urn=urn:cite2:hmt:lsj.chicago_md:n20600

# ╔═╡ 00000000-0000-0000-0000-000000000001
PLUTO_PROJECT_TOML_CONTENTS = """
[deps]
CitableCorpus = "cf5ac11a-93ef-4a1a-97a3-f6af101603b5"
CitableParserBuilder = "c834cb9d-35b9-419a-8ff8-ecaeea9e2a2a"
CitableText = "41e66566-473b-49d4-85b7-da83b66615d8"
DataFrames = "a93c6f00-e57d-5684-b7b6-d8193f3e46c0"
Kanones = "107500f9-53d4-4696-8485-0747242ad8bc"
Orthography = "0b4c9448-09b0-4e78-95ea-3eb3328be36d"
PlutoUI = "7f904dfe-b85e-4ff6-b463-dae2292396a8"
PolytonicGreek = "72b824a7-2b4a-40fa-944c-ac4f345dc63a"

[compat]
CitableCorpus = "~0.7.3"
CitableParserBuilder = "~0.20.1"
CitableText = "~0.11.1"
DataFrames = "~1.2.2"
Kanones = "~0.1.8"
Orthography = "~0.15.0"
PlutoUI = "~0.7.16"
PolytonicGreek = "~0.13.2"
"""

# ╔═╡ 00000000-0000-0000-0000-000000000002
PLUTO_MANIFEST_TOML_CONTENTS = """
# This file is machine-generated - editing it directly is not advised

[[ANSIColoredPrinters]]
git-tree-sha1 = "574baf8110975760d391c710b6341da1afa48d8c"
uuid = "a4c015fc-c6ff-483c-b24f-f7ea428134e9"
version = "0.0.1"

[[Adapt]]
deps = ["LinearAlgebra"]
git-tree-sha1 = "84918055d15b3114ede17ac6a7182f68870c16f7"
uuid = "79e6a3ab-5dfb-504d-930d-738a2a938a0e"
version = "3.3.1"

[[ArgTools]]
uuid = "0dad84c5-d112-42e6-8d28-ef12dabb789f"

[[Artifacts]]
uuid = "56f22d72-fd6d-98f1-02f0-08ddc0907c33"

[[AtticGreek]]
deps = ["DocStringExtensions", "Documenter", "Orthography", "PolytonicGreek", "Test", "Unicode"]
git-tree-sha1 = "c963e50843b6bfc52c2516c93db645b0b13f42ef"
uuid = "330c8319-f7ed-461a-8c52-cee5da4c0892"
version = "0.7.3"

[[Base64]]
uuid = "2a0f44e3-6c83-55bd-87e4-b1978d98bd5f"

[[CSV]]
deps = ["CodecZlib", "Dates", "FilePathsBase", "InlineStrings", "Mmap", "Parsers", "PooledArrays", "SentinelArrays", "Tables", "Unicode", "WeakRefStrings"]
git-tree-sha1 = "c0a735698d1a0a388c5c7ae9c7fb3da72fd5424e"
uuid = "336ed68f-0bac-5ca0-87d4-7b16caf5d00b"
version = "0.9.9"

[[ChainRulesCore]]
deps = ["Compat", "LinearAlgebra", "SparseArrays"]
git-tree-sha1 = "0541d306de71e267c1a724f84d44bbc981f287b4"
uuid = "d360d2e6-b24c-11e9-a2a3-2a2ae2dbcce4"
version = "1.10.2"

[[CitableBase]]
deps = ["DocStringExtensions", "Documenter", "Test"]
git-tree-sha1 = "f0615acff31e5aa57d6842c48d816e77537c5c9e"
uuid = "d6f014bd-995c-41bd-9893-703339864534"
version = "4.0.0"

[[CitableCorpus]]
deps = ["CitableBase", "CitableText", "CiteEXchange", "DataFrames", "DocStringExtensions", "Documenter", "HTTP", "Test"]
git-tree-sha1 = "8f71ee5e831c8517ee8338540f6c9bfa73466b89"
uuid = "cf5ac11a-93ef-4a1a-97a3-f6af101603b5"
version = "0.7.3"

[[CitableObject]]
deps = ["CitableBase", "DocStringExtensions", "Documenter", "Test"]
git-tree-sha1 = "051263adee58dd01603a53bbe9ab5c505bc89364"
uuid = "e2b2f5ea-1cd8-4ce8-9b2b-05dad64c2a57"
version = "0.8.3"

[[CitableParserBuilder]]
deps = ["CSV", "CitableBase", "CitableCorpus", "CitableObject", "CitableText", "DataStructures", "DocStringExtensions", "Documenter", "HTTP", "Orthography", "Test", "TypedTables"]
git-tree-sha1 = "0fc01861ac4e16d7ea41606ccd3c3e5ab0dc85bf"
uuid = "c834cb9d-35b9-419a-8ff8-ecaeea9e2a2a"
version = "0.20.1"

[[CitableText]]
deps = ["CitableBase", "DocStringExtensions", "Documenter", "Test"]
git-tree-sha1 = "33b778552144c9560870fa86c47b43cc597f489f"
uuid = "41e66566-473b-49d4-85b7-da83b66615d8"
version = "0.11.1"

[[CiteEXchange]]
deps = ["CSV", "CitableObject", "DocStringExtensions", "Documenter", "HTTP", "Test"]
git-tree-sha1 = "26156894cf3a817910adf9cc59c7d4625af72f67"
uuid = "e2e9ead3-1b6c-4e96-b95f-43e6ab899178"
version = "0.4.6"

[[CodecZlib]]
deps = ["TranscodingStreams", "Zlib_jll"]
git-tree-sha1 = "ded953804d019afa9a3f98981d99b33e3db7b6da"
uuid = "944b1d66-785c-5afd-91f1-9de20f533193"
version = "0.7.0"

[[Compat]]
deps = ["Base64", "Dates", "DelimitedFiles", "Distributed", "InteractiveUtils", "LibGit2", "Libdl", "LinearAlgebra", "Markdown", "Mmap", "Pkg", "Printf", "REPL", "Random", "SHA", "Serialization", "SharedArrays", "Sockets", "SparseArrays", "Statistics", "Test", "UUIDs", "Unicode"]
git-tree-sha1 = "31d0151f5716b655421d9d75b7fa74cc4e744df2"
uuid = "34da2185-b29b-5c13-b0c7-acf172513d20"
version = "3.39.0"

[[Crayons]]
git-tree-sha1 = "3f71217b538d7aaee0b69ab47d9b7724ca8afa0d"
uuid = "a8cc5b0e-0ffa-5ad4-8c14-923d3ee1735f"
version = "4.0.4"

[[DataAPI]]
git-tree-sha1 = "cc70b17275652eb47bc9e5f81635981f13cea5c8"
uuid = "9a962f9c-6df0-11e9-0e5d-c546b8b5ee8a"
version = "1.9.0"

[[DataFrames]]
deps = ["Compat", "DataAPI", "Future", "InvertedIndices", "IteratorInterfaceExtensions", "LinearAlgebra", "Markdown", "Missings", "PooledArrays", "PrettyTables", "Printf", "REPL", "Reexport", "SortingAlgorithms", "Statistics", "TableTraits", "Tables", "Unicode"]
git-tree-sha1 = "d785f42445b63fc86caa08bb9a9351008be9b765"
uuid = "a93c6f00-e57d-5684-b7b6-d8193f3e46c0"
version = "1.2.2"

[[DataStructures]]
deps = ["Compat", "InteractiveUtils", "OrderedCollections"]
git-tree-sha1 = "7d9d316f04214f7efdbb6398d545446e246eff02"
uuid = "864edb3b-99cc-5e75-8d2d-829cb0a9cfe8"
version = "0.18.10"

[[DataValueInterfaces]]
git-tree-sha1 = "bfc1187b79289637fa0ef6d4436ebdfe6905cbd6"
uuid = "e2d170a0-9d28-54be-80f0-106bbe20a464"
version = "1.0.0"

[[Dates]]
deps = ["Printf"]
uuid = "ade2ca70-3891-5945-98fb-dc099432e06a"

[[DelimitedFiles]]
deps = ["Mmap"]
uuid = "8bb1440f-4735-579b-a4ab-409b98df4dab"

[[Dictionaries]]
deps = ["Indexing", "Random"]
git-tree-sha1 = "743cd00ae2a87c338c32bf604e83219b8272ad98"
uuid = "85a47980-9c8c-11e8-2b9f-f7ca1fa99fb4"
version = "0.3.14"

[[Distributed]]
deps = ["Random", "Serialization", "Sockets"]
uuid = "8ba89e20-285c-5b6f-9357-94700520ee1b"

[[DocStringExtensions]]
deps = ["LibGit2"]
git-tree-sha1 = "a32185f5428d3986f47c2ab78b1f216d5e6cc96f"
uuid = "ffbed154-4ef7-542d-bbb7-c09d3a79fcae"
version = "0.8.5"

[[Documenter]]
deps = ["ANSIColoredPrinters", "Base64", "Dates", "DocStringExtensions", "IOCapture", "InteractiveUtils", "JSON", "LibGit2", "Logging", "Markdown", "REPL", "Test", "Unicode"]
git-tree-sha1 = "f425293f7e0acaf9144de6d731772de156676233"
uuid = "e30172f5-a6a5-5a46-863b-614d45cd2de4"
version = "0.27.10"

[[Downloads]]
deps = ["ArgTools", "LibCURL", "NetworkOptions"]
uuid = "f43a241f-c20a-4ad4-852c-f6b1247861c6"

[[FilePathsBase]]
deps = ["Dates", "Mmap", "Printf", "Test", "UUIDs"]
git-tree-sha1 = "7fb0eaac190a7a68a56d2407a6beff1142daf844"
uuid = "48062228-2e41-5def-b9a4-89aafe57970f"
version = "0.9.12"

[[Formatting]]
deps = ["Printf"]
git-tree-sha1 = "8339d61043228fdd3eb658d86c926cb282ae72a8"
uuid = "59287772-0a20-5a39-b81b-1366585eb4c0"
version = "0.4.2"

[[Future]]
deps = ["Random"]
uuid = "9fa8497b-333b-5362-9e8d-4d0656e87820"

[[Glob]]
git-tree-sha1 = "4df9f7e06108728ebf00a0a11edee4b29a482bb2"
uuid = "c27321d9-0574-5035-807b-f59d2c89b15c"
version = "1.3.0"

[[HTTP]]
deps = ["Base64", "Dates", "IniFile", "Logging", "MbedTLS", "NetworkOptions", "Sockets", "URIs"]
git-tree-sha1 = "14eece7a3308b4d8be910e265c724a6ba51a9798"
uuid = "cd3eb016-35fb-5094-929b-558a96fad6f3"
version = "0.9.16"

[[Hyperscript]]
deps = ["Test"]
git-tree-sha1 = "8d511d5b81240fc8e6802386302675bdf47737b9"
uuid = "47d2ed2b-36de-50cf-bf87-49c2cf4b8b91"
version = "0.0.4"

[[HypertextLiteral]]
git-tree-sha1 = "5efcf53d798efede8fee5b2c8b09284be359bf24"
uuid = "ac1192a8-f4b3-4bfe-ba22-af5b92cd3ab2"
version = "0.9.2"

[[IOCapture]]
deps = ["Logging", "Random"]
git-tree-sha1 = "f7be53659ab06ddc986428d3a9dcc95f6fa6705a"
uuid = "b5f81e59-6552-4d32-b1f0-c071b021bf89"
version = "0.2.2"

[[Indexing]]
git-tree-sha1 = "ce1566720fd6b19ff3411404d4b977acd4814f9f"
uuid = "313cdc1a-70c2-5d6a-ae34-0150d3930a38"
version = "1.1.1"

[[IniFile]]
deps = ["Test"]
git-tree-sha1 = "098e4d2c533924c921f9f9847274f2ad89e018b8"
uuid = "83e8ac13-25f8-5344-8a64-a9f2b223428f"
version = "0.5.0"

[[InlineStrings]]
deps = ["Parsers"]
git-tree-sha1 = "19cb49649f8c41de7fea32d089d37de917b553da"
uuid = "842dd82b-1e85-43dc-bf29-5d0ee9dffc48"
version = "1.0.1"

[[InteractiveUtils]]
deps = ["Markdown"]
uuid = "b77e0a4c-d291-57a0-90e8-8db25a27a240"

[[InverseFunctions]]
deps = ["Test"]
git-tree-sha1 = "f0c6489b12d28fb4c2103073ec7452f3423bd308"
uuid = "3587e190-3f89-42d0-90ee-14403ec27112"
version = "0.1.1"

[[InvertedIndices]]
git-tree-sha1 = "bee5f1ef5bf65df56bdd2e40447590b272a5471f"
uuid = "41ab1584-1d38-5bbf-9106-f11c6c58b48f"
version = "1.1.0"

[[IrrationalConstants]]
git-tree-sha1 = "7fd44fd4ff43fc60815f8e764c0f352b83c49151"
uuid = "92d709cd-6900-40b7-9082-c6be49f344b6"
version = "0.1.1"

[[IteratorInterfaceExtensions]]
git-tree-sha1 = "a3f24677c21f5bbe9d2a714f95dcd58337fb2856"
uuid = "82899510-4779-5014-852e-03e436cf321d"
version = "1.0.0"

[[JSON]]
deps = ["Dates", "Mmap", "Parsers", "Unicode"]
git-tree-sha1 = "8076680b162ada2a031f707ac7b4953e30667a37"
uuid = "682c06a0-de6a-54ab-a142-c8b1cf79cde6"
version = "0.21.2"

[[Kanones]]
deps = ["AtticGreek", "CSV", "CitableBase", "CitableCorpus", "CitableObject", "CitableParserBuilder", "CitableText", "DelimitedFiles", "DocStringExtensions", "Documenter", "Glob", "HTTP", "Orthography", "PolytonicGreek", "Test", "Unicode"]
git-tree-sha1 = "58034121141c7c8e9f6f7f7b97fa952b8de66eac"
uuid = "107500f9-53d4-4696-8485-0747242ad8bc"
version = "0.1.8"

[[LibCURL]]
deps = ["LibCURL_jll", "MozillaCACerts_jll"]
uuid = "b27032c2-a3e7-50c8-80cd-2d36dbcbfd21"

[[LibCURL_jll]]
deps = ["Artifacts", "LibSSH2_jll", "Libdl", "MbedTLS_jll", "Zlib_jll", "nghttp2_jll"]
uuid = "deac9b47-8bc7-5906-a0fe-35ac56dc84c0"

[[LibGit2]]
deps = ["Base64", "NetworkOptions", "Printf", "SHA"]
uuid = "76f85450-5226-5b5a-8eaa-529ad045b433"

[[LibSSH2_jll]]
deps = ["Artifacts", "Libdl", "MbedTLS_jll"]
uuid = "29816b5a-b9ab-546f-933c-edad1886dfa8"

[[Libdl]]
uuid = "8f399da3-3557-5675-b5ff-fb832c97cbdb"

[[LinearAlgebra]]
deps = ["Libdl"]
uuid = "37e2e46d-f89d-539d-b4ee-838fcccc9c8e"

[[LogExpFunctions]]
deps = ["ChainRulesCore", "DocStringExtensions", "InverseFunctions", "IrrationalConstants", "LinearAlgebra"]
git-tree-sha1 = "6193c3815f13ba1b78a51ce391db8be016ae9214"
uuid = "2ab3a3ac-af41-5b50-aa03-7779005ae688"
version = "0.3.4"

[[Logging]]
uuid = "56ddb016-857b-54e1-b83d-db4d58db5568"

[[Markdown]]
deps = ["Base64"]
uuid = "d6f4376e-aef5-505a-96c1-9c027394607a"

[[MbedTLS]]
deps = ["Dates", "MbedTLS_jll", "Random", "Sockets"]
git-tree-sha1 = "1c38e51c3d08ef2278062ebceade0e46cefc96fe"
uuid = "739be429-bea8-5141-9913-cc70e7f3736d"
version = "1.0.3"

[[MbedTLS_jll]]
deps = ["Artifacts", "Libdl"]
uuid = "c8ffd9c3-330d-5841-b78e-0817d7145fa1"

[[Missings]]
deps = ["DataAPI"]
git-tree-sha1 = "bf210ce90b6c9eed32d25dbcae1ebc565df2687f"
uuid = "e1d29d7a-bbdc-5cf2-9ac0-f12de2c33e28"
version = "1.0.2"

[[Mmap]]
uuid = "a63ad114-7e13-5084-954f-fe012c677804"

[[MozillaCACerts_jll]]
uuid = "14a3606d-f60d-562e-9121-12d972cd8159"

[[NetworkOptions]]
uuid = "ca575930-c2e3-43a9-ace4-1e988b2c1908"

[[OrderedCollections]]
git-tree-sha1 = "85f8e6578bf1f9ee0d11e7bb1b1456435479d47c"
uuid = "bac558e1-5e72-5ebc-8fee-abe8a469f55d"
version = "1.4.1"

[[Orthography]]
deps = ["CitableBase", "CitableCorpus", "CitableText", "DocStringExtensions", "Documenter", "OrderedCollections", "StatsBase", "Test", "TypedTables", "Unicode"]
git-tree-sha1 = "3cd7d46f0bae8dc2915f63d6a68073e65477824b"
uuid = "0b4c9448-09b0-4e78-95ea-3eb3328be36d"
version = "0.15.0"

[[Parsers]]
deps = ["Dates"]
git-tree-sha1 = "f19e978f81eca5fd7620650d7dbea58f825802ee"
uuid = "69de0a69-1ddd-5017-9359-2bf0b02dc9f0"
version = "2.1.0"

[[Pkg]]
deps = ["Artifacts", "Dates", "Downloads", "LibGit2", "Libdl", "Logging", "Markdown", "Printf", "REPL", "Random", "SHA", "Serialization", "TOML", "Tar", "UUIDs", "p7zip_jll"]
uuid = "44cfe95a-1eb2-52ea-b672-e2afdf69b78f"

[[PlutoUI]]
deps = ["Base64", "Dates", "Hyperscript", "HypertextLiteral", "IOCapture", "InteractiveUtils", "JSON", "Logging", "Markdown", "Random", "Reexport", "UUIDs"]
git-tree-sha1 = "4c8a7d080daca18545c56f1cac28710c362478f3"
uuid = "7f904dfe-b85e-4ff6-b463-dae2292396a8"
version = "0.7.16"

[[PolytonicGreek]]
deps = ["DocStringExtensions", "Documenter", "Orthography", "Test", "Unicode"]
git-tree-sha1 = "603debfd221e9ae14ea0fbad4c0f3aaf3062d326"
uuid = "72b824a7-2b4a-40fa-944c-ac4f345dc63a"
version = "0.13.2"

[[PooledArrays]]
deps = ["DataAPI", "Future"]
git-tree-sha1 = "a193d6ad9c45ada72c14b731a318bedd3c2f00cf"
uuid = "2dfb63ee-cc39-5dd5-95bd-886bf059d720"
version = "1.3.0"

[[PrettyTables]]
deps = ["Crayons", "Formatting", "Markdown", "Reexport", "Tables"]
git-tree-sha1 = "d940010be611ee9d67064fe559edbb305f8cc0eb"
uuid = "08abe8d2-0d0c-5749-adfa-8a2ac140af0d"
version = "1.2.3"

[[Printf]]
deps = ["Unicode"]
uuid = "de0858da-6303-5e67-8744-51eddeeeb8d7"

[[REPL]]
deps = ["InteractiveUtils", "Markdown", "Sockets", "Unicode"]
uuid = "3fa0cd96-eef1-5676-8a61-b3b8758bbffb"

[[Random]]
deps = ["Serialization"]
uuid = "9a3f8284-a2c9-5f02-9a11-845980a1fd5c"

[[Reexport]]
git-tree-sha1 = "45e428421666073eab6f2da5c9d310d99bb12f9b"
uuid = "189a3867-3050-52da-a836-e630ba90ab69"
version = "1.2.2"

[[SHA]]
uuid = "ea8e919c-243c-51af-8825-aaa63cd721ce"

[[SentinelArrays]]
deps = ["Dates", "Random"]
git-tree-sha1 = "f45b34656397a1f6e729901dc9ef679610bd12b5"
uuid = "91c51154-3ec4-41a3-a24f-3f23e20d615c"
version = "1.3.8"

[[Serialization]]
uuid = "9e88b42a-f829-5b0c-bbe9-9e923198166b"

[[SharedArrays]]
deps = ["Distributed", "Mmap", "Random", "Serialization"]
uuid = "1a1011a3-84de-559e-8e89-a11a2f7dc383"

[[Sockets]]
uuid = "6462fe0b-24de-5631-8697-dd941f90decc"

[[SortingAlgorithms]]
deps = ["DataStructures"]
git-tree-sha1 = "b3363d7460f7d098ca0912c69b082f75625d7508"
uuid = "a2af1166-a08f-5f64-846c-94a0d3cef48c"
version = "1.0.1"

[[SparseArrays]]
deps = ["LinearAlgebra", "Random"]
uuid = "2f01184e-e22b-5df5-ae63-d93ebab69eaf"

[[SplitApplyCombine]]
deps = ["Dictionaries", "Indexing"]
git-tree-sha1 = "3cdd86a92737fa0c8af19aecb1141e71057dc2db"
uuid = "03a91e81-4c3e-53e1-a0a4-9c0c8f19dd66"
version = "1.1.4"

[[Statistics]]
deps = ["LinearAlgebra", "SparseArrays"]
uuid = "10745b16-79ce-11e8-11f9-7d13ad32a3b2"

[[StatsAPI]]
git-tree-sha1 = "1958272568dc176a1d881acb797beb909c785510"
uuid = "82ae8749-77ed-4fe6-ae5f-f523153014b0"
version = "1.0.0"

[[StatsBase]]
deps = ["DataAPI", "DataStructures", "LinearAlgebra", "LogExpFunctions", "Missings", "Printf", "Random", "SortingAlgorithms", "SparseArrays", "Statistics", "StatsAPI"]
git-tree-sha1 = "eb35dcc66558b2dda84079b9a1be17557d32091a"
uuid = "2913bbd2-ae8a-5f71-8c99-4fb6c76f3a91"
version = "0.33.12"

[[TOML]]
deps = ["Dates"]
uuid = "fa267f1f-6049-4f14-aa54-33bafae1ed76"

[[TableTraits]]
deps = ["IteratorInterfaceExtensions"]
git-tree-sha1 = "c06b2f539df1c6efa794486abfb6ed2022561a39"
uuid = "3783bdb8-4a98-5b6b-af9a-565f29a5fe9c"
version = "1.0.1"

[[Tables]]
deps = ["DataAPI", "DataValueInterfaces", "IteratorInterfaceExtensions", "LinearAlgebra", "TableTraits", "Test"]
git-tree-sha1 = "fed34d0e71b91734bf0a7e10eb1bb05296ddbcd0"
uuid = "bd369af6-aec1-5ad0-b16a-f7cc5008161c"
version = "1.6.0"

[[Tar]]
deps = ["ArgTools", "SHA"]
uuid = "a4e569a6-e804-4fa4-b0f3-eef7a1d5b13e"

[[Test]]
deps = ["InteractiveUtils", "Logging", "Random", "Serialization"]
uuid = "8dfed614-e22c-5e08-85e1-65c5234f0b40"

[[TranscodingStreams]]
deps = ["Random", "Test"]
git-tree-sha1 = "216b95ea110b5972db65aa90f88d8d89dcb8851c"
uuid = "3bb67fe8-82b1-5028-8e26-92a6c54297fa"
version = "0.9.6"

[[TypedTables]]
deps = ["Adapt", "Dictionaries", "Indexing", "SplitApplyCombine", "Tables", "Unicode"]
git-tree-sha1 = "f91a10d0132310a31bc4f8d0d29ce052536bd7d7"
uuid = "9d95f2ec-7b3d-5a63-8d20-e2491e220bb9"
version = "1.4.0"

[[URIs]]
git-tree-sha1 = "97bbe755a53fe859669cd907f2d96aee8d2c1355"
uuid = "5c2747f8-b7ea-4ff2-ba2e-563bfd36b1d4"
version = "1.3.0"

[[UUIDs]]
deps = ["Random", "SHA"]
uuid = "cf7118a7-6976-5b1a-9a39-7adc72f591a4"

[[Unicode]]
uuid = "4ec0a83e-493e-50e2-b9ac-8f72acf5a8f5"

[[WeakRefStrings]]
deps = ["DataAPI", "InlineStrings", "Parsers"]
git-tree-sha1 = "c69f9da3ff2f4f02e811c3323c22e5dfcb584cfa"
uuid = "ea10d353-3f73-51f8-a26c-33c1cb351aa5"
version = "1.4.1"

[[Zlib_jll]]
deps = ["Libdl"]
uuid = "83775a58-1f1d-513f-b197-d71354ab007a"

[[nghttp2_jll]]
deps = ["Artifacts", "Libdl"]
uuid = "8e850ede-7688-5339-a07c-302acd2aaf8d"

[[p7zip_jll]]
deps = ["Artifacts", "Libdl"]
uuid = "3f19e933-33d8-53b3-aaab-bd5110c3b7a0"
"""

# ╔═╡ Cell order:
# ╟─a6bfa46c-d7d6-4ef8-b9b3-4b971c877b60
# ╟─8568c8fc-35bf-11ec-345e-25f2feeb26f0
# ╟─bcacc767-6709-4851-92ad-8a9823ca26fc
# ╟─e9b9a397-7cf8-410a-a4c0-5e2d8ae46b15
# ╟─8ed31067-b737-4558-a056-1d14f8c19eeb
# ╟─50042fea-d0a4-49b3-a6fb-4e6a15afd03d
# ╟─457d2fad-5524-4a3a-80ac-55368c198d8d
# ╟─c0c0954c-9b08-46f0-8dda-bd6771a37d69
# ╟─768a5e5d-3817-413f-a472-8e9dcecb67f5
# ╟─95971c5c-e0fc-4b0e-bf55-8701d2bfe1b9
# ╟─aa50f476-17aa-45e5-8379-cc2a58c556ce
# ╟─c7fd9f27-5c75-4765-a8ff-8d967ba358d4
# ╟─51a3fb01-aa28-4b9c-9814-9d3efe304cd8
# ╟─0c420aef-135d-4743-9703-bdf61e9e7d30
# ╟─3a11e0c0-feed-4e71-8991-8aef9ccaf481
# ╟─646ee9a2-c6fc-4a42-aba2-7a407036a5b9
# ╟─3ed4f761-8f12-46a2-8d87-864b029e334d
# ╠═c2cfc47e-8e65-48a3-86f7-ac820e86116c
# ╟─00000000-0000-0000-0000-000000000001
# ╟─00000000-0000-0000-0000-000000000002
