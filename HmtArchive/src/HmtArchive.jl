module HmtArchive

using CitableObject
using CitableTeiReaders
using CitableText
using EditionBuilders
using EzXML


# Builds single corpus of all texts
# in the archive in all editions:
export corpus

# These are available if you only want to
# work with some pieces of the archive
export iliadxmlcorpus, scholiaxmlcorpus
export iliaddipl, ilianormed
export scholiadipl, scholianormed

# Useful to make declaration of TEI namespace global
export teins


include("textbuilding.jl")
include("namedentities.jl")
include("debug.jl")


end # module
