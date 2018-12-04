package org.jinform.core.rendering;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.custommonkey.xmlunit.XMLTestCase;
import org.jinform.AllTests;
import org.jinform.core.model.IXsn;
import org.jinform.core.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author David Dossot
 */
public class TestRenderers extends XMLTestCase {
  private static final String JINFORM_URL = "foo://bar.baz";

  private IXsn xsn;

  public void setUp() throws Exception {
    super.setUp();

    xsn = AllTests.getXsnFactory().newXsn(new File("./test/formWithEmbeddedDataObject.xsn"));
  }

  public void testXFormsRenderer() throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    xsn.getDefaultRenderer().renderEmpty(JINFORM_URL, baos);
    byte[] emptyForm = baos.toByteArray();
    String emptyFormString = new String(emptyForm);

    assertTrue(emptyFormString.indexOf(JINFORM_URL) > -1);
    assertTrue(emptyFormString.indexOf(xsn.getName()) > -1);

    assertTrue(XmlUtil.isWellFormed(new ByteArrayInputStream(emptyForm)));

    Document doc = XmlUtil.buildNewDocument(emptyForm);

    assertXpathValuesEqual("4", "count(//processing-instruction())", doc);

    Element root = doc.getDocumentElement();
    assertEquals("html", root.getLocalName());
    assertEquals(1, root.getElementsByTagNameNS("http://www.w3.org/2002/xforms", "model").getLength());
    assertEquals(2, root.getElementsByTagNameNS("http://schemas.microsoft.com/office/infopath/2003/myXSD/2005-05-20T06:57:42",
      "applicationDataV2").getLength());

    assertEquals("http://jinform.org", root.getAttribute("xmlns:jf"));
    assertEquals("http://schemas.microsoft.com/office/infopath/2003/myXSD/2005-05-20T06:57:42", root.getAttribute("xmlns:my"));
  }

}
