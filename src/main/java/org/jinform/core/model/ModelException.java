package org.jinform.core.model;

import org.jinform.core.CoreException;

/**
 * @author Stephane Joyeux
 */
public class ModelException extends CoreException {
  private static final long serialVersionUID = -3176052061408693440L;

  public ModelException(String message) {
    super(message);
  }

  public ModelException(String message, Throwable cause) {
    super(message, cause);
  }

  public ModelException(Throwable cause) {
    super(cause);
  }

}