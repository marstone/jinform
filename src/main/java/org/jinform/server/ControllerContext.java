package org.jinform.server;

import org.jinform.core.IFormCatalog;

public class ControllerContext {
  private String versionNumber;

  private String encoding;

  private IFormCatalog formCatalog;

  private String presentationServerHome;

  public String getPresentationServerHome() {
    return presentationServerHome;
  }

  public void setPresentationServerHome(String presentationServerHome) {
    this.presentationServerHome = presentationServerHome;
  }

  public IFormCatalog getFormCatalog() {
    return formCatalog;
  }

  public void setFormCatalog(IFormCatalog formCatalog) {
    this.formCatalog = formCatalog;
  }

  public void setVersionNumber(String versionNumber) {
    this.versionNumber = versionNumber;
  }

  public String getVersionNumber() {
    return versionNumber;
  }

  public String getEncoding() {
    return this.encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

}
