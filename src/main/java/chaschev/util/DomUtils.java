package chaschev.util;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static org.w3c.dom.Node.ELEMENT_NODE;

/**
 * User: achaschev
 * Date: 4/5/13
 * Time: 3:46 PM
 */
public class DomUtils {
    public static final Predicate<Node> TRUE = new Predicate<Node>() {
        public boolean apply(Node node) {
            return true;
        }
    };

    public interface Predicate<T> {

        /**
         * Applies this Predicate to the given object.
         *
         * @return the value of this Predicate when applied to input {@code t}
         */
        boolean apply(T t);
    }

    public static Element child(Element element, String tag) {
        final NodeList list = element.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            final Node item = list.item(i);

            if (item.getNodeType() == ELEMENT_NODE && hasLocalName(item, tag)) {

                return (Element) item;
            }
        }

        return null;
    }

    public static boolean hasLocalName(Node item, String tag) {
        final String nodeName = item.getNodeName();
        if (nodeName.endsWith(tag)) {
            int i = nodeName.length() - tag.length();
            return i == 0 || nodeName.charAt(i - 1) == ':';
        }

        return false;
    }

    public static Element childPath(Element element, String path) {
        final String[] elements = path.split("/");

        for (String s : elements) {
            element = child(element, s);
            if (element == null) return null;
        }

        return element;
    }

    public static Iterable<Element> children(final Element element, final String tag) {
        return children(element, new Predicate<Node>() {
            @Override
            public boolean apply(Node node) {
                return hasLocalName(node, tag);
            }
        });
    }

    public static Iterable<Element> children(final Element element) {
        return children(element, TRUE);
    }

    public static Iterable<Element> children(final Element element, final Predicate<Node> predicate) {
        return new Iterable<Element>() {
            @Override
            public Iterator<Element> iterator() {
                return new Iterator<Element>() {
                    final NodeList list = element.getChildNodes();

                    int i = 0;

                    @Override
                    public boolean hasNext() {
                        for (; i < list.getLength(); i++) {
                            final Node node = list.item(i);

                            if (node.getNodeType() == ELEMENT_NODE && predicate.apply(node)) {
                                break;
                            }
                        }
                        return i < list.getLength();
                    }

                    @Override
                    public Element next() {
                        return (Element) list.item(i++);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("todo .remove");
                    }
                };
            }
        };
    }

    /**
     * First tries to get the attribute value by doing an getAttributeNS on the element, if that gets an empty element it does a getAttribute without namespace.
     */
    public static String getAttribute(Element element, String attribute) {
        return element.getAttribute(attribute);
    }

    /**
     * Gets all descendant elements of the given parentElement with the given namespace and tagname and returns their text child as a list of String.
     */
    public static List<String> childrenText(Element parentElement, String tagname) {
        final Iterable<Element> children = children(parentElement, tagname);

        List<String> result = new ArrayList<String>();

        for (Element element : children) {
            result.add(elementText(element));
        }

        return result;
    }

    /**
     * The contents of all Text nodes that are children of the given parentElement.
     * The result is trim()-ed.
     * <p/>
     * The reason for this more complicated procedure instead of just returning the data of the firstChild is that
     * when the text is Chinese characters then on Android each Characater is represented in the DOM as
     * an individual Text node.
     *
     * @param parentElement
     * @return
     */
    public static String elementText(Element parentElement) {
        if (parentElement == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        NodeList childNodes = parentElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if ((node == null) ||
                (node.getNodeType() != Node.TEXT_NODE)) {
                continue;
            }
            result.append(((Text) node).getData());
        }
        return result.toString().trim();
    }

    public static String elementToString(Node n) {
        return elementToString(n, 0, 4);
    }

    public static String elementToString(Node node, int depth, int maxDepth) {
        return elementToString(node, new HashSet<Node>(), depth, maxDepth);
    }

    private static String elementToString(Node n, Set<Node> visitedSet, int depth, int maxDepth) {
        if (depth >= maxDepth) {
            return "depth limit\n";
        }

        if (visitedSet.contains(n)) {
            return "cycle to " + n.getLocalName() + "\n";
        } else {
            visitedSet.add(n);
        }

        String name = n.getNodeName();

        short type = n.getNodeType();

        if (Node.CDATA_SECTION_NODE == type) {
            return "<![CDATA[" + n.getNodeValue() + "]]&gt;";
        }

        if (name.startsWith("#")) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        sb.append('<').append(name);

        NamedNodeMap attrs = n.getAttributes();
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                Node attr = attrs.item(i);
                sb.append(' ').append(attr.getNodeName()).append("=\"").append(attr.getNodeValue()).append(
                    "\"");
            }
        }

        String textContent = null;
        NodeList children = n.getChildNodes();

        if (children.getLength() == 0) {
            if ((textContent = getTextContent(n)) != null && !"".equals(textContent)) {
                sb.append(textContent).append("</").append(name).append('>');

            } else {
                sb.append("/>").append('\n');
            }
        } else {
            sb.append('>').append('\n');
            boolean hasValidChildren = false;
            for (int i = 0; i < children.getLength(); i++) {
                String childToString = elementToString(children.item(i), visitedSet, depth + 1, maxDepth);
                if (!"".equals(childToString)) {
                    sb.append(childToString);
                    hasValidChildren = true;
                }
            }

            if (!hasValidChildren && ((textContent = getTextContent(n)) != null)) {
                sb.append(textContent);
            }

            sb.append("</").append(name).append('>');
        }

        return sb.toString();
    }


    public static String getTextContent(Node n) {
        if (n == null) return null;
        Node n1 = getChild(n, Node.TEXT_NODE);

        if (n1 == null) return null;

        String s1 = n1.getNodeValue();
        return s1.trim();
    }

    /**
     * Get the first direct child with a given type
     */
    public static Node getChild(Node parent, int type) {
        Node n = parent.getFirstChild();
        while (n != null && type != n.getNodeType()) {
            n = n.getNextSibling();
        }
        if (n == null) return null;
        return n;
    }

    public static Document parseXml(String s){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setIgnoringElementContentWhitespace(true);
            dbFactory.setNamespaceAware(false);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new ByteArrayInputStream(s.getBytes()));
            return doc;
        } catch (Exception e) {
            throw Exceptions.runtime(e);
        }
    }
}

