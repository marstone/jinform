package org.jinform.core.xml;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.jinform.core.xsl.extension.Formatting;
import org.jinform.core.xsl.extension.XDocument;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * @author David Dossot
 */
public class TestMapping extends TestCase {
  private static String ORIGINAL = "<xsl:stylesheet version='1.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:my='http://schemas.microsoft.com/office/infopath/2003/myXSD/2005-05-20T06:57:42' xmlns:xd='http://schemas.microsoft.com/office/infopath/2003' xmlns:xsl='http://www.w3.org/1999/XSL/Transform' xmlns:msxsl='urn:schemas-microsoft-com:xslt' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns:xdExtension='http://schemas.microsoft.com/office/infopath/2003/xslt/extension' xmlns:xdXDocument='http://schemas.microsoft.com/office/infopath/2003/xslt/xDocument' xmlns:xdSolution='http://schemas.microsoft.com/office/infopath/2003/xslt/solution' xmlns:xdFormatting='http://schemas.microsoft.com/office/infopath/2003/xslt/formatting' xmlns:xdImage='http://schemas.microsoft.com/office/infopath/2003/xslt/xImage' xmlns:xdUtil='http://schemas.microsoft.com/office/infopath/2003/xslt/Util' xmlns:xdMath='http://schemas.microsoft.com/office/infopath/2003/xslt/Math' xmlns:xdDate='http://schemas.microsoft.com/office/infopath/2003/xslt/Date' xmlns:sig='http://www.w3.org/2000/09/xmldsig#' xmlns:xdSignatureProperties='http://schemas.microsoft.com/office/infopath/2003/SignatureProperties'/>";

  public void testUriToPackageSAX() {
    Document result = XmlUtil.buildNewDocument(NamespaceMapping.uriToPackage(new InputSource(new ByteArrayInputStream(ORIGINAL.getBytes()))));

    assertEquals(NamespaceMapping.NS_EXTENSION_PREFIX + XDocument.class.getName(), result.getDocumentElement()
        .getAttribute("xmlns:xdXDocument"));
    
    assertEquals(NamespaceMapping.NS_EXTENSION_PREFIX + Formatting.class.getName(), result.getDocumentElement()
        .getAttribute("xmlns:xdFormatting"));
  }
}