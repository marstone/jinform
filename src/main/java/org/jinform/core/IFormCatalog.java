package org.jinform.core;

import org.jinform.core.model.IXsn;

/**
 * @author Stephane Joyeux
 * @author David Dossot
 */
public interface IFormCatalog {
  public IAvailableForms getForms() throws InterruptedException;

  public IXsn getForm(String formName) throws InterruptedException;
}