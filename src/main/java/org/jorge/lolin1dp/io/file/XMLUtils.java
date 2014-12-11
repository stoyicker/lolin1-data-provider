package org.jorge.lolin1dp.io.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Methods for performing data CRUD on XML files. The performable actions will
 * not go deeper than one level (apart from root, obviously).
 *
 * @author See <a href="the Google Code page of the easy-utils
 * library">http://code.google.com/p/easy-utils/</a> for collaborators and other
 * information.
 */
public abstract class XMLUtils {

    /**
     * Adds a new node to a file.
     *
     * @param nodeType {@link String} The type of the element to add.
     * @param idField {@link String} The name of the field used to identify this
     * node.
     * @param nodeID {@link String} The identifier for this node, so its data
     * can be later retrieved and modified.
     * @param destFile {@link File} The file where the node must be added.
     * @param attributes {@link ArrayList} of array of String. The arrays must
     * be bidimensional (first index must contain attribute name, second one
     * attribute value). Otherwise, an error will be thrown. However, if
     * <value>null</value>, it is ignored.
     */
    public static void addNode(String nodeType, String idField, String nodeID, File destFile, ArrayList<String[]> attributes) {
        if (attributes != null) {
            for (Iterator<String[]> it = attributes.iterator(); it.hasNext();) {
                if (it.next().length != 2) {
                    throw new IllegalArgumentException("Invalid attribute combination");
                }
            }
        }
        /*
         * XML DATA CREATION - BEGINNING
         */
        DocumentBuilder docBuilder;
        Document doc;
        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuilder.parse(destFile);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            return;
        }

        Node index = doc.getFirstChild(), newElement = doc.createElement(nodeType);
        NamedNodeMap elementAttributes = newElement.getAttributes();

        Attr attrID = doc.createAttribute(idField);
        attrID.setValue(nodeID);
        elementAttributes.setNamedItem(attrID);

        if (attributes != null) {
            for (Iterator<String[]> it = attributes.iterator(); it.hasNext();) {
                String[] x = it.next();
                Attr currAttr = doc.createAttribute(x[0]);
                currAttr.setValue(x[1]);
                elementAttributes.setNamedItem(currAttr);
            }
        }

        index.appendChild(newElement);
        /*
         * XML DATA CREATION - END
         */

        /*
         * XML DATA DUMP - BEGINNING
         */
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException ex) {
            return;
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        try {
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            return;
        }

        String xmlString = result.getWriter().toString();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(destFile))) {
            bufferedWriter.write(xmlString);
        } catch (IOException ex) {
        }
        /*
         * XML DATA DUMP - END
         */
    }

    /**
     * Parses a XML file and retrieves the identifier attribute of all nodes of
     * a given type.
     *
     * @param type {@link String} The type identifier.
     * @param identifierAttribute {@link String} The name of the attribute that
     * identifies the node.
     * @param destFile {@link File} The file containing the data where the
     * information must be retrieved from.
     * @return The value of the requested attribute for the requested node.
     */
    public static ArrayList<String> getAllOfType(String type, String identifierAttribute, File destFile) {
        ArrayList<String> ret = new ArrayList<>();
        DocumentBuilder docBuilder;
        Document doc;

        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuilder.parse(destFile);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            return null;
        }

        NodeList parent = doc.getElementsByTagName(type);

        for (int i = 0; i < parent.getLength(); i++) {
            ret.add(((Element) parent.item(i)).getAttribute(identifierAttribute));
        }

        return ret;
    }

    /**
     * Parses a XML file and retrieves the requested attribute of a certain XML
     * node.
     *
     * @param type {@link String} The type identifier of the XML node.
     * @param identifierAttribute {@link String} The name of the attribute that
     * identifies the node.
     * @param identifierValue {@link String} The value that the identifier
     * attribute will have in the requested node so it can be found.
     * @param attributeName {@link String} The name of the attribute to
     * retrieve.
     * @param destFile {@link File} The file containing the data where the
     * information must be retrieved from.
     * @return The value of the requested attribute for the requested node.
     */
    public static String getAttribute(String type, String identifierAttribute, String identifierValue, String attributeName, File destFile) {
        DocumentBuilder docBuilder;
        Document doc;

        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuilder.parse(destFile);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            return null;
        }

        return XMLUtils.getAttribute(type, identifierAttribute, identifierValue, attributeName, doc);
    }

    /**
     * Parses a XML file and retrieves the requested attribute of a certain XML
     * node.
     *
     * @param type {@link String} The type identifier of the XML node.
     * @param identifierAttribute {@link String} The name of the attribute that
     * identifies the node.
     * @param identifierValue {@link String} The value that the identifier
     * attribute will have in the requested node so it can be found.
     * @param attributeName {@link String} The name of the attribute to
     * retrieve.
     * @param doc {@link Document} The document containing the data where the
     * information must be retrieved from.
     * @return The value of the requested attribute for the requested node.
     */
    public static String getAttribute(String type, String identifierAttribute, String identifierValue, String attributeName, Document doc) {
        NodeList parent = doc.getElementsByTagName(type);

        for (int i = 0; i < parent.getLength(); i++) {
            if (((Element) parent.item(i)).getAttribute(identifierAttribute).matches(identifierValue)) {
                return ((Element) parent.item(i)).getAttribute(attributeName);
            }
        }

        return null;
    }

    /**
     * Parses an inner XML file and retrieves the requested attribute of a
     * certain XML node.
     *
     * @param type {@link String} The type identifier of the XML node.
     * @param identifierAttribute {@link String} The name of the attribute that
     * identifies the node.
     * @param identifierValue {@link String} The value that the identifier
     * attribute will have in the requested node so it can be found.
     * @param attributeName {@link String} The name of the attribute to
     * retrieve.
     * @param URI {@link String} The absolute URI to the inner XML file.
     * @return The value of the requested attribute for the requested node.
     */
    public static String getAttribute(String type, String identifierAttribute, String identifierValue, String attributeName, String URI) {
        DocumentBuilder docBuilder;
        Document doc;

        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuilder.parse(XMLUtils.class.getResource(URI).toURI().toString());
        } catch (SAXException | IOException | URISyntaxException | ParserConfigurationException ex) {
            return null;
        }

        return XMLUtils.getAttribute(type, identifierAttribute, identifierValue, attributeName, doc);
    }

    /**
     * Updates an already existing node in a file. If the node doesn't exist, no
     * operation is performed.
     *
     * @param nodeType {@link String} The type of the element to add.
     * @param IDField {@link String} The name of the field used to identify this
     * node.
     * @param nodeID {@link String} The value of the identifier field for this
     * node.
     * @param destFile {@link File} The file where the node must be added.
     * @param attributes {@link ArrayList} of array of String. The arrays must
     * be bidimensional (first index must contain attribute name, second one
     * attribute value). Otherwise, an error will be thrown. However, if
     * <value>null</value>, it is ignored.
     */
    public static void updateNodeInfo(String nodeType, String IDField, String nodeID, File destFile, ArrayList<String[]> attributes) {
        if (attributes != null) {
            for (Iterator<String[]> it = attributes.iterator(); it.hasNext();) {
                if (it.next().length != 2) {
                    throw new IllegalArgumentException("Invalid attribute combination");
                }
            }
        }
        /*
         * XML DATA EDITION - BEGINNING
         */
        DocumentBuilder docBuilder;
        Document doc;

        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuilder.parse(destFile);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            return;
        }

        NodeList elements = doc.getElementsByTagName(nodeType);
        Node node = null;

        for (int i = 0; i < elements.getLength(); i++) {
            if (IDField.matches(((Element) elements.item(i)).getAttribute(nodeID))) {
                node = elements.item(i);
                break;
            }
        }
        NamedNodeMap map = node.getAttributes();

        if (attributes != null) {
            for (Iterator<String[]> it = attributes.iterator(); it.hasNext();) {
                String[] x = it.next();
                map.getNamedItem(x[0]).setTextContent(x[1]);
            }
        }
        /*
         * XML DATA EDITION - END
         */

        /*
         * XML DATA DUMP - BEGINNING
         */
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException ex) {
            return;
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        try {
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            return;
        }

        String xmlString = result.getWriter().toString();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(destFile))) {
            bufferedWriter.write(xmlString);
        } catch (IOException ex) {
        }
        /*
         * XML DATA DUMP - END
         */
    }

    /**
     * Removes a node element from a file.
     *
     * @param nodeType {@link String} The name of the media element.
     * @param IDField {@link String} The name of the field used to identify this
     * node.
     * @param nodeID {@link String} The value of the identifier field for this
     * node.
     * @param destFile {@link File} The file where the node must be added.
     */
    public static void removeElementFromIndex(String nodeType, String IDField, String nodeID, File destFile) {
        /*
         * XML DATA DELETION - BEGINNING
         */
        DocumentBuilder docBuilder;
        Document doc;

        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuilder.parse(destFile);
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            return;
        }

        NodeList elements = doc.getElementsByTagName(nodeType);
        Node currentElementNode;

        for (int i = 0; i < elements.getLength(); i++) {
            if (nodeID.matches(((Element) elements.item(i)).getAttribute(IDField))) {
                currentElementNode = elements.item(i);
                currentElementNode.getParentNode().removeChild(currentElementNode);
                break;
            }
        }
        /*
         * XML DATA DELETION - END
         */

        /*
         * XML DATA DUMP - BEGINNING
         */
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException ex) {
            return;
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        try {
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            return;
        }

        String xmlString = result.getWriter().toString();
        xmlString = xmlString.replaceAll("(?m)^[ \t]*\r?\n", ""); //Remove generetad blank line.
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(destFile))) {
            bufferedWriter.write(xmlString);
        } catch (IOException ex) {
        }
        /*
         * XML DATA DUMP - END
         */
    }
}
