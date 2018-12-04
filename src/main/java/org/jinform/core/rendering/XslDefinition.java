package org.jinform.core.rendering;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;

/**
 * A holder for an XSL file.
 * 
 * @author David Dossot
 */
public final class XslDefinition implements IXslDefinition {
  private final Templates templates;

  public XslDefinition(String xslFileName) {
    try {
      StreamSource source = new StreamSource(this.getClass().getResourceAsStream(xslFileName));
      templates = TransformerFactory.newInstance().newTemplates(source);
    }
    catch (TransformerConfigurationException tce) {
      throw new RenderingException("Can not compile templates", tce);
    }
    catch (TransformerFactoryConfigurationError tfce) {
      throw new RenderingException("Can not compile templates", tfce);
    }
  }

  public Templates getTemplates() {
    return templates;
  }

}
