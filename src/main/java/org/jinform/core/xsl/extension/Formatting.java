package org.jinform.core.xsl.extension;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

/**
 * @author David Dossot
 */
public class Formatting {
  private final static Log LOG = LogFactory.getLog(Formatting.class);

  public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

  /**
   * Formats the specified string or XML node according to the specified category and options parameters. See:
   * http://msdn.microsoft.com/library/default.asp?url=/library/en-us/ipsdk/html/xdmthFormatString_HV01025232.asp
   * 
   * @param input
   *          The string value or XML node to be formatted.
   * @param category
   *          The string value that specifies the category used for formatting. Values include number, percentage, currency, date, time, and
   *          datetime
   * @param option
   *          The string value that specifies the options used for formatting. Takes the form of a case-sensitive string in the format
   *          "optionName:value".
   * @return A String representing the formatted input.
   */
  public static String formatString(Node input, String category, String option) {
    String valueToFormat = input.getNodeValue();
    if (StringUtils.isEmpty(valueToFormat)) return "";

    if (LOG.isDebugEnabled()) LOG.debug("Must format: '" + valueToFormat + "' ; category: '" + category + "' ; option: '" + option + "'");

    // for string category return the unchanged value
    if ("string".equalsIgnoreCase(category)) return valueToFormat;

    // TODO support other categories : percentage, currency, datetime, time

    // Number Formatting
    if ("number".equalsIgnoreCase(category)) {
      Number numberToFormat = null;
      NumberFormat nf = NumberFormat.getInstance();
      try {
        numberToFormat = nf.parse(valueToFormat.replace('+', ' ').trim());
      }
      catch (ParseException e) {
        // Can not format so log and return value unchanged
        LOG.error("Formatting impossible because the original format is not XML compliant.", e);
        return valueToFormat;
      }

      // then process optional number format
      // sample option: numDigits:0;grouping:0;negativeOrder:1;
      // so far, let's only consider "numDigit" and support "grouping" in an on/off manner, we can improve later
      try {
        int grouping = NumberUtils.createInteger(StringUtils.substringBetween(option, "grouping:", ";")).intValue();
        nf.setGroupingUsed(grouping != 0);
      }
      catch (NumberFormatException nfe) {
        if (LOG.isDebugEnabled()) LOG.debug("Number formatting ignored grouping in option: '" + option + "'");
      }

      try {
        int numDigits = NumberUtils.createInteger(StringUtils.substringBetween(option, "numDigits:", ";")).intValue();
        nf.setMinimumFractionDigits(numDigits);
        nf.setMaximumFractionDigits(numDigits);
        return nf.format(numberToFormat);
      }
      catch (NumberFormatException nfe) {
        // if the number of fractional digits is auto or not specified, we
        // default to the platform number format
        // and since this format was used to parse the current number, we can
        // then simply return the original value!
        if (LOG.isDebugEnabled()) LOG.debug("Number formatting ignored numDigits in option: '" + option + "'");
        return valueToFormat;
      }
    }

    // Date Formatting
    if ("date".equalsIgnoreCase(category)) {
      Date dateToFormat = null;
      try {
        dateToFormat = new SimpleDateFormat(DateUtil.XML_DATE_FORMAT).parse(valueToFormat);
      }
      catch (ParseException e) {
        // Can not format so log and return value unchanged
        LOG.error("Formatting impossible because the original format is not XML compliant.", e);
        return valueToFormat;
      }

      // then process optional date format
      // sample option: locale:2057;dateFormat:yyyy-MM-dd;
      String dateFormat = StringUtils.substringBetween(option, "dateFormat:", ";");

      if (StringUtils.isEmpty(dateFormat)) {
        if (LOG.isDebugEnabled()) LOG.debug("FormatString uses defaut date format.");
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(dateToFormat);
      }

      DateFormat df;

      if (StringUtils.contains(dateFormat.toUpperCase(), "SHORT")) df = DateFormat.getDateInstance(DateFormat.SHORT);
      else if (StringUtils.contains(dateFormat.toUpperCase(), "MEDIUM")) df = DateFormat.getDateInstance(DateFormat.MEDIUM);
      else if (StringUtils.contains(dateFormat.toUpperCase(), "LONG")) df = DateFormat.getDateInstance(DateFormat.LONG);
      else df = new SimpleDateFormat(dateFormat);

      return df.format(dateToFormat);
    }

    // As a fall-back mechanism, warn and return the unchanged value
    LOG.warn("Format [category: '" + category + "' ; option: '" + option + "'] not supported.");
    return valueToFormat;
  }
}