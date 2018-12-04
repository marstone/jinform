package org.jinform.core.xsl.extension;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author David Dossot
 */
public class TestFormatting extends TestCase {
  private final static String TEXT = "text";

  private final static String ATTRIBUTE = "attribute";

  private final static String TEST_DATE_FORMAT = "yyyy-MM-dd";

  private String originalDate;

  private String expectedDate;

  private Node textNode;

  private Attr attributeNode;

  private Node dateNode;

  protected void setUp() throws Exception {
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    textNode = doc.createTextNode(TEXT);
    attributeNode = doc.createAttribute("foo");
    attributeNode.setValue(ATTRIBUTE);

    Date date = new Date();
    originalDate = new SimpleDateFormat(org.jinform.core.xsl.extension.DateUtil.XML_DATE_FORMAT).format(date);
    dateNode = doc.createTextNode(originalDate);
    expectedDate = new SimpleDateFormat(TEST_DATE_FORMAT).format(date);
  }

  public void testFormatStringUnknown() {
    assertEquals(TEXT, Formatting.formatString(textNode, "n/a", "n/a"));
    assertEquals(ATTRIBUTE, Formatting.formatString(attributeNode, "n/a", "n/a"));
    assertEquals(originalDate, Formatting.formatString(dateNode, "n/a", "n/a"));
  }

  public void testFormatStringString() {
    assertEquals(TEXT, Formatting.formatString(textNode, "string", "n/a"));
    assertEquals(ATTRIBUTE, Formatting.formatString(attributeNode, "string", "n/a"));
  }

  public void testFormatStringDate() {
    assertEquals(expectedDate, Formatting.formatString(dateNode, "date", null));
    assertEquals(expectedDate, Formatting.formatString(dateNode, "date", "n/a"));
    assertEquals(expectedDate, Formatting.formatString(dateNode, "date", "locale:2057;dateFormat:" + TEST_DATE_FORMAT + ";"));
    assertFalse(originalDate.equals(Formatting.formatString(dateNode, "date", "locale:2057;dateFormat:dd-MMM-yyyy;")));
  }

  // TODO (SJO) test other formats
}
