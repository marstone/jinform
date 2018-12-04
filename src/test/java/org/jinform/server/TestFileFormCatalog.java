package org.jinform.server;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

import org.jinform.AllTests;
import org.jinform.core.IFormCatalogEntry;
import org.jinform.core.impl.FileFormCatalog;

/**
 * @author Stephane Joyeux
 */
public class TestFileFormCatalog extends TestCase {

  private final static String FOLDER_PATH = "./test/forms";

  private final static String VALID_FORM = "ValidForm.zip";

  private final static String INVALID_FORM = "simpleForm.xsn";

  protected FileFormCatalog ffc;

  public void setUp() throws Exception {
    super.setUp();
    ffc = new FileFormCatalog(AllTests.getXsnFactory(), new File(FOLDER_PATH));
    ffc.refreshCatalog();
  }

  public void testValidForm() throws InterruptedException {
    assertEquals(3, ffc.getForms().getValidForms().size());
  }

  public void testInvalidForm() throws InterruptedException {
    Set set = ffc.getForms().getInvalidForms();
    for (Iterator iter = set.iterator(); iter.hasNext();) {
      IFormCatalogEntry element = (IFormCatalogEntry) iter.next();
      if (element.getFileName().equals(INVALID_FORM)) return;
    }
    fail("Invalid form not found");
  }

  public void testListFormsIsWellBuild() throws InterruptedException {
    String formValidName = null;

    for (Iterator iter = ffc.getForms().getValidForms().iterator(); iter.hasNext();) {
      IFormCatalogEntry element = (IFormCatalogEntry) iter.next();
      if (element.getFileName().equals(VALID_FORM)) {
        formValidName = element.getFormName();
        break;
      }
    }

    for (Iterator iter = ffc.getForms().getInvalidForms().iterator(); iter.hasNext();) {
      IFormCatalogEntry element = (IFormCatalogEntry) iter.next();
      if (element.getFormName() != null) {
        String formInvalidName = element.getFormName();
        assertFalse(formInvalidName.equals(formValidName));
      }
    }
  }

}