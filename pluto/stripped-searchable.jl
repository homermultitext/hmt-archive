### A Pluto.jl notebook ###
# v0.12.20

using Markdown
using InteractiveUtils

# ╔═╡ b6a8d7d4-679a-11eb-3b88-9d40c1e45114
begin
	using Pkg
	Pkg.activate(".")
	Pkg.add("CitableText")

	Pkg.add(url="https://github.com/homermultitext/HmtArchive.jl")
	
	using CitableText
	using HmtArchive
	using Unicode
end

# ╔═╡ d0c15568-7101-11eb-32b1-a3daf87952bb
md"Setup environment in an invisible cell."

# ╔═╡ 9eaf507a-679a-11eb-32ee-331cc3e5212f
md"""
# Building a corpus of searchable strings

How to build a corpus of HMT scholia for easy string searching:  a normalized text, all lower-case, with accents and breathings stripped.

## Prerequisites

The hidden cell above this one loads the Julia packages you need:

```julia
using CitableText
using HmtArchive
using Unicode
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

## Building a normalized corpus


"""

# ╔═╡ 1013c15e-679f-11eb-118d-8773fd66b602
md"**1**. Assemble a corpus from the archival XML:"

# ╔═╡ 37dad150-679f-11eb-3afa-a321d4d108f5
scholiaxml = scholiaxmlcorpus(hmt)

# ╔═╡ a01c6058-679f-11eb-2ec1-e3d1900aa0de
md"**2**. Extract a normalized edition from multivalent TEI:"

# ╔═╡ b8466e4e-679f-11eb-2701-253cda3a88db
scholianormalized = scholianormed(hmt)

# ╔═╡ 9975dbc8-7102-11eb-1d49-972b13d2204e
md"The two corpora should be the same size."

# ╔═╡ 168b83f8-67a1-11eb-075c-776a77a2ac35
length(scholiaxml.corpus) == length(scholianormalized.corpus)

# ╔═╡ abb52fa4-7103-11eb-36f6-2dcebd07ca19
md"Omit citable nodes with no text."

# ╔═╡ b6bda1b0-7103-11eb-29d9-c5520a81d5c7
nonempty = filter(n -> n.text != "", scholianormalized.corpus)

# ╔═╡ c88752d8-7103-11eb-1e48-398e57cfe61d
length(nonempty)

# ╔═╡ d7cd31a8-7104-11eb-1a7b-0507c64510f7
md"Omit `ref` nodes."

# ╔═╡ d178f3c8-7104-11eb-249a-9106a334a908
scholia = filter(cn -> !occursin("ref", passagecomponent(cn.urn)), nonempty)

# ╔═╡ f4e9f898-7104-11eb-15a9-69c85c385d5c
length(scholia)

# ╔═╡ e1e89e40-7102-11eb-2bfd-3deba3df971f
md"## Preparing the corpus for easy searching"

# ╔═╡ ebbeb5e8-7103-11eb-26e9-9f9690ce08f5
md"**1**. Make corpus lower case."

# ╔═╡ f3e39eb0-7102-11eb-17b8-4178aa088a38
lc = map(cn -> CitableNode(cn.urn, lowercase(cn.text)), scholia)

# ╔═╡ fec9c596-7104-11eb-119a-cd421eb243c1
length(lc) == length(scholia)

# ╔═╡ f788a1fe-7103-11eb-1bca-635d569460d2
md"**2**.  Strip breathings and accents."

# ╔═╡ 0543125c-7104-11eb-22e2-97614e4cd2bf
stripped = map(cn -> CitableNode(cn.urn, Unicode.normalize(cn.text,stripmark=true)), lc)

# ╔═╡ 9fdc607e-7105-11eb-04de-7780cb8bc2c3
md"## Write corpus to delimited-text file"

# ╔═╡ a9eb7cb2-7105-11eb-370c-fd4cfce2ec05
#=
open("searchable_scholia.tsv", "w") do io
    print(io, cex(stripped, "\t")
end
=#

# ╔═╡ Cell order:
# ╟─d0c15568-7101-11eb-32b1-a3daf87952bb
# ╟─b6a8d7d4-679a-11eb-3b88-9d40c1e45114
# ╟─9eaf507a-679a-11eb-32ee-331cc3e5212f
# ╟─f7fa0692-679d-11eb-3769-83bfcf93c3ea
# ╟─ea2b5ee0-679c-11eb-3a7e-f704d9a92a4b
# ╟─c0ba95ce-679e-11eb-0a68-5b9fbcbe366b
# ╟─1013c15e-679f-11eb-118d-8773fd66b602
# ╠═37dad150-679f-11eb-3afa-a321d4d108f5
# ╟─a01c6058-679f-11eb-2ec1-e3d1900aa0de
# ╠═b8466e4e-679f-11eb-2701-253cda3a88db
# ╟─9975dbc8-7102-11eb-1d49-972b13d2204e
# ╠═168b83f8-67a1-11eb-075c-776a77a2ac35
# ╟─abb52fa4-7103-11eb-36f6-2dcebd07ca19
# ╠═b6bda1b0-7103-11eb-29d9-c5520a81d5c7
# ╠═c88752d8-7103-11eb-1e48-398e57cfe61d
# ╟─d7cd31a8-7104-11eb-1a7b-0507c64510f7
# ╠═d178f3c8-7104-11eb-249a-9106a334a908
# ╠═f4e9f898-7104-11eb-15a9-69c85c385d5c
# ╟─e1e89e40-7102-11eb-2bfd-3deba3df971f
# ╟─ebbeb5e8-7103-11eb-26e9-9f9690ce08f5
# ╠═f3e39eb0-7102-11eb-17b8-4178aa088a38
# ╠═fec9c596-7104-11eb-119a-cd421eb243c1
# ╟─f788a1fe-7103-11eb-1bca-635d569460d2
# ╠═0543125c-7104-11eb-22e2-97614e4cd2bf
# ╟─9fdc607e-7105-11eb-04de-7780cb8bc2c3
# ╠═a9eb7cb2-7105-11eb-370c-fd4cfce2ec05
