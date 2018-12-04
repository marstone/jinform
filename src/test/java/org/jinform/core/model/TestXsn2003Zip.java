package org.jinform.core.model;

import java.io.File;

import org.jinform.AllTests;

/**
 * @author Stephane Joyeux
 * @author David Dossot
 */
public class TestXsn2003Zip extends TestXsn2003 {

  protected String getFormName() {
    return "urn:schemas-microsoft-com:office:infopath:applicationV3:-myXSD-2005-05-20T06-57-42";
  }

  protected void init() throws Exception {
    xsn = AllTests.getXsnFactory().newXsn(new File("./test/formWithEmbeddedDataObject.zip"));
  }

}