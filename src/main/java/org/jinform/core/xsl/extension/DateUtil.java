package org.jinform.core.xsl.extension;

import java.text.SimpleDateFormat;

/**
 * Different utilities for handling ISO 8601 compliant XML dates.
 * 
 * @author David Dossot
 */
public abstract class DateUtil {
  private DateUtil() {
    //NOOP
  }

  public final static String XML_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public final static String XML_DATE_FORMAT = "yyyy-MM-dd";

  public final static String XML_TIME_FORMAT = "yyyy-MM-dd";

  public static String Now() {
    return new SimpleDateFormat( XML_DATETIME_FORMAT).format(new java.util.Date());
  }

  public static String Today() {
    return new SimpleDateFormat(XML_DATE_FORMAT).format(new java.util.Date());
  }
}