package org.jinform.core.model;

import dorkbox.cabParser.CabException;
import dorkbox.cabParser.CabParser;
import org.jinform.core.rendering.IFormRendererFactory;

import java.io.*;


/**
 * @author Stephane Joyeux
 */
class Xsn2003 extends AbstractXsn2003 {
  public Xsn2003(File sourceFile, IFormRendererFactory rendererFactory) throws IOException {
    super(sourceFile, rendererFactory);
  }

  protected InputStream readFromArchive(File sourceFile, final String fileName) {
    FileInputStream fis = null;

    try {
      fis = new FileInputStream(sourceFile);
      ByteArrayOutputStream baos = new CabParser(fis, fileName).extractStream();
      return new ByteArrayInputStream(baos.toByteArray());
    }
    catch (FileNotFoundException fnfe) {
      throw new ModelException("Can not find the source form file", fnfe);
    }
    catch (CabException ce) {
      throw new ModelException("Can not decompress the XSN file", ce);
    }
    catch (IOException ioe) {
      throw new ModelException("Can not read the form", ioe);
    }
    finally {
      if (fis != null) try {
        fis.close();
      }
      catch (IOException ioe) {
        // Ignore Exception
      }
    }
  }

}