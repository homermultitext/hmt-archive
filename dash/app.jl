
using Dash
using CitableBase, CitableText, CitableCorpus
using Unicode

url = "https://raw.githubusercontent.com/homermultitext/hmt-archive/master/release-candidates/hmt-current.cex"
corpus = fromcex(url, CitableTextCorpus, UrlReader)
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

    html_div(
      
        children = [
            "Searching corpus with $(length(corpus.passages)) passages. "
        ]
    ),
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
    ) do query_value, ms_value
    if length(query_value) > 2
        count = filter(p -> contains(p.text, query_value), corpus.passages) 
        "Results of search for: $(query_value) : $(count) in $(ms_value)"
    else
        ""
    end
end

run_server(app, "0.0.0.0", debug=true)
