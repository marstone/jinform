package org.jinform.core.xml;

import org.jinform.core.CoreException;

/**
 * @author David Dossot
 */
public class XmlException extends CoreException {
  private static final long serialVersionUID = -5968954451378712318L;

  public XmlException(String message) {
    super(message);
  }

  public XmlException(String message, Throwable cause) {
    super(message, cause);
  }

  public XmlException(Throwable cause) {
    super(cause);
  }

}