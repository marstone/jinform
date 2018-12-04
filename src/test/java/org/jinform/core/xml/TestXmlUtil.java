package org.jinform.core.xml;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;

import junit.framework.TestCase;

import org.w3c.dom.Document;

/**
 * @author David Dossot
 */
public class TestXmlUtil extends TestCase {
  private final static String ATTRIBUTE_VALUE = "attr";

  private final static String TEXT_VALUE = "txt";

  public final static String SIMPLE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo><bar>"
                                          + TEXT_VALUE
                                          + "<element/><element/></bar><baz attribute=\""
                                          + ATTRIBUTE_VALUE
                                          + "\"/></foo>";

  private final static String NS_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><my:foo xmlns:my=\"http://foo.bar\"><my:bar>"
                                       + TEXT_VALUE
                                       + "<my:element/><my:element/></my:bar><my:baz my:attribute=\""
                                       + ATTRIBUTE_VALUE
                                       + "\"/></my:foo>";

  private Document simpleXml;

  private Document nsXml;

  public void setUp() {
    simpleXml = XmlUtil.buildNewDocument(new ByteArrayInputStream(SIMPLE_XML.getBytes()));
    nsXml = XmlUtil.buildNewDocument(NS_XML.getBytes());
  }

  public void testAttributeSimpleXml() {
    assertEquals(ATTRIBUTE_VALUE, XmlUtil.getString(simpleXml, "/foo/baz/@attribute"));
  }

  public void testTextNodeSimpleXml() {
    assertEquals(TEXT_VALUE, XmlUtil.getString(simpleXml, "/foo/bar/text()"));
  }

  public void testNodeListSimpleXml() {
    assertEquals(2, XmlUtil.select(simpleXml, "/foo/bar/element").length);
  }

  public void testAttributeNsXml() {
    assertEquals(ATTRIBUTE_VALUE, XmlUtil.getString(nsXml, "my", "http://foo.bar", "/my:foo/my:baz/@my:attribute"));
  }

  public void testTextNodeNsXml() {
    assertEquals(TEXT_VALUE, XmlUtil.getString(nsXml, "my", "http://foo.bar", "/my:foo/my:bar/text()"));
  }

  public void testNodeListNsXml() {
    assertEquals(2, XmlUtil.select(nsXml, "my", "http://foo.bar", "/my:foo/my:bar/my:element").length);
  }

  public void testIsSchemaValidating() {
    if (System.getProperty("java.version").compareTo("1.5.0") >= 0) {
      assertTrue(XmlUtil.isSchemaValidating());
    }
  }

  public void testGetProcessingInstructionAttribute() throws Exception {
    Document template = XmlUtil.buildNewDocument(new FileInputStream("./test/formWithEmbeddedDataObject/template.xml"));

    assertEquals("urn:schemas-microsoft-com:office:infopath:applicationV3:-myXSD-2005-05-20T06-57-42", XmlUtil
      .getProcessingInstructionAttribute(template, "mso-infoPathSolution", "name"));
  }

}