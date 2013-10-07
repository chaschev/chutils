package chaschev.util;

import org.w3c.dom.*;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
* User: chaschev
* Date: 6/9/13
*/
public class DomBuilder {
    private final DocumentBuilderFactory builderFactory;
    public final Document document;
    private final DocumentBuilder builder;

    public DomBuilder(DocumentBuilderFactory builderFactory) {
        this.builderFactory = builderFactory;
        try {
            builder = builderFactory.newDocumentBuilder();
            document = builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public Node root(){
        final Element el = document.getDocumentElement();

        if(el == null){
            return document;
        }

        return el;
    }

    public Element child(Element parent, String name){
        return DomUtils.child(parent, name);
    }

    public DomBuilder(Document document) {
        this.document = document;
        builder = null;
        builderFactory = null;
    }

    public static String documentToString(Document doc){
        org.w3c.dom.ls.DOMImplementationLS domImplementation = (org.w3c.dom.ls.DOMImplementationLS) doc.getImplementation();
        org.w3c.dom.ls.LSSerializer lsSerializer = domImplementation.createLSSerializer();
        return lsSerializer.writeToString(doc);
    }

    public DomBuilder() {
        builderFactory = DocumentBuilderFactory.newInstance();

        builderFactory.setValidating(false);
        builderFactory.setNamespaceAware(false);
        builderFactory.setCoalescing(false);

        try {
            builder = builderFactory.newDocumentBuilder();
            document = builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public Element newElement(Node parent, String childName){
        final Element element = document.createElement(childName);
        parent.appendChild(element);
        return element;
    }

    public Text addTextNode(Node parent, String text){
        final Text textNode = document.createTextNode(text);
        parent.appendChild(textNode);
        return textNode;
    }

    public CDATASection addCDATA(Node parent, String text){
        final CDATASection section = document.createCDATASection(text);
        parent.appendChild(section);
        return section;
    }

    public String asString() {
        return documentToString(document);
    }
}
