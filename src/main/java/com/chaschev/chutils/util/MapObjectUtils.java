package com.chaschev.chutils.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.*;

/**
 * User: chaschev
 * Date: 5/15/13
 */

/**
 * <p>Provides utility methods over map objects</p>
 *
 * <p>Map object is a Map of Maps or Lists, typically an in-memory representation for a JSON object.</p>
 */
public class MapObjectUtils {
    public static class Objects{
        public static boolean equals(Object a, Object b) {
            return (a == b) || (a != null && a.equals(b));
        }
    }

    public static void clean(Collection l){
        cleanCollection(l);
    }

    public static Map pathToMap(Map<String, Object> mapObject, String path){
        if(mapObject == null) return null;

        final String[] split = path.split("/");

        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            final Object o = mapObject.get(s);

            if(!(o instanceof Map)) {
                if(o instanceof Collection){
                    throw new IllegalStateException("expected map, found collection");
                }
                return null;
            }

            mapObject = (Map<String, Object>) o;
        }

        return mapObject;
    }

    public static Object pathToItem(Map<String, Object> mapObject, String path, int n){
        return pathToList(mapObject, path).get(n);
    }

    public static List<Object> pathToList(Map<String, Object> mapObject, String path){
        return (List<Object>) pathToCollection(mapObject, path);
    }

    public static Collection pathToCollection(Map<String, Object> mapObject, String path){
        final String[] split = path.split("/");

        final int length = split.length;
        for (int i = 0; i < length; i++) {
            String s = split[i];
            final Object o = mapObject.get(s);

            if(i == length - 1){
                if(!(o instanceof Collection)) {
                    if (o instanceof Map) {
                        throw new IllegalStateException("collection expected, but found map");

                    }
                    return null;
                }

                return (Collection) o;
            }

            if(!(o instanceof Map)) return null;

            mapObject = (Map<String, Object>) o;
        }

        return null;
    }

    public static Map<String, Object> having(Collection collection, String field, Object value){
        for (Object o : collection) {
            if (o instanceof Map) {
                Map map = (Map) o;
                if(Objects.equals(value, map.get(field))){
                    return map;
                }
            }
        }

        return null;
    }

    public static Map<String, Object> having(
        Collection collection,
        String field1, Object value1,
        String field2, Object value2
    ) {
        for (Object o : collection) {
            if (o instanceof Map) {
                Map map = (Map) o;
                if (Objects.equals(value1, map.get(field1)) &&
                    Objects.equals(value2, map.get(field2))) {
                    return map;
                }
            }
        }

        return null;
    }

    public static Map<String, Object> fromJSON(Reader is){
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(is,
                    new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw Exceptions.runtime(e);
        }
    }

    public static Map<String, Object> fromJSON(InputStream is){
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(is,
                new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw Exceptions.runtime(e);
        }
    }

    public static Map<String, Object> fromJSON(String content){
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(content,
                new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw Exceptions.runtime(e);
        }
    }

    public static String toJSON(Map<String, Object> map){
        return toJSON((Object) map);
    }

    public static String toJSON(Object pojo){
        try {
            ObjectMapper mapper = new ObjectMapper();

            StringWriter sw = new StringWriter(50 * 1024);

            mapper.writeValue(sw, pojo);

            return sw.toString();
        } catch (IOException e) {
            throw Exceptions.runtime(e);
        }
    }


    protected static void cleanCollection(Collection collection)
    {
        final Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Object o = iterator.next();

            if (o == null) {
                iterator.remove();
            }else
            if (o instanceof Collection) {
                Collection oColl = (Collection) o;

                if (oColl.isEmpty()) {
                    iterator.remove();
                } else {
                    clean(oColl);
                    if(oColl.isEmpty()){
                        iterator.remove();
                    }
                }
            } else if (o instanceof Map) {
                Map oMap = (Map) o;

                if (oMap.isEmpty()) {
                    iterator.remove();
                } else {
                    clean(oMap);
                    if(oMap.isEmpty()){
                        iterator.remove();
                    }
                }
            } else if (o instanceof String) {
                String oString = (String) o;

                if (oString.isEmpty()) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Remove empty items (empty lists, maps, strings) from the MapObject.
     */
    public static void clean(Map mapObject){
        clean(mapObject.values());
    }

    public static String toXML(Map object){
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        toXML(object, baos);
        return baos.toString();
    }

    public static void toXML(Map object, OutputStream stream){
        try {
            final XMLOutputFactory factory = XMLOutputFactory.newFactory();
            final XMLStreamWriter writer = factory.createXMLStreamWriter(stream);

            new XmlStreamCreator(writer).toXMLRec(object, writer);

            writer.flush();
            stream.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Document toDomXML(Map object){
        try {
            final DomBuilder b = new DomBuilder();
            toDomXMLRec(object, b.document, b);
            return b.document;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void toDomXMLRec(Map object, Node parent, DomBuilder b) {
        final Set<Map.Entry> set = object.entrySet();

        for (Map.Entry entry : set) {
            final Object key = entry.getKey();
            final Object value = entry.getValue();

            if (isSimpleValue(value)) {
                String s = value.toString();
                ((Element)parent).setAttribute(key.toString(), s);
            }else
            if (value instanceof Collection) {
                toDomXMLRec((Collection) value, b.newElement(parent, key.toString()), b);
            }else if (value instanceof Map) {
                toDomXMLRec((Map) value, b.newElement(parent, key.toString()), b);
            }
        }
    }

    private static boolean isSimpleValue(Object value) {
        return value instanceof String || value instanceof Number || value instanceof Date;
    }

    private static void toDomXMLRec(Collection list, Element parent, DomBuilder b){
        for (Object o : list) {
            final Element item = b.newElement(parent, "item");

            if (isSimpleValue(o)) {
                b.addTextNode(item, o.toString());
            }else
            if(o instanceof Collection){
                toDomXMLRec((Collection) o, item, b);
            }
            else
            if(o instanceof Map){
                toDomXMLRec((Map) o, item, b);
            }
        }
    }

    private static class XmlStreamCreator {
        XMLStreamWriter writer;
        List<Map.Entry> attributes = new ArrayList<Map.Entry>();

        private XmlStreamCreator(XMLStreamWriter writer) {
            this.writer = writer;
        }

        private void toXMLRec(Collection list, XMLStreamWriter writer) throws Exception{
            for (Object o : list) {
                writer.writeStartElement("item");
                if (isSimpleValue(o)) {
                    writer.writeCharacters(o.toString());
                }else
                if(o instanceof Collection){
                    toXMLRec((Collection)o, writer);
                }
                else
                if(o instanceof Map){
                    toXMLRec((Map)o, writer);
                }
                writer.writeEndElement();
            }
        }

        private void toXMLRec(Map object, XMLStreamWriter writer) throws Exception{
            final Set<Map.Entry> set = object.entrySet();

            attributes.clear();

            for (Map.Entry entry : set) {
                if (isSimpleValue(entry.getValue())) {
                    attributes.add(entry);
                }
            }

            for (Map.Entry attribute : attributes) {
                writer.writeAttribute(attribute.getKey().toString(), attribute.getValue().toString());
            }

            for (Map.Entry entry : set) {
                final Object key = entry.getKey();
                final Object value = entry.getValue();

                if (value instanceof Collection) {
                    writer.writeStartElement(key.toString());
                    toXMLRec((Collection) value, writer);
                    writer.writeEndElement();
                }else if (value instanceof Map) {
                    Map map = (Map) value;
                    writer.writeStartElement(key.toString());
                    toXMLRec(map, writer);
                    writer.writeEndElement();
                }
            }

        }
    }


}
