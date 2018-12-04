/*
 * Created on May 27, 2005
 */
package org.jinform.server;

/**
 * @author David Dossot
 */
public class ServerException extends RuntimeException {
  private static final long serialVersionUID = -3933326818073191310L;

  public ServerException(String message) {
    super(message);
  }

  public ServerException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServerException(Throwable cause) {
    super(cause);
  }

}