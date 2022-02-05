### A Pluto.jl notebook ###
# v0.17.5

using Markdown
using InteractiveUtils

# This Pluto notebook uses @bind for interactivity. When running this notebook outside of Pluto, the following 'mock version' of @bind gives bound variables a default value (instead of an error).
macro bind(def, element)
    quote
        local iv = try Base.loaded_modules[Base.PkgId(Base.UUID("6e696c72-6542-2067-7265-42206c756150"), "AbstractPlutoDingetjes")].Bonds.initial_value catch; b -> missing; end
        local el = $(esc(element))
        global $(esc(def)) = Core.applicable(Base.get, el) ? Base.get(el) : iv(el)
        el
    end
end

# ╔═╡ 424e46e1-4d83-4145-990f-d229cb71fa95
begin
	#using HmtFacsimileBuilders
	using CitableBase
	using CitableText
	using CitableCorpus
	using Unicode
	using PlutoUI
end

# ╔═╡ 530c3958-1cba-420e-92c0-b38dbdbdec14
md"""
*Notebook version*:  **1.0.0**
"""

# ╔═╡ 19fb1827-4e34-4e13-b41a-45be89943277
md"""
# HMT project: search corpus by alphabetic string
"""

# ╔═╡ e869534c-7091-498c-abb6-9479dcbd7c15
md"""*Search for*: $(@bind query TextField())"""

# ╔═╡ 74066309-96f6-470d-ad93-1bd12d0a4b84
html"""
<br/><br/><br/>
"""

# ╔═╡ 45efb9a0-64a6-4d63-a840-98919f4a5a67
md"""---

> Data
"""

# ╔═╡ 7fde224c-85ec-11ec-302c-8948181600dd
release_candidate_url = "https://raw.githubusercontent.com/homermultitext/hmt-archive/master/release-candidates/hmt-current.cex"

# ╔═╡ b248f6bc-d13a-4388-bdc8-6fbc89dc0192
hmt_catalog = fromcex(release_candidate_url, TextCatalogCollection, UrlReader)


# ╔═╡ fe772369-107c-407b-b825-baaeea6e997e
# Load all HMT texts from URL:
hmt_corpus = fromcex(release_candidate_url, CitableTextCorpus, UrlReader)

# ╔═╡ 4e586e90-3d81-4a2f-a720-0d1969674eff
# Select diplomatic edition of all texts:
normalizededition = filter(psg -> endswith(workcomponent(psg.urn), "normalized"), hmt_corpus.passages)


# ╔═╡ cc739038-5dc1-4466-a9f4-94269ce5318a
md"> User selection of content"

# ╔═╡ 621643c2-cf17-4c62-bf27-7a8103b6f325
md"> UI"

# ╔═╡ b63800b8-cc35-4af7-9635-ff52120edd79
msmenu = ["va" => "Venetus A only", "all" => "All manuscripts",]

# ╔═╡ be2c98b7-aa8a-480b-9827-18d30f811bfa
md"""*Manuscripts to include*: $(@bind ms Select(msmenu, default = "all"))"""

# ╔═╡ b3615d84-a1b0-4773-b981-82b11c5b669f
# Filter hmt_normalized for selected setting for mss 
function filtermss()
	if ms == "all"
		normalizededition
		
	elseif ms == "va"
		msascholia = filter(p -> startswith(workcomponent(p.urn), "tlg5026.msA"), normalizededition)
		msailiad = filter(p -> startswith(workcomponent(p.urn), "tlg0012.tlg001.msA"), normalizededition)
		vcat(msascholia, msailiad)

		
	end
end

# ╔═╡ 2191664d-f346-4c87-acd4-73c88e3d8579
textmenu = [
"iliad" => "Iliad",
"scholia" => "scholia",
"all" => "All texts"
]

# ╔═╡ b7716ef3-02f3-453d-b97c-c42b988fcd42
md"""*Texts to include*: $(@bind txt Select(textmenu, default = "all"))"""

# ╔═╡ 766be4f8-74f5-4e8e-a7d0-ff608b1f137f
function filtertext(psgs)
	if txt == "all"
		psgs
	elseif txt == "iliad"
		iliadurn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
		filter(p -> urncontains(iliadurn, p.urn), psgs)
	elseif txt == "scholia"
		scholiaurn = CtsUrn("urn:cts:greekLit:tlg5026:")
		filter(p -> urncontains(scholiaurn, p.urn), psgs)
	end
end

# ╔═╡ 117aabd7-1258-4c8b-ba0a-8c5c152b8343
function selection()
	mss = filtermss()
	filtertext(mss)
end

# ╔═╡ c216d97b-0149-42d3-940b-9da96e5b6796
selectedpassages = selection()

# ╔═╡ 6d5a3b9c-09a8-44e9-9c4f-b6ebf8eace74
md"""Text passages to search: $(length(selectedpassages))"""

# ╔═╡ 5a580c58-7e05-4616-b1c3-b7a5859fda01
# Generate text with accents and breathings stripped:
alphabeticstrings = map(psg -> Unicode.normalize(psg.text, stripmark=true) |> lowercase, selectedpassages)

# ╔═╡ 36673851-5e71-424f-bf40-9a9728dc7546
# Sanity check:
length(alphabeticstrings) == length(selectedpassages)

# ╔═╡ 829da435-58ca-4c13-a027-48cd0f041a34
md"""> Searching"""

# ╔═╡ 6f192fc5-1801-4546-ad34-d4e2eaab2ca1
minlength = 3

# ╔═╡ 9e8143e0-5817-4ca8-a1f4-6d2738518f3c
md"""
- Select manuscripts and texts to include
- Enter an alphabetic string (no accents or breathings) in Unicode Greek to search for
- Minimum length of query string is **$(minlength) characters** .
"""

# ╔═╡ 329827b7-51dc-4285-a959-cd6b9ad9fc4f
# Search on alphabetic corpus to find array indices of matches,
# then collect corresponding entry in array of diplomatic edition
results = begin
	if length(query) > 2
		indices = findall(contains(lowercase(query)), alphabeticstrings)
		rslts = []
		for i in indices
			push!(rslts, selectedpassages[i])
		end
		rslts
	else
		[]
	end
end

# ╔═╡ 8bc3b6d7-0f76-42bb-8a42-b43d106fafd9
# Format a `CitablePassage` in  markdown
function formatpassage(psg)
	"1. **" * passagecomponent(psg.urn) * "**: " * psg.text
end

# ╔═╡ d0d06fe1-4d2e-40ca-ba5a-10bafc03f8f0
function isiliad(u::CtsUrn)
	urncontains(CtsUrn("urn:cts:greekLit:tlg0012.tlg001:"), u)
end

# ╔═╡ 02c02960-23fa-4f82-9630-774fb82cbe84
function markdowntitle(catentry::CatalogedText)
	if isiliad(urn(catentry))
		textgroup(catentry) * ", *" * work(catentry) * "* (" * version(catentry) * ")"
	else
		textgroup(catentry) * ", *" * work(catentry) * "*"
	end
end

# ╔═╡ 465fd66d-452c-470e-b6c7-558f769e98a6
function titleforurn(u::CtsUrn, catalog::TextCatalogCollection)
	catalogurn = isiliad(u) ? dropexemplar(u) : dropversion(u)
	
	catentries = urncontains(catalogurn, catalog).entries
	if length(catentries) == 1
		markdowntitle(catentries[1])
	else
		"FAILED TO FIND $(u)  in catalog"
	end
end

# ╔═╡ 869d2ad6-120f-4038-9a63-113b84a43f65
begin
	if length(query) < 3
		md""
		
	else
		hdr = length(results) == 1 ? "1 *match for* " :  "$(length(results)) *matches for* "

		
		#psglist = map(p -> formatpassage(p), results)
		#mdlist = join(psglist, "\n\n")
		
		currentwork = nothing
		mdlines = ["## " * hdr * " " * query,
		""]
		for psg in results
			newwork = droppassage(psg.urn)
			if newwork != currentwork
				title = titleforurn(newwork, hmt_catalog)
				push!(mdlines, "#### $(title)")
				global currentwork = newwork
			end
			push!(mdlines, formatpassage(psg))
		end
		mdlist = join(mdlines, "\n\n")

		mdsrc = """


		$(mdlist)
		

		
		"""
		Markdown.parse(mdsrc)
	end
end

# ╔═╡ 00000000-0000-0000-0000-000000000001
PLUTO_PROJECT_TOML_CONTENTS = """
[deps]
CitableBase = "d6f014bd-995c-41bd-9893-703339864534"
CitableCorpus = "cf5ac11a-93ef-4a1a-97a3-f6af101603b5"
CitableText = "41e66566-473b-49d4-85b7-da83b66615d8"
PlutoUI = "7f904dfe-b85e-4ff6-b463-dae2292396a8"
Unicode = "4ec0a83e-493e-50e2-b9ac-8f72acf5a8f5"

[compat]
CitableBase = "~10.0.0"
CitableCorpus = "~0.12.3"
CitableText = "~0.14.3"
PlutoUI = "~0.7.34"
"""

# ╔═╡ 00000000-0000-0000-0000-000000000002
PLUTO_MANIFEST_TOML_CONTENTS = """
# This file is machine-generated - editing it directly is not advised

julia_version = "1.7.0"
manifest_format = "2.0"

[[deps.ANSIColoredPrinters]]
git-tree-sha1 = "574baf8110975760d391c710b6341da1afa48d8c"
uuid = "a4c015fc-c6ff-483c-b24f-f7ea428134e9"
version = "0.0.1"

[[deps.AbstractPlutoDingetjes]]
deps = ["Pkg"]
git-tree-sha1 = "8eaf9f1b4921132a4cff3f36a1d9ba923b14a481"
uuid = "6e696c72-6542-2067-7265-42206c756150"
version = "1.1.4"

[[deps.ArgTools]]
uuid = "0dad84c5-d112-42e6-8d28-ef12dabb789f"

[[deps.Artifacts]]
uuid = "56f22d72-fd6d-98f1-02f0-08ddc0907c33"

[[deps.Base64]]
uuid = "2a0f44e3-6c83-55bd-87e4-b1978d98bd5f"

[[deps.CSV]]
deps = ["CodecZlib", "Dates", "FilePathsBase", "InlineStrings", "Mmap", "Parsers", "PooledArrays", "SentinelArrays", "Tables", "Unicode", "WeakRefStrings"]
git-tree-sha1 = "49f14b6c56a2da47608fe30aed711b5882264d7a"
uuid = "336ed68f-0bac-5ca0-87d4-7b16caf5d00b"
version = "0.9.11"

[[deps.CitableBase]]
deps = ["DocStringExtensions", "Documenter", "HTTP", "Test"]
git-tree-sha1 = "68eadd300ed816ad77cd8a28329a30105adca03d"
uuid = "d6f014bd-995c-41bd-9893-703339864534"
version = "10.0.0"

[[deps.CitableCorpus]]
deps = ["CitableBase", "CitableText", "CiteEXchange", "DocStringExtensions", "Documenter", "HTTP", "Tables", "Test"]
git-tree-sha1 = "cc45bf7a370795a8f6559bacaee00e0fe124147f"
uuid = "cf5ac11a-93ef-4a1a-97a3-f6af101603b5"
version = "0.12.3"

[[deps.CitableText]]
deps = ["CitableBase", "DocStringExtensions", "Documenter", "Test"]
git-tree-sha1 = "6bcd8a1c3ff7d77643f84215472b756f839cdc06"
uuid = "41e66566-473b-49d4-85b7-da83b66615d8"
version = "0.14.3"

[[deps.CiteEXchange]]
deps = ["CSV", "CitableBase", "DocStringExtensions", "Documenter", "HTTP", "Test"]
git-tree-sha1 = "7ce89a2380e83a91b46ef09c495c3291871b82d5"
uuid = "e2e9ead3-1b6c-4e96-b95f-43e6ab899178"
version = "0.9.3"

[[deps.CodecZlib]]
deps = ["TranscodingStreams", "Zlib_jll"]
git-tree-sha1 = "ded953804d019afa9a3f98981d99b33e3db7b6da"
uuid = "944b1d66-785c-5afd-91f1-9de20f533193"
version = "0.7.0"

[[deps.ColorTypes]]
deps = ["FixedPointNumbers", "Random"]
git-tree-sha1 = "024fe24d83e4a5bf5fc80501a314ce0d1aa35597"
uuid = "3da002f7-5984-5a60-b8a6-cbb66c0b333f"
version = "0.11.0"

[[deps.Compat]]
deps = ["Base64", "Dates", "DelimitedFiles", "Distributed", "InteractiveUtils", "LibGit2", "Libdl", "LinearAlgebra", "Markdown", "Mmap", "Pkg", "Printf", "REPL", "Random", "SHA", "Serialization", "SharedArrays", "Sockets", "SparseArrays", "Statistics", "Test", "UUIDs", "Unicode"]
git-tree-sha1 = "44c37b4636bc54afac5c574d2d02b625349d6582"
uuid = "34da2185-b29b-5c13-b0c7-acf172513d20"
version = "3.41.0"

[[deps.CompilerSupportLibraries_jll]]
deps = ["Artifacts", "Libdl"]
uuid = "e66e0078-7015-5450-92f7-15fbd957f2ae"

[[deps.DataAPI]]
git-tree-sha1 = "cc70b17275652eb47bc9e5f81635981f13cea5c8"
uuid = "9a962f9c-6df0-11e9-0e5d-c546b8b5ee8a"
version = "1.9.0"

[[deps.DataValueInterfaces]]
git-tree-sha1 = "bfc1187b79289637fa0ef6d4436ebdfe6905cbd6"
uuid = "e2d170a0-9d28-54be-80f0-106bbe20a464"
version = "1.0.0"

[[deps.Dates]]
deps = ["Printf"]
uuid = "ade2ca70-3891-5945-98fb-dc099432e06a"

[[deps.DelimitedFiles]]
deps = ["Mmap"]
uuid = "8bb1440f-4735-579b-a4ab-409b98df4dab"

[[deps.Distributed]]
deps = ["Random", "Serialization", "Sockets"]
uuid = "8ba89e20-285c-5b6f-9357-94700520ee1b"

[[deps.DocStringExtensions]]
deps = ["LibGit2"]
git-tree-sha1 = "b19534d1895d702889b219c382a6e18010797f0b"
uuid = "ffbed154-4ef7-542d-bbb7-c09d3a79fcae"
version = "0.8.6"

[[deps.Documenter]]
deps = ["ANSIColoredPrinters", "Base64", "Dates", "DocStringExtensions", "IOCapture", "InteractiveUtils", "JSON", "LibGit2", "Logging", "Markdown", "REPL", "Test", "Unicode"]
git-tree-sha1 = "75c6cf9d99e0efc79b724f5566726ad3ad010a01"
uuid = "e30172f5-a6a5-5a46-863b-614d45cd2de4"
version = "0.27.12"

[[deps.Downloads]]
deps = ["ArgTools", "LibCURL", "NetworkOptions"]
uuid = "f43a241f-c20a-4ad4-852c-f6b1247861c6"

[[deps.FilePathsBase]]
deps = ["Compat", "Dates", "Mmap", "Printf", "Test", "UUIDs"]
git-tree-sha1 = "04d13bfa8ef11720c24e4d840c0033d145537df7"
uuid = "48062228-2e41-5def-b9a4-89aafe57970f"
version = "0.9.17"

[[deps.FixedPointNumbers]]
deps = ["Statistics"]
git-tree-sha1 = "335bfdceacc84c5cdf16aadc768aa5ddfc5383cc"
uuid = "53c48c17-4a7d-5ca2-90c5-79b7896eea93"
version = "0.8.4"

[[deps.Future]]
deps = ["Random"]
uuid = "9fa8497b-333b-5362-9e8d-4d0656e87820"

[[deps.HTTP]]
deps = ["Base64", "Dates", "IniFile", "Logging", "MbedTLS", "NetworkOptions", "Sockets", "URIs"]
git-tree-sha1 = "0fa77022fe4b511826b39c894c90daf5fce3334a"
uuid = "cd3eb016-35fb-5094-929b-558a96fad6f3"
version = "0.9.17"

[[deps.Hyperscript]]
deps = ["Test"]
git-tree-sha1 = "8d511d5b81240fc8e6802386302675bdf47737b9"
uuid = "47d2ed2b-36de-50cf-bf87-49c2cf4b8b91"
version = "0.0.4"

[[deps.HypertextLiteral]]
git-tree-sha1 = "2b078b5a615c6c0396c77810d92ee8c6f470d238"
uuid = "ac1192a8-f4b3-4bfe-ba22-af5b92cd3ab2"
version = "0.9.3"

[[deps.IOCapture]]
deps = ["Logging", "Random"]
git-tree-sha1 = "f7be53659ab06ddc986428d3a9dcc95f6fa6705a"
uuid = "b5f81e59-6552-4d32-b1f0-c071b021bf89"
version = "0.2.2"

[[deps.IniFile]]
deps = ["Test"]
git-tree-sha1 = "098e4d2c533924c921f9f9847274f2ad89e018b8"
uuid = "83e8ac13-25f8-5344-8a64-a9f2b223428f"
version = "0.5.0"

[[deps.InlineStrings]]
deps = ["Parsers"]
git-tree-sha1 = "61feba885fac3a407465726d0c330b3055df897f"
uuid = "842dd82b-1e85-43dc-bf29-5d0ee9dffc48"
version = "1.1.2"

[[deps.InteractiveUtils]]
deps = ["Markdown"]
uuid = "b77e0a4c-d291-57a0-90e8-8db25a27a240"

[[deps.IteratorInterfaceExtensions]]
git-tree-sha1 = "a3f24677c21f5bbe9d2a714f95dcd58337fb2856"
uuid = "82899510-4779-5014-852e-03e436cf321d"
version = "1.0.0"

[[deps.JSON]]
deps = ["Dates", "Mmap", "Parsers", "Unicode"]
git-tree-sha1 = "8076680b162ada2a031f707ac7b4953e30667a37"
uuid = "682c06a0-de6a-54ab-a142-c8b1cf79cde6"
version = "0.21.2"

[[deps.LibCURL]]
deps = ["LibCURL_jll", "MozillaCACerts_jll"]
uuid = "b27032c2-a3e7-50c8-80cd-2d36dbcbfd21"

[[deps.LibCURL_jll]]
deps = ["Artifacts", "LibSSH2_jll", "Libdl", "MbedTLS_jll", "Zlib_jll", "nghttp2_jll"]
uuid = "deac9b47-8bc7-5906-a0fe-35ac56dc84c0"

[[deps.LibGit2]]
deps = ["Base64", "NetworkOptions", "Printf", "SHA"]
uuid = "76f85450-5226-5b5a-8eaa-529ad045b433"

[[deps.LibSSH2_jll]]
deps = ["Artifacts", "Libdl", "MbedTLS_jll"]
uuid = "29816b5a-b9ab-546f-933c-edad1886dfa8"

[[deps.Libdl]]
uuid = "8f399da3-3557-5675-b5ff-fb832c97cbdb"

[[deps.LinearAlgebra]]
deps = ["Libdl", "libblastrampoline_jll"]
uuid = "37e2e46d-f89d-539d-b4ee-838fcccc9c8e"

[[deps.Logging]]
uuid = "56ddb016-857b-54e1-b83d-db4d58db5568"

[[deps.Markdown]]
deps = ["Base64"]
uuid = "d6f4376e-aef5-505a-96c1-9c027394607a"

[[deps.MbedTLS]]
deps = ["Dates", "MbedTLS_jll", "Random", "Sockets"]
git-tree-sha1 = "1c38e51c3d08ef2278062ebceade0e46cefc96fe"
uuid = "739be429-bea8-5141-9913-cc70e7f3736d"
version = "1.0.3"

[[deps.MbedTLS_jll]]
deps = ["Artifacts", "Libdl"]
uuid = "c8ffd9c3-330d-5841-b78e-0817d7145fa1"

[[deps.Mmap]]
uuid = "a63ad114-7e13-5084-954f-fe012c677804"

[[deps.MozillaCACerts_jll]]
uuid = "14a3606d-f60d-562e-9121-12d972cd8159"

[[deps.NetworkOptions]]
uuid = "ca575930-c2e3-43a9-ace4-1e988b2c1908"

[[deps.OpenBLAS_jll]]
deps = ["Artifacts", "CompilerSupportLibraries_jll", "Libdl"]
uuid = "4536629a-c528-5b80-bd46-f80d51c5b363"

[[deps.Parsers]]
deps = ["Dates"]
git-tree-sha1 = "0b5cfbb704034b5b4c1869e36634438a047df065"
uuid = "69de0a69-1ddd-5017-9359-2bf0b02dc9f0"
version = "2.2.1"

[[deps.Pkg]]
deps = ["Artifacts", "Dates", "Downloads", "LibGit2", "Libdl", "Logging", "Markdown", "Printf", "REPL", "Random", "SHA", "Serialization", "TOML", "Tar", "UUIDs", "p7zip_jll"]
uuid = "44cfe95a-1eb2-52ea-b672-e2afdf69b78f"

[[deps.PlutoUI]]
deps = ["AbstractPlutoDingetjes", "Base64", "ColorTypes", "Dates", "Hyperscript", "HypertextLiteral", "IOCapture", "InteractiveUtils", "JSON", "Logging", "Markdown", "Random", "Reexport", "UUIDs"]
git-tree-sha1 = "8979e9802b4ac3d58c503a20f2824ad67f9074dd"
uuid = "7f904dfe-b85e-4ff6-b463-dae2292396a8"
version = "0.7.34"

[[deps.PooledArrays]]
deps = ["DataAPI", "Future"]
git-tree-sha1 = "db3a23166af8aebf4db5ef87ac5b00d36eb771e2"
uuid = "2dfb63ee-cc39-5dd5-95bd-886bf059d720"
version = "1.4.0"

[[deps.Printf]]
deps = ["Unicode"]
uuid = "de0858da-6303-5e67-8744-51eddeeeb8d7"

[[deps.REPL]]
deps = ["InteractiveUtils", "Markdown", "Sockets", "Unicode"]
uuid = "3fa0cd96-eef1-5676-8a61-b3b8758bbffb"

[[deps.Random]]
deps = ["SHA", "Serialization"]
uuid = "9a3f8284-a2c9-5f02-9a11-845980a1fd5c"

[[deps.Reexport]]
git-tree-sha1 = "45e428421666073eab6f2da5c9d310d99bb12f9b"
uuid = "189a3867-3050-52da-a836-e630ba90ab69"
version = "1.2.2"

[[deps.SHA]]
uuid = "ea8e919c-243c-51af-8825-aaa63cd721ce"

[[deps.SentinelArrays]]
deps = ["Dates", "Random"]
git-tree-sha1 = "15dfe6b103c2a993be24404124b8791a09460983"
uuid = "91c51154-3ec4-41a3-a24f-3f23e20d615c"
version = "1.3.11"

[[deps.Serialization]]
uuid = "9e88b42a-f829-5b0c-bbe9-9e923198166b"

[[deps.SharedArrays]]
deps = ["Distributed", "Mmap", "Random", "Serialization"]
uuid = "1a1011a3-84de-559e-8e89-a11a2f7dc383"

[[deps.Sockets]]
uuid = "6462fe0b-24de-5631-8697-dd941f90decc"

[[deps.SparseArrays]]
deps = ["LinearAlgebra", "Random"]
uuid = "2f01184e-e22b-5df5-ae63-d93ebab69eaf"

[[deps.Statistics]]
deps = ["LinearAlgebra", "SparseArrays"]
uuid = "10745b16-79ce-11e8-11f9-7d13ad32a3b2"

[[deps.TOML]]
deps = ["Dates"]
uuid = "fa267f1f-6049-4f14-aa54-33bafae1ed76"

[[deps.TableTraits]]
deps = ["IteratorInterfaceExtensions"]
git-tree-sha1 = "c06b2f539df1c6efa794486abfb6ed2022561a39"
uuid = "3783bdb8-4a98-5b6b-af9a-565f29a5fe9c"
version = "1.0.1"

[[deps.Tables]]
deps = ["DataAPI", "DataValueInterfaces", "IteratorInterfaceExtensions", "LinearAlgebra", "TableTraits", "Test"]
git-tree-sha1 = "bb1064c9a84c52e277f1096cf41434b675cd368b"
uuid = "bd369af6-aec1-5ad0-b16a-f7cc5008161c"
version = "1.6.1"

[[deps.Tar]]
deps = ["ArgTools", "SHA"]
uuid = "a4e569a6-e804-4fa4-b0f3-eef7a1d5b13e"

[[deps.Test]]
deps = ["InteractiveUtils", "Logging", "Random", "Serialization"]
uuid = "8dfed614-e22c-5e08-85e1-65c5234f0b40"

[[deps.TranscodingStreams]]
deps = ["Random", "Test"]
git-tree-sha1 = "216b95ea110b5972db65aa90f88d8d89dcb8851c"
uuid = "3bb67fe8-82b1-5028-8e26-92a6c54297fa"
version = "0.9.6"

[[deps.URIs]]
git-tree-sha1 = "97bbe755a53fe859669cd907f2d96aee8d2c1355"
uuid = "5c2747f8-b7ea-4ff2-ba2e-563bfd36b1d4"
version = "1.3.0"

[[deps.UUIDs]]
deps = ["Random", "SHA"]
uuid = "cf7118a7-6976-5b1a-9a39-7adc72f591a4"

[[deps.Unicode]]
uuid = "4ec0a83e-493e-50e2-b9ac-8f72acf5a8f5"

[[deps.WeakRefStrings]]
deps = ["DataAPI", "InlineStrings", "Parsers"]
git-tree-sha1 = "c69f9da3ff2f4f02e811c3323c22e5dfcb584cfa"
uuid = "ea10d353-3f73-51f8-a26c-33c1cb351aa5"
version = "1.4.1"

[[deps.Zlib_jll]]
deps = ["Libdl"]
uuid = "83775a58-1f1d-513f-b197-d71354ab007a"

[[deps.libblastrampoline_jll]]
deps = ["Artifacts", "Libdl", "OpenBLAS_jll"]
uuid = "8e850b90-86db-534c-a0d3-1478176c7d93"

[[deps.nghttp2_jll]]
deps = ["Artifacts", "Libdl"]
uuid = "8e850ede-7688-5339-a07c-302acd2aaf8d"

[[deps.p7zip_jll]]
deps = ["Artifacts", "Libdl"]
uuid = "3f19e933-33d8-53b3-aaab-bd5110c3b7a0"
"""

# ╔═╡ Cell order:
# ╟─424e46e1-4d83-4145-990f-d229cb71fa95
# ╟─530c3958-1cba-420e-92c0-b38dbdbdec14
# ╟─19fb1827-4e34-4e13-b41a-45be89943277
# ╟─9e8143e0-5817-4ca8-a1f4-6d2738518f3c
# ╟─be2c98b7-aa8a-480b-9827-18d30f811bfa
# ╟─b7716ef3-02f3-453d-b97c-c42b988fcd42
# ╟─6d5a3b9c-09a8-44e9-9c4f-b6ebf8eace74
# ╟─e869534c-7091-498c-abb6-9479dcbd7c15
# ╟─869d2ad6-120f-4038-9a63-113b84a43f65
# ╟─74066309-96f6-470d-ad93-1bd12d0a4b84
# ╟─45efb9a0-64a6-4d63-a840-98919f4a5a67
# ╟─7fde224c-85ec-11ec-302c-8948181600dd
# ╟─b248f6bc-d13a-4388-bdc8-6fbc89dc0192
# ╟─fe772369-107c-407b-b825-baaeea6e997e
# ╟─4e586e90-3d81-4a2f-a720-0d1969674eff
# ╟─5a580c58-7e05-4616-b1c3-b7a5859fda01
# ╟─cc739038-5dc1-4466-a9f4-94269ce5318a
# ╟─c216d97b-0149-42d3-940b-9da96e5b6796
# ╟─117aabd7-1258-4c8b-ba0a-8c5c152b8343
# ╟─766be4f8-74f5-4e8e-a7d0-ff608b1f137f
# ╟─b3615d84-a1b0-4773-b981-82b11c5b669f
# ╠═36673851-5e71-424f-bf40-9a9728dc7546
# ╟─621643c2-cf17-4c62-bf27-7a8103b6f325
# ╟─b63800b8-cc35-4af7-9635-ff52120edd79
# ╟─2191664d-f346-4c87-acd4-73c88e3d8579
# ╟─829da435-58ca-4c13-a027-48cd0f041a34
# ╟─6f192fc5-1801-4546-ad34-d4e2eaab2ca1
# ╟─329827b7-51dc-4285-a959-cd6b9ad9fc4f
# ╟─8bc3b6d7-0f76-42bb-8a42-b43d106fafd9
# ╟─02c02960-23fa-4f82-9630-774fb82cbe84
# ╟─465fd66d-452c-470e-b6c7-558f769e98a6
# ╟─d0d06fe1-4d2e-40ca-ba5a-10bafc03f8f0
# ╟─00000000-0000-0000-0000-000000000001
# ╟─00000000-0000-0000-0000-000000000002
