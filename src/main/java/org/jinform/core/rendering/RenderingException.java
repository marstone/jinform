package org.jinform.core.rendering;

import org.jinform.core.CoreException;

/**
 * @author David Dossot
 */
public class RenderingException extends CoreException {
  private static final long serialVersionUID = 2968495556452067345L;

  public RenderingException(String message) {
    super(message);
  }

  public RenderingException(String message, Throwable cause) {
    super(message, cause);
  }

  public RenderingException(Throwable cause) {
    super(cause);
  }

}