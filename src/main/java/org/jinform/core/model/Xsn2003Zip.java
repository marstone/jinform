package org.jinform.core.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jinform.core.rendering.IFormRendererFactory;

/**
 * @author Stephane Joyeux
 */
final class Xsn2003Zip extends AbstractXsn2003 {
  public Xsn2003Zip(File file, IFormRendererFactory rendererFactory) throws IOException {
    super(file, rendererFactory);
  }

  protected InputStream readFromArchive(File sourceFile, String fileName) {
    try {
      ZipFile archive = new ZipFile(sourceFile);
      for (Enumeration e = archive.entries(); e.hasMoreElements();) {
        // Now follow the zip file in memory :
        ZipEntry zipEntry = (ZipEntry) e.nextElement();
        if (zipEntry.getName().equals(fileName)) return archive.getInputStream(zipEntry);
      }
    }
    catch (IOException e) {
      throw new ModelException(e);
    }
    throw new ModelException(fileName + " not found in form archive.");
  }
}