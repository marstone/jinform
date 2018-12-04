package org.jinform;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jinform.core.model.TestXsn2003;
import org.jinform.core.model.TestXsn2003Zip;
import org.jinform.core.model.XsnFactory;
import org.jinform.core.rendering.TestRenderers;
import org.jinform.core.rendering.XFormsRendererFactory;
import org.jinform.core.rendering.XslDefinition;
import org.jinform.core.xml.TestMapping;
import org.jinform.core.xml.TestXmlUtil;
import org.jinform.core.xsl.extension.TestFormatting;
import org.jinform.core.xsl.extension.TestXDocument;
import org.jinform.server.TestFileFormCatalog;

/**
 * @author Stephane Joyeux
 */
public class AllTests {

  private static XsnFactory xsnFactory;

  public synchronized static XsnFactory getXsnFactory() {
    if (xsnFactory == null) {
      XFormsRendererFactory xfrf = new XFormsRendererFactory(new XslDefinition("/org/jinform/core/rendering/preProcessXsl.xsl"),
        new XslDefinition("/org/jinform/core/rendering/postProcessXForms.xsl"), "utf-8");
      xsnFactory = new XsnFactory(xfrf);
    }
    return xsnFactory;
  }

  public static Test suite() {
    TestSuite suite = new TestSuite("Test the whole jinFORM project");
    // $JUnit-BEGIN$
    suite.addTestSuite(TestXsn2003.class);
    suite.addTestSuite(TestXsn2003Zip.class);
    suite.addTestSuite(TestXmlUtil.class);
    suite.addTestSuite(TestMapping.class);
    suite.addTestSuite(TestFileFormCatalog.class);
    suite.addTestSuite(TestFormatting.class);
    suite.addTestSuite(TestXDocument.class);
    suite.addTestSuite(TestRenderers.class);
    // $JUnit-END$
    return suite;
  }
}
