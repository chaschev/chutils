package com.chaschev.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import org.w3c.dom.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

/**
 * User: achaschev
 * Date: 8/18/13
 * Time: 9:13 PM
 */
public class JsonXML {

    public static final JsonFactory JSON_FACTORY = new JsonFactory();

    public static String domToJson(Node in) throws IOException {
        final StringWriter sw = new StringWriter(512);
        final JsonGenerator gen = JSON_FACTORY.createGenerator(sw);
        gen.useDefaultPrettyPrinter();
        gen.writeStartObject();
        domToJson(in, gen);
        gen.writeEndObject();
        gen.flush();
        sw.flush();
        return sw.toString();
    }

    public static void domToJson(Node in, JsonGenerator out) throws IOException {
        domToJson(in, out, 1);
        out.flush();
    }

    protected static void domToJson(Node in, JsonGenerator out, int index) throws IOException {
//        out.writeFieldName();
        out.writeObjectFieldStart(in.getNodeName() + "@" + index);

        final NamedNodeMap attrs = in.getAttributes();

        int n;

        if (attrs != null) {
            n = attrs.getLength();

            for (int i = 0; i < n; i++) {
                final Node item = attrs.item(i);
                final String value = item.getNodeValue();

                out.writeStringField("@" + item.getNodeName(), value);
            }
        }

        final NodeList childNodes = in.getChildNodes();

        n = childNodes.getLength();

        for (int i = 0, jsonIndex = 1; i < n; i++, jsonIndex++) {
            final Node item = childNodes.item(i);

            if (item instanceof Element) {
                domToJson(item, out, jsonIndex);
            }else if (item instanceof Text) {
                Text text = (Text) item;
                final String wholeText = text.getWholeText();
                if(isBlank(wholeText)){
                    jsonIndex--;
                }else{
                    out.writeStringField("/text@" + jsonIndex, wholeText);
                }
            }else
            if (item instanceof CDATASection) {
                CDATASection cdata = (CDATASection) item;
                out.writeStringField("/cdata@" + jsonIndex, cdata.getWholeText());
            }else{
                throw new UnsupportedOperationException("todo: " + in.getNodeName() +", class: " + item.getClass().getSimpleName());
            }
        }

        out.writeEndObject();
    }

    private static boolean isBlank(String s) {
        final int l = s.length();

        for (int i = 0; i < l; i++) {
            final char c = s.charAt(i);

            switch (c){
                case ' ':
                case '\n':
                case '\t':
                case '\r':
                case ((char)160):
                    continue;
                default:
                    return false;
            }
        }

        return true;
    }


    public static DomBuilder jsonToDom(JsonNode node) throws IOException {
        final DomBuilder b = new DomBuilder();

        jsonToDom(node, b.root(), b);

        return b;
    }

    public static void jsonToDom(JsonNode node, Node container, DomBuilder b) throws IOException {
//        if(node.getNodeType() == JsonNodeType.OBJECT){

        final Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> e = fields.next();
            final String key = e.getKey();

            if (key.startsWith("@")) {
                //this stupid cast is because Document is a Node
                ((Element)container).setAttribute(key.substring(1), e.getValue().textValue());
            } else if (key.startsWith("/")) {
                final String directive = getName(1, key);

                if ("text".equals(directive)) {
                    b.addTextNode(container, e.getValue().textValue());
                } else {
                    throw new UnsupportedOperationException("unknown directive: " + directive);
                }
            } else {
                //else this is an element
                final Element element = b.newElement(container, getName(key));
                jsonToDom(e.getValue(), element, b);
            }
        }

    }

    private static String getName(String key) {
        return getName(0, key);
    }

    private static String getName(int beginIndex, String key) {
        int i = key.lastIndexOf('@');

        if(i == -1) i = key.length();

        return key.substring(beginIndex, i);
    }
}
