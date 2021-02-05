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

Create an `Archive` by passing in the root directory in a clone of the repository at [https://github.com/homermultitext/hmt-archive](https://github.com/homermultitext/hmt-archive).

Since this notebook is actually part of that repository in a subdirectory of the root we can create an `Archive` like this:

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
iliadnormalized = ilianormed(hmt)

# ╔═╡ b8466e4e-679f-11eb-2701-253cda3a88db
scholianormalized = scholianormed(hmt)

# ╔═╡ 4c710160-679e-11eb-1e6d-d7f17286051d
md"Build a comprehensive `CitableCorpus`:"

# ╔═╡ 5f5f5530-679e-11eb-245c-799749bd64ef
texts = corpus(hmt)

# ╔═╡ a935c4b4-679e-11eb-088c-757e96eff8b8
md"It has many citable text passages."

# ╔═╡ af73bce6-679e-11eb-38a7-a9de07f3ed00
length(texts.corpus)

# ╔═╡ 1023c2d2-679c-11eb-031a-0dd8fea0bfb0
iliad = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")

# ╔═╡ Cell order:
# ╟─b6a8d7d4-679a-11eb-3b88-9d40c1e45114
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
# ╟─4c710160-679e-11eb-1e6d-d7f17286051d
# ╟─5f5f5530-679e-11eb-245c-799749bd64ef
# ╟─a935c4b4-679e-11eb-088c-757e96eff8b8
# ╠═af73bce6-679e-11eb-38a7-a9de07f3ed00
# ╟─1023c2d2-679c-11eb-031a-0dd8fea0bfb0
