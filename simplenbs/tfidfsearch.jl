### A Pluto.jl notebook ###
# v0.15.0

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

# ╔═╡ a492f62a-d420-11eb-04f5-29615000168a
using TextAnalysis, CitableCorpus, CitableText, PlutoUI, Markdown, CiteEXchange, HTTP, Unicode

# ╔═╡ 3ced021a-5548-464c-8448-4cdbf62c2fb0
md""">## Explore salient terms
>
>Search the *scholia* in in-progress work on *Iliad* 8 and 10.
> See "documents" containing the term, and the TF-IDF score for the
> term in each document. (In this notebook, each *scholion*'s comment is treated as a *document*.)
>
> For each matching document, the results display both a full text of the scholion, and a version
> highlighting the term in a text without accents or breathings.
"""

# ╔═╡ c6405d2c-5c14-4ee1-bf6d-165f3e0d9ec3
md"""
Term: $(@bind rawterm TextField(;default=""))
"""

# ╔═╡ f6d71fbd-cf3b-4ee3-a45a-4108f28a84d5
term = Unicode.normalize(rawterm; stripmark=true)

# ╔═╡ 99dab151-d9bb-43d0-b215-3c38482d3d50
md">(Peek at internals of how `TextAnalysis` module indexing works:)"

# ╔═╡ cf4f497b-0ef6-4e50-8aef-f0fe945222dc
md">Formatting"

# ╔═╡ 3ecf3e9f-328e-4265-b99c-ed5784be1301
# Highlight term in txt
function highlight(term, txt)
	wrapped = replace(txt, term => """<span class="hilite">$term</span>""")
	wrapped
end

# ╔═╡ 3d7bafa0-1819-4944-8648-fc26da7213b6
	hint(text, label) = Markdown.MD(Markdown.Admonition("warn", label, [text]))

# ╔═╡ fa925d14-98ee-46c0-8732-8e85b5a138c9

	hint(md"""
- remove punctuation from stripped corpus in order to get more relevant TF-IDF score.
- score average word length of scholia with this term

""", "Features to add")

# ╔═╡ 91dee988-3378-47da-803f-4d839b828c4a
css = html"""
<style>
.hilite {
	background-color: yellow;
	font-weight: bold;
}
.scholion {
	background-color: white;
}
.hint {
	color: silver;
}
</style>
"""

# ╔═╡ d1531ae6-06a8-4723-88cc-c4fe00d8f8a6
md"> Citable text passages and corpora"

# ╔═╡ 5d904b6c-80c3-4a8a-ba11-82fba50be209
xlation = "https://raw.githubusercontent.com/homermultitext/hmt-archive/master/archive/translations/book_ten_due_ebbott.cex"

# ╔═╡ 42ea5cd8-e6a5-4214-b243-caad4f6299d9
# From a URL pointing to a CEX file, get the text content of 
# ctsdata blocks
function txtfromurl(url)
	str = HTTP.get(url).body |> String
	blks = blocks(str)
	txt = datafortype("ctsdata", blks)
	c = CitableCorpus.fromdelimited(CitableTextCorpus, join(txt, "\n"))
	txtcorp = map(cn -> cn.text, c.corpus)
	txtcorp
end

# ╔═╡ 9fc91194-e193-4a82-8283-1a3e55dff4dc
md">Other"

# ╔═╡ 5b2cb068-2f9d-4877-8124-f8640c361543
url = "https://raw.githubusercontent.com/hmteditors/composite-summer21/main/data/s21corpus-normed.cex" #"https://raw.githubusercontent.com/hmteditors/composite-summer21/main/data/s21corpus-normed.cex"

# ╔═╡ 09e0722b-9571-4c94-a6cc-b408fc3f13cc
c = CitableCorpus.fromurl(CitableTextCorpus, url, "|")

# ╔═╡ 9d0c3590-293b-427c-82bd-9e9f1c67b73e
reff = filter(cn -> endswith(cn.urn.urn, "ref"), c.corpus)

# ╔═╡ f60970e8-ca28-458a-b23d-a581c960e3f1
comments = filter(cn -> endswith(cn.urn.urn, "comment"), c.corpus)

# ╔═╡ 13092236-e810-4ace-aa40-8250edbad095
function formatscholion(i)
	
	scholurn = comments[i].urn
	docid = workparts(scholurn)[2]
	#scholpsg = passagecomponent(scholurn)
	scholpsg = collapsePassageBy(scholurn, 1) |>  passagecomponent
	ref = replace(passagecomponent(scholurn), "comment" => "ref")
	refurn = addpassage(scholurn, ref)
	iliad = filter(cn -> cn.urn == refurn, c.corpus)
	iliadurn = CtsUrn(iliad[1].text)
	ilmatches = length(iliad)
	
	string("<b>", docid, "</b> ", scholpsg,  ", commenting on <b><i>Iliad</i> ", passagecomponent(iliadurn), "</b> <blockquote class=\"scholion\">", comments[i].text,"</blockquote>" )
	
end

# ╔═╡ 1c938931-47bb-4042-a051-84b48c2eaecb
function txtforcomments()
	txts = map(cn -> Unicode.normalize(cn.text; stripmark=true), comments)
	map(t -> lowercase(t), txts)
end


# ╔═╡ bba171b2-f0a9-4172-a7e9-5d365a1b4f22
srcdocs = txtforcomments() #txtfromurl(xlation)

# ╔═╡ 33a8a1e4-e56e-4b32-b517-430b6ab40cb9
comments |> length

# ╔═╡ 6b3bc116-d544-4470-8b44-64ab0db15739
md">julia `TextAnalysis` structures"

# ╔═╡ 8dffd39f-f4a4-45d9-96d5-1f05084a1e96
docs = map(s -> StringDocument(s), srcdocs)

# ╔═╡ 4f5c8bae-81d5-4475-9936-9e4e4aacaadb
corp = Corpus(docs)

# ╔═╡ 7bccf880-ae21-40ac-ac99-7e1bf59f683b
# Index value of documents in corpus where `term` appears.
# The document is accessible as corp.documents[INDEXVALUE]
documentindices = corp[term]

# ╔═╡ f274badc-adb1-4136-ab45-e614c08618a1
matchcount = length(documentindices)

# ╔═╡ 2699073c-dd9d-4dae-af37-077e85fad819
lex = begin
	update_inverse_index!(corp)
	update_lexicon!(corp)
	lexicon(corp)
end

# ╔═╡ 93533849-f297-4479-9b63-d4f6754e4e08
m = DocumentTermMatrix(corp)

# ╔═╡ 0526b4e1-cd16-4fab-8951-1c1bfac4d465
# Find index of term within document matrix
termidx = findfirst(t -> t == term, m.terms)

# ╔═╡ e2e87b56-8003-41f8-8660-55e557665276
begin
	if isempty(term)
		HTML("<span class=\"hint\">Please enter a term</span>")
	else
	label = matchcount == 1 ? "**1** occurrence" : "**$matchcount** occurrences"
	display = """$label of `term $termidx` *$term*.

Term frequency in corpus: **$(round(lexical_frequency(corp, term); digits=5))**
"""
	Markdown.parse(display)
	end
end

# ╔═╡ 0caceb74-ece3-41b6-aae5-b55f34b88cd1
tfidf = tf_idf(m)

# ╔═╡ 2f5200bc-03c7-4423-b23b-6e0fdf9e9ade
begin
	psgs = ["<ol>"]
	for idx in documentindices
		score  = tfidf[idx,termidx]
		hilited = highlight(term, corp.documents[idx].text)
		push!(psgs, string("<li>",    "<code>doc. $(idx): tf-idf score ", round(score; digits = 3), "</code> ", formatscholion(idx), " <blockquote>", hilited, "</blockquote>"))
		push!(psgs, "</li>")
	end
	push!(psgs, "</ol>")
	HTML(join(psgs, "\n"))
end


# ╔═╡ dabf76d8-faea-43a3-9589-9521af253f32
m.terms |> length

# ╔═╡ 00000000-0000-0000-0000-000000000001
PLUTO_PROJECT_TOML_CONTENTS = """
[deps]
CitableCorpus = "cf5ac11a-93ef-4a1a-97a3-f6af101603b5"
CitableText = "41e66566-473b-49d4-85b7-da83b66615d8"
CiteEXchange = "e2e9ead3-1b6c-4e96-b95f-43e6ab899178"
HTTP = "cd3eb016-35fb-5094-929b-558a96fad6f3"
Markdown = "d6f4376e-aef5-505a-96c1-9c027394607a"
PlutoUI = "7f904dfe-b85e-4ff6-b463-dae2292396a8"
TextAnalysis = "a2db99b7-8b79-58f8-94bf-bbc811eef33d"
Unicode = "4ec0a83e-493e-50e2-b9ac-8f72acf5a8f5"

[compat]
CitableCorpus = "~0.2.1"
CitableText = "~0.9.0"
CiteEXchange = "~0.3.0"
HTTP = "~0.9.10"
PlutoUI = "~0.7.9"
TextAnalysis = "~0.7.3"
"""

# ╔═╡ 00000000-0000-0000-0000-000000000002
PLUTO_MANIFEST_TOML_CONTENTS = """
# This file is machine-generated - editing it directly is not advised

[[ArgTools]]
uuid = "0dad84c5-d112-42e6-8d28-ef12dabb789f"

[[Artifacts]]
uuid = "56f22d72-fd6d-98f1-02f0-08ddc0907c33"

[[Base64]]
uuid = "2a0f44e3-6c83-55bd-87e4-b1978d98bd5f"

[[BenchmarkTools]]
deps = ["JSON", "Logging", "Printf", "Statistics", "UUIDs"]
git-tree-sha1 = "9e62e66db34540a0c919d72172cc2f642ac71260"
uuid = "6e4b80f9-dd63-53aa-95a3-0cdb28fa8baf"
version = "0.5.0"

[[BinaryProvider]]
deps = ["Libdl", "Logging", "SHA"]
git-tree-sha1 = "ecdec412a9abc8db54c0efc5548c64dfce072058"
uuid = "b99e7846-7c00-51b0-8f62-c81ae34c0232"
version = "0.5.10"

[[CSV]]
deps = ["Dates", "Mmap", "Parsers", "PooledArrays", "SentinelArrays", "Tables", "Unicode"]
git-tree-sha1 = "b83aa3f513be680454437a0eee21001607e5d983"
uuid = "336ed68f-0bac-5ca0-87d4-7b16caf5d00b"
version = "0.8.5"

[[CitableBase]]
deps = ["DocStringExtensions", "Documenter", "Test"]
git-tree-sha1 = "e1edbddb151b18f8290b8f19e4310c369b01c049"
uuid = "d6f014bd-995c-41bd-9893-703339864534"
version = "1.2.2"

[[CitableCorpus]]
deps = ["CSV", "CitableText", "DataFrames", "DocStringExtensions", "Documenter", "HTTP", "Test"]
git-tree-sha1 = "ce07aadee5fe89c3e72667a72ea804502b7e2dcf"
uuid = "cf5ac11a-93ef-4a1a-97a3-f6af101603b5"
version = "0.2.1"

[[CitableObject]]
deps = ["CitableBase", "DocStringExtensions", "Documenter", "Test"]
git-tree-sha1 = "26433318def871240c90de244a364f056ace7041"
uuid = "e2b2f5ea-1cd8-4ce8-9b2b-05dad64c2a57"
version = "0.6.0"

[[CitableText]]
deps = ["BenchmarkTools", "CitableBase", "DocStringExtensions", "Documenter", "Test"]
git-tree-sha1 = "3d95c0ceea520fae5248a6842026b99d6ca23356"
uuid = "41e66566-473b-49d4-85b7-da83b66615d8"
version = "0.9.0"

[[CiteEXchange]]
deps = ["CitableObject", "DocStringExtensions", "Documenter", "Test"]
git-tree-sha1 = "ad1d80adea90ef286b9f1cfd7de62e71d2c48b4c"
uuid = "e2e9ead3-1b6c-4e96-b95f-43e6ab899178"
version = "0.3.0"

[[Compat]]
deps = ["Base64", "Dates", "DelimitedFiles", "Distributed", "InteractiveUtils", "LibGit2", "Libdl", "LinearAlgebra", "Markdown", "Mmap", "Pkg", "Printf", "REPL", "Random", "SHA", "Serialization", "SharedArrays", "Sockets", "SparseArrays", "Statistics", "Test", "UUIDs", "Unicode"]
git-tree-sha1 = "dc7dedc2c2aa9faf59a55c622760a25cbefbe941"
uuid = "34da2185-b29b-5c13-b0c7-acf172513d20"
version = "3.31.0"

[[Crayons]]
git-tree-sha1 = "3f71217b538d7aaee0b69ab47d9b7724ca8afa0d"
uuid = "a8cc5b0e-0ffa-5ad4-8c14-923d3ee1735f"
version = "4.0.4"

[[DataAPI]]
git-tree-sha1 = "dfb3b7e89e395be1e25c2ad6d7690dc29cc53b1d"
uuid = "9a962f9c-6df0-11e9-0e5d-c546b8b5ee8a"
version = "1.6.0"

[[DataDeps]]
deps = ["BinaryProvider", "HTTP", "Libdl", "Reexport", "SHA", "p7zip_jll"]
git-tree-sha1 = "4f0e41ff461d42cfc62ff0de4f1cd44c6e6b3771"
uuid = "124859b0-ceae-595e-8997-d05f6a7a8dfe"
version = "0.7.7"

[[DataFrames]]
deps = ["Compat", "DataAPI", "Future", "InvertedIndices", "IteratorInterfaceExtensions", "LinearAlgebra", "Markdown", "Missings", "PooledArrays", "PrettyTables", "Printf", "REPL", "Reexport", "SortingAlgorithms", "Statistics", "TableTraits", "Tables", "Unicode"]
git-tree-sha1 = "66ee4fe515a9294a8836ef18eea7239c6ac3db5e"
uuid = "a93c6f00-e57d-5684-b7b6-d8193f3e46c0"
version = "1.1.1"

[[DataStructures]]
deps = ["Compat", "InteractiveUtils", "OrderedCollections"]
git-tree-sha1 = "4437b64df1e0adccc3e5d1adbc3ac741095e4677"
uuid = "864edb3b-99cc-5e75-8d2d-829cb0a9cfe8"
version = "0.18.9"

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

[[Distributed]]
deps = ["Random", "Serialization", "Sockets"]
uuid = "8ba89e20-285c-5b6f-9357-94700520ee1b"

[[DocStringExtensions]]
deps = ["LibGit2"]
git-tree-sha1 = "a32185f5428d3986f47c2ab78b1f216d5e6cc96f"
uuid = "ffbed154-4ef7-542d-bbb7-c09d3a79fcae"
version = "0.8.5"

[[Documenter]]
deps = ["Base64", "Dates", "DocStringExtensions", "IOCapture", "InteractiveUtils", "JSON", "LibGit2", "Logging", "Markdown", "REPL", "Test", "Unicode"]
git-tree-sha1 = "3ebb967819b284dc1e3c0422229b58a40a255649"
uuid = "e30172f5-a6a5-5a46-863b-614d45cd2de4"
version = "0.26.3"

[[Downloads]]
deps = ["ArgTools", "LibCURL", "NetworkOptions"]
uuid = "f43a241f-c20a-4ad4-852c-f6b1247861c6"

[[Formatting]]
deps = ["Printf"]
git-tree-sha1 = "8339d61043228fdd3eb658d86c926cb282ae72a8"
uuid = "59287772-0a20-5a39-b81b-1366585eb4c0"
version = "0.4.2"

[[Future]]
deps = ["Random"]
uuid = "9fa8497b-333b-5362-9e8d-4d0656e87820"

[[HTML_Entities]]
deps = ["StrTables"]
git-tree-sha1 = "c4144ed3bc5f67f595622ad03c0e39fa6c70ccc7"
uuid = "7693890a-d069-55fe-a829-b4a6d304f0ee"
version = "1.0.1"

[[HTTP]]
deps = ["Base64", "Dates", "IniFile", "MbedTLS", "NetworkOptions", "Sockets", "URIs"]
git-tree-sha1 = "86ed84701fbfd1142c9786f8e53c595ff5a4def9"
uuid = "cd3eb016-35fb-5094-929b-558a96fad6f3"
version = "0.9.10"

[[IOCapture]]
deps = ["Logging"]
git-tree-sha1 = "377252859f740c217b936cebcd918a44f9b53b59"
uuid = "b5f81e59-6552-4d32-b1f0-c071b021bf89"
version = "0.1.1"

[[IniFile]]
deps = ["Test"]
git-tree-sha1 = "098e4d2c533924c921f9f9847274f2ad89e018b8"
uuid = "83e8ac13-25f8-5344-8a64-a9f2b223428f"
version = "0.5.0"

[[InteractiveUtils]]
deps = ["Markdown"]
uuid = "b77e0a4c-d291-57a0-90e8-8db25a27a240"

[[InvertedIndices]]
deps = ["Test"]
git-tree-sha1 = "15732c475062348b0165684ffe28e85ea8396afc"
uuid = "41ab1584-1d38-5bbf-9106-f11c6c58b48f"
version = "1.0.0"

[[IteratorInterfaceExtensions]]
git-tree-sha1 = "a3f24677c21f5bbe9d2a714f95dcd58337fb2856"
uuid = "82899510-4779-5014-852e-03e436cf321d"
version = "1.0.0"

[[JSON]]
deps = ["Dates", "Mmap", "Parsers", "Unicode"]
git-tree-sha1 = "81690084b6198a2e1da36fcfda16eeca9f9f24e4"
uuid = "682c06a0-de6a-54ab-a142-c8b1cf79cde6"
version = "0.21.1"

[[Languages]]
deps = ["InteractiveUtils", "JSON"]
git-tree-sha1 = "b1a564061268ccc3f3397ac0982983a657d4dcb8"
uuid = "8ef0a80b-9436-5d2c-a485-80b904378c43"
version = "0.4.3"

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
git-tree-sha1 = "4ea90bd5d3985ae1f9a908bd4500ae88921c5ce7"
uuid = "e1d29d7a-bbdc-5cf2-9ac0-f12de2c33e28"
version = "1.0.0"

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

[[Parsers]]
deps = ["Dates"]
git-tree-sha1 = "c8abc88faa3f7a3950832ac5d6e690881590d6dc"
uuid = "69de0a69-1ddd-5017-9359-2bf0b02dc9f0"
version = "1.1.0"

[[Pkg]]
deps = ["Artifacts", "Dates", "Downloads", "LibGit2", "Libdl", "Logging", "Markdown", "Printf", "REPL", "Random", "SHA", "Serialization", "TOML", "Tar", "UUIDs", "p7zip_jll"]
uuid = "44cfe95a-1eb2-52ea-b672-e2afdf69b78f"

[[PlutoUI]]
deps = ["Base64", "Dates", "InteractiveUtils", "JSON", "Logging", "Markdown", "Random", "Reexport", "Suppressor"]
git-tree-sha1 = "44e225d5837e2a2345e69a1d1e01ac2443ff9fcb"
uuid = "7f904dfe-b85e-4ff6-b463-dae2292396a8"
version = "0.7.9"

[[PooledArrays]]
deps = ["DataAPI", "Future"]
git-tree-sha1 = "cde4ce9d6f33219465b55162811d8de8139c0414"
uuid = "2dfb63ee-cc39-5dd5-95bd-886bf059d720"
version = "1.2.1"

[[PrettyTables]]
deps = ["Crayons", "Formatting", "Markdown", "Reexport", "Tables"]
git-tree-sha1 = "0d1245a357cc61c8cd61934c07447aa569ff22e6"
uuid = "08abe8d2-0d0c-5749-adfa-8a2ac140af0d"
version = "1.1.0"

[[Printf]]
deps = ["Unicode"]
uuid = "de0858da-6303-5e67-8744-51eddeeeb8d7"

[[ProgressMeter]]
deps = ["Distributed", "Printf"]
git-tree-sha1 = "afadeba63d90ff223a6a48d2009434ecee2ec9e8"
uuid = "92933f4c-e287-5a05-a399-4b506db050ca"
version = "1.7.1"

[[REPL]]
deps = ["InteractiveUtils", "Markdown", "Sockets", "Unicode"]
uuid = "3fa0cd96-eef1-5676-8a61-b3b8758bbffb"

[[Random]]
deps = ["Serialization"]
uuid = "9a3f8284-a2c9-5f02-9a11-845980a1fd5c"

[[Reexport]]
git-tree-sha1 = "5f6c21241f0f655da3952fd60aa18477cf96c220"
uuid = "189a3867-3050-52da-a836-e630ba90ab69"
version = "1.1.0"

[[SHA]]
uuid = "ea8e919c-243c-51af-8825-aaa63cd721ce"

[[SentinelArrays]]
deps = ["Dates", "Random"]
git-tree-sha1 = "ffae887d0f0222a19c406a11c3831776d1383e3d"
uuid = "91c51154-3ec4-41a3-a24f-3f23e20d615c"
version = "1.3.3"

[[Serialization]]
uuid = "9e88b42a-f829-5b0c-bbe9-9e923198166b"

[[SharedArrays]]
deps = ["Distributed", "Mmap", "Random", "Serialization"]
uuid = "1a1011a3-84de-559e-8e89-a11a2f7dc383"

[[Snowball]]
deps = ["Languages", "Snowball_jll", "WordTokenizers"]
git-tree-sha1 = "d38c1ff8a2fca7b1c65a51457dabebef28052399"
uuid = "fb8f903a-0164-4e73-9ffe-431110250c3b"
version = "0.1.0"

[[Snowball_jll]]
deps = ["Libdl", "Pkg"]
git-tree-sha1 = "35031519df40fbf0d4a6d2faae4f00e117b0ad11"
uuid = "88f46535-a3c0-54f4-998e-4320a1339f51"
version = "2.0.0+0"

[[Sockets]]
uuid = "6462fe0b-24de-5631-8697-dd941f90decc"

[[SortingAlgorithms]]
deps = ["DataStructures"]
git-tree-sha1 = "2ec1962eba973f383239da22e75218565c390a96"
uuid = "a2af1166-a08f-5f64-846c-94a0d3cef48c"
version = "1.0.0"

[[SparseArrays]]
deps = ["LinearAlgebra", "Random"]
uuid = "2f01184e-e22b-5df5-ae63-d93ebab69eaf"

[[Statistics]]
deps = ["LinearAlgebra", "SparseArrays"]
uuid = "10745b16-79ce-11e8-11f9-7d13ad32a3b2"

[[StatsAPI]]
git-tree-sha1 = "1958272568dc176a1d881acb797beb909c785510"
uuid = "82ae8749-77ed-4fe6-ae5f-f523153014b0"
version = "1.0.0"

[[StatsBase]]
deps = ["DataAPI", "DataStructures", "LinearAlgebra", "Missings", "Printf", "Random", "SortingAlgorithms", "SparseArrays", "Statistics", "StatsAPI"]
git-tree-sha1 = "2f6792d523d7448bbe2fec99eca9218f06cc746d"
uuid = "2913bbd2-ae8a-5f71-8c99-4fb6c76f3a91"
version = "0.33.8"

[[StrTables]]
deps = ["Dates"]
git-tree-sha1 = "5998faae8c6308acc25c25896562a1e66a3bb038"
uuid = "9700d1a9-a7c8-5760-9816-a99fda30bb8f"
version = "1.0.1"

[[Suppressor]]
git-tree-sha1 = "a819d77f31f83e5792a76081eee1ea6342ab8787"
uuid = "fd094767-a336-5f1f-9728-57cf17d0bbfb"
version = "0.2.0"

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
git-tree-sha1 = "aa30f8bb63f9ff3f8303a06c604c8500a69aa791"
uuid = "bd369af6-aec1-5ad0-b16a-f7cc5008161c"
version = "1.4.3"

[[Tar]]
deps = ["ArgTools", "SHA"]
uuid = "a4e569a6-e804-4fa4-b0f3-eef7a1d5b13e"

[[Test]]
deps = ["InteractiveUtils", "Logging", "Random", "Serialization"]
uuid = "8dfed614-e22c-5e08-85e1-65c5234f0b40"

[[TextAnalysis]]
deps = ["DataStructures", "DelimitedFiles", "JSON", "Languages", "LinearAlgebra", "Printf", "ProgressMeter", "Random", "Serialization", "Snowball", "SparseArrays", "Statistics", "StatsBase", "Tables", "WordTokenizers"]
git-tree-sha1 = "bc85e54209c30e69e1925460ec0257a916683f59"
uuid = "a2db99b7-8b79-58f8-94bf-bbc811eef33d"
version = "0.7.3"

[[URIs]]
git-tree-sha1 = "97bbe755a53fe859669cd907f2d96aee8d2c1355"
uuid = "5c2747f8-b7ea-4ff2-ba2e-563bfd36b1d4"
version = "1.3.0"

[[UUIDs]]
deps = ["Random", "SHA"]
uuid = "cf7118a7-6976-5b1a-9a39-7adc72f591a4"

[[Unicode]]
uuid = "4ec0a83e-493e-50e2-b9ac-8f72acf5a8f5"

[[WordTokenizers]]
deps = ["DataDeps", "HTML_Entities", "StrTables", "Unicode"]
git-tree-sha1 = "01dd4068c638da2431269f49a5964bf42ff6c9d2"
uuid = "796a5d58-b03d-544a-977e-18100b691f6e"
version = "0.5.6"

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
# ╟─a492f62a-d420-11eb-04f5-29615000168a
# ╟─fa925d14-98ee-46c0-8732-8e85b5a138c9
# ╟─3ced021a-5548-464c-8448-4cdbf62c2fb0
# ╟─e2e87b56-8003-41f8-8660-55e557665276
# ╟─c6405d2c-5c14-4ee1-bf6d-165f3e0d9ec3
# ╟─f6d71fbd-cf3b-4ee3-a45a-4108f28a84d5
# ╟─2f5200bc-03c7-4423-b23b-6e0fdf9e9ade
# ╟─99dab151-d9bb-43d0-b215-3c38482d3d50
# ╟─0526b4e1-cd16-4fab-8951-1c1bfac4d465
# ╟─7bccf880-ae21-40ac-ac99-7e1bf59f683b
# ╟─f274badc-adb1-4136-ab45-e614c08618a1
# ╟─cf4f497b-0ef6-4e50-8aef-f0fe945222dc
# ╟─13092236-e810-4ace-aa40-8250edbad095
# ╟─3ecf3e9f-328e-4265-b99c-ed5784be1301
# ╟─3d7bafa0-1819-4944-8648-fc26da7213b6
# ╟─91dee988-3378-47da-803f-4d839b828c4a
# ╟─d1531ae6-06a8-4723-88cc-c4fe00d8f8a6
# ╟─bba171b2-f0a9-4172-a7e9-5d365a1b4f22
# ╟─5d904b6c-80c3-4a8a-ba11-82fba50be209
# ╟─42ea5cd8-e6a5-4214-b243-caad4f6299d9
# ╟─1c938931-47bb-4042-a051-84b48c2eaecb
# ╟─9fc91194-e193-4a82-8283-1a3e55dff4dc
# ╟─5b2cb068-2f9d-4877-8124-f8640c361543
# ╟─09e0722b-9571-4c94-a6cc-b408fc3f13cc
# ╟─9d0c3590-293b-427c-82bd-9e9f1c67b73e
# ╟─f60970e8-ca28-458a-b23d-a581c960e3f1
# ╟─33a8a1e4-e56e-4b32-b517-430b6ab40cb9
# ╟─6b3bc116-d544-4470-8b44-64ab0db15739
# ╟─8dffd39f-f4a4-45d9-96d5-1f05084a1e96
# ╟─4f5c8bae-81d5-4475-9936-9e4e4aacaadb
# ╟─2699073c-dd9d-4dae-af37-077e85fad819
# ╟─93533849-f297-4479-9b63-d4f6754e4e08
# ╟─0caceb74-ece3-41b6-aae5-b55f34b88cd1
# ╟─dabf76d8-faea-43a3-9589-9521af253f32
# ╟─00000000-0000-0000-0000-000000000001
# ╟─00000000-0000-0000-0000-000000000002
