package org.jinform.core.rendering;

import java.io.OutputStream;

/**
 * Defines an IFormRenderer that is a <b>form-specific</b> rendering object that targets a particular format.
 * 
 * @author David Dossot
 */
public interface IFormRenderer {
  /**
   * Renders an empty Infopath form.
   * 
   * @param jinformRootUrl
   *          The Url of jinFORM, necessary for generating correct XForms.
   * 
   * @param result
   *          The outputstream into which the rendering is written
   * 
   * @return A byte array resulting from the transformation.
   */
  void renderEmpty(String jinformRootUrl, OutputStream result);

  /**
   * Renders an Infopath form filled with the provided data.
   * 
   * @param data
   *          The form data.
   * 
   * @param jinformRootUrl
   *          The Url of jinFORM, necessary for generating correct XForms.
   * 
   * @param result
   *          The outputstream into which the rendering is written
   * 
   * @return A byte array resulting from the transformation.
   */
  void render(byte[] data, String jinformRootUrl, OutputStream result);

}