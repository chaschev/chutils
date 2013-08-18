package com.chaschev.chutils.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.StringWriter;

/**
 * User: achaschev
 * Date: 8/18/13
 * Time: 10:03 PM
 */
public class JsonXMLTest {
    @Test
    public void testDomToJson() throws Exception {
        final String s = JsonXML.domToJson(
            DomUtils.parseXml("" +
                "<a x=\"z\">             \n" +
                "    <b x=\"z\">c</b>    \n" +
                "    crappy text    \n" +
                "    <b>d</b>          \n" +
                "    <b v=\"mmm\" p=\"popo\"><e keke=\"popo and never ask again\">f</e></b>    \n" +
                "    <x></x>           \n" +
                "    more text           \n" +
                "    <b>m</b>          \n" +
                "</a>                  "
            ).getDocumentElement());

        System.out.println(s);

        JsonFactory fac = new JsonFactory();
        final JsonParser parser = fac.createParser(s);
        ObjectMapper mapper = new ObjectMapper();
        final JsonNode jsonNode = mapper.readTree(s);

        System.out.println(JsonXML.jsonToDom(jsonNode).asString());
    }
}
