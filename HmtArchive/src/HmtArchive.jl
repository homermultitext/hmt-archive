module HmtArchive

using CitableObject
using CitableTeiReaders
using CitableText
using EditionBuilders
using EzXML


# Diplomatic edition of VA Iliad
export iliaddipl

# Useful to make declaration of TEI namespace global
export teins

include("textbuilding.jl")
include("namedentities.jl")

end # module
