package org.jinform.core.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * @author David Dossot
 */
public abstract class XmlUtil {
  private static final Node[] EMPTY_NODE_ARRAY = new Node[0];

  private XmlUtil() {
    // NOOP
  }

  final static String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

  final static String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

  final static String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

  private static boolean schemaValidating = false;

  static {
    try {
      DocumentBuilderFactory.newInstance().setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
      schemaValidating = true;
    }
    catch (Exception e) { /* JAXP is not schema validating */
    }
  }

  /**
   * @return Returns true if the JAXP supports schema validation.
   */
  public static boolean isSchemaValidating() {
    return schemaValidating;
  }

  private static ThreadLocal identityTransformer = new ThreadLocal() {
    protected synchronized Object initialValue() {
      try {
        return TransformerFactory.newInstance().newTransformer();
      }
      catch (TransformerConfigurationException e) {
        throw new RuntimeException(e);
      }
      catch (TransformerFactoryConfigurationError e) {
        throw new RuntimeException(e);
      }
    }
  };

  public static void writeTo(Node node, Writer writer) {
    Transformer t = (Transformer) identityTransformer.get();
    try {
      t.transform(new DOMSource(node), new StreamResult(writer));
    }
    catch (TransformerException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean isWellFormed(InputStream xml) {
    try {
      SAXParserFactory.newInstance().newSAXParser().parse(xml, new DefaultHandler());
      return true;
    }
    catch (ParserConfigurationException e) {
      // NOOP
    }
    catch (SAXException e) {
      // NOOP
    }
    catch (FactoryConfigurationError e) {
      // NOOP
    }
    catch (IOException e) {
      // NOOP
    }
    return false;
  }

  public static void validate(InputStream xml, InputStream schema, DefaultHandler dh) {
    try {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setValidating(true);
      spf.setNamespaceAware(true);
      SAXParser sp = spf.newSAXParser();
      sp.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
      sp.setProperty(JAXP_SCHEMA_SOURCE, schema);
      sp.parse(xml, dh);
    }
    catch (ParserConfigurationException e) {
      throw new XmlException(e);
    }
    catch (SAXException e) {
      // we do not stop on major errors
      // and leave the fatal error to detected from the error handler
      throw new XmlException(e);
    }
    catch (IOException e) {
      throw new XmlException(e);
    }
  }

  public static Document buildNewDocument(InputSource is) {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      if (is == null) return dbf.newDocumentBuilder().newDocument();
      else return dbf.newDocumentBuilder().parse(is);
    }
    catch (SAXException e) {
      throw new XmlException(e);
    }
    catch (IOException e) {
      throw new XmlException(e);
    }
    catch (ParserConfigurationException e) {
      throw new XmlException(e);
    }
  }

  public static Document buildNewDocument(InputStream is) {
    return buildNewDocument(new InputSource(is));
  }

  public static Document buildNewDocument(byte[] bytes) {
    return buildNewDocument(new ByteArrayInputStream(bytes));
  }

  public static Document buildNewDocument() {
    return buildNewDocument((InputSource) null);
  }

  public static Document buildNewDocument(Source source) {
    Transformer t = (Transformer) identityTransformer.get();
    Document result = buildNewDocument();
    try {
      t.transform(source, new DOMResult(result));
      return result;
    }
    catch (TransformerException te) {
      throw new XmlException(te);
    }
  }

  public static String getString(Document doc, String xPath) {
    return getString(doc, null, null, xPath);
  }

  public static String getString(Document doc, String prefix, String uri, String xPath) {
    try {
      List results = runXPath(doc, prefix, uri, xPath);
      if (results.size() == 0) return null;
      else return ((Node) results.get(0)).getNodeValue();
    }
    catch (JaxenException e) {
      throw new XmlException(e);
    }
  }

  public static Node[] select(Document doc, String xPath) {
    return select(doc, null, null, xPath);
  }

  public static Node[] select(Document doc, String prefix, String uri, String xPath) {
    try {
      return (Node[]) runXPath(doc, prefix, uri, xPath).toArray(EMPTY_NODE_ARRAY);
    }
    catch (JaxenException e) {
      throw new XmlException(e);
    }
  }

  public static String getProcessingInstructionAttribute(Document doc, String target, String attributeName) {
    try {
      List results = runXPath(doc, null, null, "/processing-instruction('" + target + "')");
      if (results.size() == 0) return null;
      return XmlUtil.buildNewDocument(("<pi " + ((ProcessingInstruction) results.get(0)).getData() + "/>").getBytes()).getDocumentElement()
        .getAttribute(attributeName);
    }
    catch (JaxenException e) {
      throw new XmlException(e);
    }
  }

  //when:JDK5 use javax.xml.xpath 
  private static List runXPath(Document doc, String prefix, String uri, String xPath) throws JaxenException {
    XPath path = new DOMXPath(xPath);
    if ((prefix != null) && (uri != null)) path.addNamespace(prefix, uri);
    List results = path.selectNodes(doc);
    return results;
  }

  public static Map gatherNamespaces(InputSource is) {
    NamespaceGatherer ng = new NamespaceGatherer();
    SAXParserFactory spf = SAXParserFactory.newInstance();
    spf.setNamespaceAware(true);
    try {
      spf.newSAXParser().parse(is, ng);
    }
    catch (SAXException e) {
      throw new XmlException(e);
    }
    catch (IOException e) {
      throw new XmlException(e);
    }
    catch (ParserConfigurationException e) {
      throw new XmlException(e);
    }
    return Collections.unmodifiableMap(ng.getNamespaces());
  }

  public static XMLFilter newRootElementNamespaceAdder(Map namespaces) {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    spf.setNamespaceAware(false);
    try {
      return new RootNamespaceTweaker(spf.newSAXParser().getXMLReader(), namespaces);
    }
    catch (SAXException e) {
      throw new XmlException(e);
    }
    catch (ParserConfigurationException e) {
      throw new XmlException(e);
    }
  }

  /**
   * A handler that gathers all the declared namespaces in an XML document. If a namespace is declared several times, the last one is
   * possible the one that will be retained. The SAX parser using this filter <b>MUST</b> be namespace aware.
   */
  private static class NamespaceGatherer extends DefaultHandler {
    private final Map namespaces = new HashMap();

    public Map getNamespaces() {
      return this.namespaces;
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
      namespaces.put(prefix, uri);
      super.startPrefixMapping(prefix, uri);
    }
  }

  /**
   * A filter that adds the provided namespaces to the root element if they are not already declared. The SAX parser using this filter must
   * <b>NOT</b> be namespace aware.
   */
  private static class RootNamespaceTweaker extends XMLFilterImpl {
    private final Map namespacesToAdd;

    private boolean root = true;

    public RootNamespaceTweaker(XMLReader parent, Map namespacesToAdd) {
      super(parent);
      this.namespacesToAdd = namespacesToAdd;
    }

    public void startElement(String uri, String localName, String qName, Attributes originalAttributes) throws SAXException {
      if (root) {
        root = false;
        AttributesImpl newAttributes = new AttributesImpl(originalAttributes);

        for (Iterator namespaceIterator = namespacesToAdd.entrySet().iterator(); namespaceIterator.hasNext();) {
          Map.Entry namespaceEntry = (Entry) namespaceIterator.next();
          String newQName = "xmlns:" + namespaceEntry.getKey();
          if (newAttributes.getIndex(newQName) == -1)
            newAttributes.addAttribute("", "", newQName, "CDATA", namespaceEntry.getValue().toString());
        }

        super.startElement(uri, localName, qName, newAttributes);
      }
      else {
        super.startElement(uri, localName, qName, originalAttributes);
      }
    }
  }

}