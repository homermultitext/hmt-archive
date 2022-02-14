/*
Make sure output is UTF8.

Can assume that source document is in TEI namespace.

*/
 

String collectAttrs(groovy.util.Node n) {
    StringBuffer attrStr = new StringBuffer()
    n.attributes().keySet().each { a ->
        if (a instanceof groovy.xml.QName) {
            attrStr.append(" ${a.getPrefix()}:${a.getLocalPart()}='" + n.attribute(a) + "' ")
        } else {
            attrStr.append(" ${a}='" + n.attribute(a) + "' ")
        }
    }
        return attrStr.toString()
}

String openElement(groovy.util.Node n, boolean isRoot) {
    StringBuffer tag = new StringBuffer()
    if (isRoot) {
        tag.append("<?xml version='1.0' encoding='utf-8'?>\n<?xml-stylesheet  type='text/css' href='scholia-css/tei-scholia.css' title='TEI style simple scholia' alternate='no'?>\n")
    }

    if (n.name().getPrefix().size() > 0) {
        tag.append("<${n.name().getPrefix()}:${n.name().getLocalPart()}")
    } else {
        tag.append("<${n.name().getLocalPart()}")
    }

    if (isRoot) {
        tag.append (" xmlns='http://www.tei-c.org/ns/1.0'   xmlns:tei='http://www.tei-c.org/ns/1.0' ")
    }
    tag.append(collectAttrs(n))
    tag.append (">")
    return tag.toString()
}

String closeElement(groovy.util.Node n) {
    if (n.name() instanceof groovy.xml.QName)  {
        if (n.name().getPrefix().size() > 0) {
            return "</${n.name().getPrefix()}:${n.name().getLocalPart()}>"
        } else {
            return "</${n.name().getLocalPart()}>"
        }
    } else {
        return "</" + n.name() + ">\n"
    }
}




String convertReff(Object n, String allText, boolean isRoot) { 


    String urnPrefix = "urn:cts:greekLit:tlg0012.tlg001.msA:"
    groovy.xml.Namespace tei = new groovy.xml.Namespace("http://www.tei-c.org/ns/1.0")

    boolean continueRecursion = true
    if (n.getClass().getName() == "java.lang.String") {
        allText = allText + n
    } else {

        // All TEI, so name() always returns a QName:
        String localName = n.name().getLocalPart()
        if (localName == "div") {
            if (n.'@type' == "ref") {
                continueRecursion = false
                n[tei.p].each { para ->
                    def pval = para.text()
                    String urnStr = "${urnPrefix}${pval}"
                    allText = allText + "<tei:div type='ref'>\n<p>${urnStr}</p>\n</tei:div>\n"
                }
            } else {
              continueRecursion = true  
            }

        } 

        if (continueRecursion) {
            allText = allText + openElement(n, isRoot)

            n.children().each { child ->
                allText = convertReff(child, allText,false)
            }
            allText = allText + closeElement(n)
        }
    }
    return allText
}
 
 
File f = new File(args[0])
groovy.util.Node root = new XmlParser().parse(f)
String converted = convertReff(root, "",true)

println converted
