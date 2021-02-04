module HmtArchive

using CitableObject
using CitableTeiReaders
using CitableText
using EditionBuilders
using EzXML


export iliadxmlcorpus, scholiaxmlcorpus
export iliaddipl

# Useful to make declaration of TEI namespace global
export teins


include("textbuilding.jl")
include("namedentities.jl")
include("debug.jl")


end # module
