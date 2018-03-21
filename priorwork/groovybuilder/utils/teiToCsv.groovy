
/*
Read all tables in a /TEI/text/body/div of a TEI P5 document,
and write out data in CSV format.  Rows of @role = 'label' are
omitted.

Usage: groovy teiToCsv.groovy [-n] <FILENAME> 
FILENAME is required.  -n means autonumber the resulting rows.
*/


def tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")
def f 
boolean autonum = false
if (args.size() == 2) {
    f = new File(args[1])
    autonum = true
} else {
    f = new File(args[0])
}


def cnt = 0
def root = new XmlParser().parse(f)
root[tei.text][tei.body][tei.div][tei.table][tei.row].each { r ->

    if (r.'@role' == "label") {
    } else {
        if (autonum) {cnt++; print "${cnt}\t"}

        r[tei.cell].eachWithIndex { c, i ->
            def txt = c.text()
            txt = txt.replaceAll(/[\n\t]+/,' ')
            txt = txt.replaceAll(/[\s]+/,' ')
            txt = txt.replace(/^[\s]+/,'')
            txt = txt.replace(/[\s]+$/,'')
            if (i > 0) { print "\t"}
            print txt
        }
    }
    print "\n"
    
}
