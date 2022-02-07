
using Dash
using CitableBase, CitableText, CitableCorpus
using Unicode

url = "https://raw.githubusercontent.com/homermultitext/hmt-archive/master/release-candidates/hmt-current.cex"
corpus = fromcex(url, CitableTextCorpus, UrlReader)
normalizededition = filter(psg -> endswith(workcomponent(psg.urn), "normalized"), corpus.passages)
#external_stylesheets = ["https://codepen.io/chriddyp/pen/bWLwgP.css"]
#app = dash(external_stylesheets=external_stylesheets)

#function findtext(psgs::Vector{CitablePassages, q::AbstractString})
#    filter(p -> contains(p.text, q), psgs) |> length
#end
app = dash()

app.layout = html_div() do
    html_h1("HMT project: alphabetic search"),
    html_div() do
            "Manuscripts to include:",
            html_div(style=Dict("max-width" => "200px"),
                dcc_dropdown(
                    id = "ms",
                    options = [
                        (label = "All manuscripts", value = "all"),
                        (label = "Venetus A", value = "va")
                    ],
                    value = "all"
                )
            )
    end,
    html_div() do
        "Texts to include:",
        html_div(style=Dict("max-width" => "200px"),
            dcc_dropdown(
                id = "txt",
                options = [
                    (label = "All texts", value = "all"),
                    (label = "Iliad", value = "iliad"),
                    (label = "scholia", value = "scholia"),
                ],
                value = "scholia"
            )
        )
end,
  
    html_div(
        children = [
        "Search for: ",
        dcc_input(id = "query", value = "", type = "text")
        ]
    ),
    html_br(),
    html_div(id = "results")
end

callback!(app, 
    Output("results", "children"), 
    Input("query", "value"),
    Input("ms", "value"),
    Input("txt", "value"),
    ) do query_value, ms_value, txt_value


    selected_mss = if ms_value == "all"
		normalizededition
	elseif ms_value == "va"
		msascholia = filter(p -> startswith(workcomponent(p.urn), "tlg5026.msA"), normalizededition)
		msailiad = filter(p -> startswith(workcomponent(p.urn), "tlg0012.tlg001.msA"), normalizededition)
		vcat(msascholia, msailiad)
	end
    
    selected_passages = if txt_value == "all"
		selected_mss
	elseif txt_value == "iliad"
		iliadurn = CtsUrn("urn:cts:greekLit:tlg0012.tlg001:")
		filter(p -> urncontains(iliadurn, p.urn), selected_mss)
	elseif txt_value == "scholia"
		scholiaurn = CtsUrn("urn:cts:greekLit:tlg5026:")
		filter(p -> urncontains(scholiaurn, p.urn), selected_mss)
	end
   
    if isnothing(selected_passages)
        ""
    elseif length(query_value) > 2
        hits = filter(p -> contains(lowercase(p.text), lowercase(query_value)), selected_passages) 
     
        summary = "Results of search for: $(query_value) : $(length(selected_passages)) passages in $(ms_value). $(length(hits)) matches"
        rslts = []
        for hit in hits
            push!(rslts, "1. " * hit.text)
        end
        dcc_markdown(summary * "\n\n" * join(rslts, "\n"))
    else
        "Selected corpus has $(length(selected_passages)) passages. "

    end
end

run_server(app, "0.0.0.0", debug=true)
