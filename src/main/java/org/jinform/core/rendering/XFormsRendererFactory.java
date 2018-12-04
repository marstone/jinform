package org.jinform.core.rendering;

import org.jinform.core.model.IXsn;

/**
 * @author David Dossot
 */
public class XFormsRendererFactory implements IFormRendererFactory {

  private final IXslDefinition preProcessXsl;

  private final IXslDefinition postProcessXForms;

  private final String encoding;

  public XFormsRendererFactory(IXslDefinition preProcessXsl, IXslDefinition postProcessXForms, String encoding) {
    this.preProcessXsl = preProcessXsl;
    this.postProcessXForms = postProcessXForms;
    this.encoding = encoding;
  }

  public IFormRenderer newRenderer(IXsn form) {
    return new XFormsRenderer(form, preProcessXsl, postProcessXForms, encoding);
  }

}
