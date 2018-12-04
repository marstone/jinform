/*
 * Created on May 27, 2005
 */
package org.jinform.core;

/**
 * @author David Dossot
 */
public class CoreException extends RuntimeException {
  private static final long serialVersionUID = -3933326818073191310L;

  public CoreException(String message) {
    super(message);
  }

  public CoreException(String message, Throwable cause) {
    super(message, cause);
  }

  public CoreException(Throwable cause) {
    super(cause);
  }

}