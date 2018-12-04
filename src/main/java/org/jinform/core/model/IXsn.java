package org.jinform.core.model;

import java.io.InputStream;

import org.jinform.core.rendering.IFormRenderer;
import org.w3c.dom.Document;

/**
 * @author Stephane Joyeux
 * @author David Dossot
 */
public interface IXsn {
  String getName();

  boolean isValid();

  InputStream getView();

  InputStream getTemplate();

  InputStream getSchema();

  Document getDataObject(String name);

  byte[] getPackagedFile(String packagedFileName);

  IFormRenderer getDefaultRenderer();
}