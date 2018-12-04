package org.jinform.core.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.custommonkey.xmlunit.XMLTestCase;
import org.jinform.AllTests;
import org.jinform.core.xml.AccumulatingErrorHandler;
import org.jinform.core.xml.XmlUtil;

/**
 * @author Stephane Joyeux
 * @author David Dossot
 */
public class TestXsn2003 extends XMLTestCase {

  protected IXsn xsn;

  protected String getFormName() {
    return "urn:schemas-microsoft-com:office:infopath:applicationV3:-myXSD-2005-05-20T06-57-42";
  }

  public void setUp() throws Exception {
    super.setUp();
    init();
  }
  
  protected void init() throws Exception {
    xsn = AllTests.getXsnFactory().newXsn(new File("./test/formWithEmbeddedDataObject.xsn"));
  }

  public void testValidation() throws Exception {
    if (XmlUtil.isSchemaValidating()) {
      AccumulatingErrorHandler aeh = new AccumulatingErrorHandler();
      XmlUtil.validate(xsn.getTemplate(), xsn.getSchema(), aeh);
      assertEquals(0, aeh.getWarnings().size());
      assertEquals(8, aeh.getErrors().size());
      assertEquals(0, aeh.getFatalErrors().size());
    }
  }

  public void testGetView() throws Exception {
    assertEquals(17217, fileSize(xsn.getView()));
    assertTrue(XmlUtil.isWellFormed(xsn.getView()));
  }

  public void testGetTemplate() throws IOException {
    assertNotNull(xsn.getTemplate());
  }

  public void testGetSchema() throws Exception {
    assertEquals(2243, fileSize(xsn.getSchema()));
    assertTrue(XmlUtil.isWellFormed(xsn.getSchema()));
  }

  public void testGetDataObject() throws IOException {
    assertNotNull(xsn.getDataObject("jobs"));
  }

  protected int fileSize(InputStream is) throws IOException {
    // function to get the size in bytes of a input stream file :
    int fileSize = 0;
    byte[] block = new byte[1024];
    int blockSize;
    while ((blockSize = is.read(block)) != -1) {
      fileSize += blockSize;
    }
    return fileSize;

  }

  public void testSchemaPhysicalFile() throws IOException {
    assertXMLEqual(XmlUtil.buildNewDocument(xsn.getSchema()), XmlUtil.buildNewDocument(new FileInputStream(
      "./test/formWithEmbeddedDataObject/myschema.xsd")));
  }

  public void testViewPhysicalFile() throws IOException {
    assertXMLEqual(XmlUtil.buildNewDocument(xsn.getView()), XmlUtil.buildNewDocument(new FileInputStream(
      "./test/formWithEmbeddedDataObject/view1.xsl")));
  }

  public void testObjectPhysicalFile() throws IOException {
    assertXMLEqual(xsn.getDataObject("jobs"), XmlUtil.buildNewDocument(new FileInputStream("./test/formWithEmbeddedDataObject/jobs.xml")));
  }

  public void testTemplatePhysicalFile() throws Exception {
    assertXMLEqual(new InputStreamReader(xsn.getTemplate()), new FileReader("./test/formWithEmbeddedDataObject/template.xml"));
  }

  public void testIsValid() throws FileNotFoundException {
    assertTrue(xsn.isValid());
  }

  public void testgetName() throws FileNotFoundException {
    assertEquals(getFormName(), xsn.getName());
  }

  public void testGetFormNameFromDataInstance() throws FileNotFoundException {
    assertEquals(getFormName(), Xsn2003.getFormNameFromDataInstance(XmlUtil.buildNewDocument(xsn.getTemplate())));
  }

}