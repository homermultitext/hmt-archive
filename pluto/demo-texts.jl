### A Pluto.jl notebook ###
# v0.12.20

using Markdown
using InteractiveUtils

# ╔═╡ b6a8d7d4-679a-11eb-3b88-9d40c1e45114
begin
	using Pkg
	Pkg.activate(".")
	Pkg.add("CitableText")
	Pkg.add("Markdown")
	
	Pkg.add(url="https://github.com/homermultitext/HmtArchive.jl")
	
	using CitableText
	using HmtArchive
	using Markdown
end

# ╔═╡ 9eaf507a-679a-11eb-32ee-331cc3e5212f
md"""
# Working with texts in the HMT archive

This notebook illustrates some ways to work with texts archived in the Homer Multitext project's `hmt-archive` repository.


## Prerequisites

The hidden cell above this one loads two Julia packages:

```julia
using CitableText
using HmtArchive
```


"""

# ╔═╡ f7fa0692-679d-11eb-3769-83bfcf93c3ea
md"""

Create an `Archive` by passing in the root directory of a local clone of the `hmt-archive` repository ([https://github.com/homermultitext/hmt-archive](https://github.com/homermultitext/hmt-archive)).

Since this notebook is actually included in a subdirectory of that repository we can create an `Archive` like this:

```julia
hmt = Archive(dirname(pwd()))
```
"""

# ╔═╡ ea2b5ee0-679c-11eb-3a7e-f704d9a92a4b
hmt = Archive(dirname(pwd()))

# ╔═╡ c0ba95ce-679e-11eb-0a68-5b9fbcbe366b
md"""

## Building text corpora

The `HmtArchive` package includes functions to build text corpora for either the *Iliad* or the *scholia* of the Venetus A manuscript as:

- citable multivalent well-formed XML
- univocal diplomatic editions
- univocal normalized editions

"""

# ╔═╡ 1013c15e-679f-11eb-118d-8773fd66b602
md"**XML corpora**:"

# ╔═╡ 1827a4fa-679f-11eb-2073-0d6ba61dfa04
iliadxml = iliadxmlcorpus(hmt)

# ╔═╡ 37dad150-679f-11eb-3afa-a321d4d108f5
scholiaxml = scholiaxmlcorpus(hmt)

# ╔═╡ 4aedfd58-679f-11eb-0252-6d09fd919b40
md"Let's note their sizes:"

# ╔═╡ 55ffb2d6-679f-11eb-1cbe-15b993c86a8f
length(iliadxml.corpus)

# ╔═╡ 5c701d74-679f-11eb-269c-5569d74e4b25
length(scholiaxml.corpus)

# ╔═╡ 610f1d92-679f-11eb-16fd-93c1371de4ad
md"**Diplomatic corpora**:"

# ╔═╡ 79bb9c8c-679f-11eb-3ff6-833a7a8ee40d
iliaddiplomatic = iliaddipl(hmt)

# ╔═╡ 8356ab0c-679f-11eb-0f55-51a44801acca
scholiadiplomatic = scholiadipl(hmt)

# ╔═╡ 8c18916c-679f-11eb-0d28-b7ff5a8c3f9f
md"Sizes:"

# ╔═╡ 904e9196-679f-11eb-21f0-137580485911
length(iliaddiplomatic.corpus)

# ╔═╡ 98c19fd0-679f-11eb-1262-51dbfc13ea03
length(scholiadiplomatic.corpus)

# ╔═╡ a01c6058-679f-11eb-2ec1-e3d1900aa0de
md"**Normalized**:"

# ╔═╡ ab55be60-679f-11eb-17bc-c317b6222f17
iliadnormalized = iliadnormed(hmt)

# ╔═╡ b8466e4e-679f-11eb-2701-253cda3a88db
scholianormalized = scholianormed(hmt)

# ╔═╡ 0b04c29e-67a1-11eb-1049-3bf387925d06
md"Sizes:"

# ╔═╡ 0ff8d9e6-67a1-11eb-2154-0b50d3925734
length(iliadnormalized.corpus)

# ╔═╡ 168b83f8-67a1-11eb-075c-776a77a2ac35
length(scholianormalized.corpus)

# ╔═╡ 4c710160-679e-11eb-1e6d-d7f17286051d
md"""
**Comprehensive corpus**

There is also a functoin to build a comprehensive `CitableCorpus` containing all of these:
"""

# ╔═╡ 5f5f5530-679e-11eb-245c-799749bd64ef
texts = corpus(hmt)

# ╔═╡ a935c4b4-679e-11eb-088c-757e96eff8b8
md"It has many citable text passages."

# ╔═╡ af73bce6-679e-11eb-38a7-a9de07f3ed00
totalpsgs = length(texts.corpus)

# ╔═╡ 2351680a-67b0-11eb-2978-a7e0755c15b3
md"""Ever wonder how many citable nodes of text are in the editions of the HMT archive?  **$(totalpsgs)** !
"""

# ╔═╡ 0c82f4ca-6b04-11eb-020a-036745d9d3ba
md"## Surveying the contents of the corpus"

# ╔═╡ 24efc298-67c8-11eb-16a4-d9a0d1edcdc6
md"Here is an alphabetically sorted list of `textgroup.work` values in the corpus."

# ╔═╡ fcac42b8-6b03-11eb-0fec-93b035ee2989
md"Here is a list of all versions for each `textgroup.work` combination."

# ╔═╡ 9f868134-67ca-11eb-16bd-01101e5f1d03
md"And here's a list of version identifiers for each `textgroup.work`."

# ╔═╡ e54abade-67af-11eb-1de7-411146641f50
md"""
> A couple of useful functions
> (some may be generic enough that they belong in a library someplace)
"""

# ╔═╡ 78dc551c-67b0-11eb-1ad1-310b07cbf096
# Extract text part of workcomponent.
# With proper error checkling, this should be in the `CitableText` library.
function textid(u::CtsUrn)
  workparts(u)[2]
end

# ╔═╡ 73c2688e-67b0-11eb-3c83-cd45f4bc9174
# Find list of unique values for textgroup-work component of URNs in corpus
function textids(c::CitableCorpus)
  alltexts = map(cn -> workcomponent(dropversion(cn.urn)), c.corpus)
  unique(alltexts)
end

# ╔═╡ 1aa7c484-67c8-11eb-32b6-43b57046993f
begin 
	groupworks = textids(texts)
	items = map(gw -> "- $(gw)", groupworks)
	mdlist = join(sort(items), "\n")
	Markdown.parse(mdlist)
end

# ╔═╡ 2bfa9a62-67c9-11eb-0431-9da8868b86d2
# Extract version part of workcomponent.
# With proper error checkling, this should be in the `CitableText` library.
function versionid(u::CtsUrn)
	workparts(u)[3]
end

# ╔═╡ 5031108c-67b0-11eb-2c2b-8b0541ec1b61
#  Find versions in corpus of each textgroup-work
function textversions(c::CitableCorpus)
  textlist = textids(c)
  pairs = []
  for t in textlist
    filtered = filter(cn -> occursin(t, workcomponent(cn.urn)), c.corpus)
    verss = unique(map(cn -> versionid(cn.urn) , filtered))
    push!(pairs, (t => verss))
  end
  Dict(pairs)
end


# ╔═╡ 318396e4-67cb-11eb-3eff-278dc6b22341
# Format list of versions given for work in `textversions` dictionary
# as a markdown list
function mdlistforcorpus(c::CitableCorpus)
	verss = textversions(c)
	#join(map(v -> "- $(v)", verss), "\n")
	entries = []
	for k in keys(verss)
		verslist = verss[k]
		items = map(i -> "    - " * i, verslist)
		push!(entries, "- $(k):\n" * join(items,"\n") * "\n")
	end
	join(entries,"\n")
end


# ╔═╡ 9a0c5354-67cb-11eb-1175-59717ea7c6cc
versionsperwork = Markdown.parse(mdlistforcorpus(texts))

# ╔═╡ Cell order:
# ╟─b6a8d7d4-679a-11eb-3b88-9d40c1e45114
# ╟─2351680a-67b0-11eb-2978-a7e0755c15b3
# ╟─9eaf507a-679a-11eb-32ee-331cc3e5212f
# ╟─f7fa0692-679d-11eb-3769-83bfcf93c3ea
# ╟─ea2b5ee0-679c-11eb-3a7e-f704d9a92a4b
# ╟─c0ba95ce-679e-11eb-0a68-5b9fbcbe366b
# ╟─1013c15e-679f-11eb-118d-8773fd66b602
# ╠═1827a4fa-679f-11eb-2073-0d6ba61dfa04
# ╠═37dad150-679f-11eb-3afa-a321d4d108f5
# ╟─4aedfd58-679f-11eb-0252-6d09fd919b40
# ╠═55ffb2d6-679f-11eb-1cbe-15b993c86a8f
# ╠═5c701d74-679f-11eb-269c-5569d74e4b25
# ╟─610f1d92-679f-11eb-16fd-93c1371de4ad
# ╠═79bb9c8c-679f-11eb-3ff6-833a7a8ee40d
# ╠═8356ab0c-679f-11eb-0f55-51a44801acca
# ╟─8c18916c-679f-11eb-0d28-b7ff5a8c3f9f
# ╠═904e9196-679f-11eb-21f0-137580485911
# ╠═98c19fd0-679f-11eb-1262-51dbfc13ea03
# ╟─a01c6058-679f-11eb-2ec1-e3d1900aa0de
# ╠═ab55be60-679f-11eb-17bc-c317b6222f17
# ╠═b8466e4e-679f-11eb-2701-253cda3a88db
# ╟─0b04c29e-67a1-11eb-1049-3bf387925d06
# ╠═0ff8d9e6-67a1-11eb-2154-0b50d3925734
# ╠═168b83f8-67a1-11eb-075c-776a77a2ac35
# ╟─4c710160-679e-11eb-1e6d-d7f17286051d
# ╠═5f5f5530-679e-11eb-245c-799749bd64ef
# ╟─a935c4b4-679e-11eb-088c-757e96eff8b8
# ╟─af73bce6-679e-11eb-38a7-a9de07f3ed00
# ╟─0c82f4ca-6b04-11eb-020a-036745d9d3ba
# ╟─24efc298-67c8-11eb-16a4-d9a0d1edcdc6
# ╟─1aa7c484-67c8-11eb-32b6-43b57046993f
# ╟─fcac42b8-6b03-11eb-0fec-93b035ee2989
# ╠═9a0c5354-67cb-11eb-1175-59717ea7c6cc
# ╟─9f868134-67ca-11eb-16bd-01101e5f1d03
# ╟─e54abade-67af-11eb-1de7-411146641f50
# ╠═78dc551c-67b0-11eb-1ad1-310b07cbf096
# ╠═73c2688e-67b0-11eb-3c83-cd45f4bc9174
# ╠═2bfa9a62-67c9-11eb-0431-9da8868b86d2
# ╠═5031108c-67b0-11eb-2c2b-8b0541ec1b61
# ╠═318396e4-67cb-11eb-3eff-278dc6b22341
