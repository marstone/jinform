package org.jinform.core.xml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.jinform.core.xsl.extension.DateUtil;
import org.jinform.core.xsl.extension.Formatting;
import org.jinform.core.xsl.extension.XDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * @author David Dossot
 */
public abstract class NamespaceMapping {

  final static String NS_EXTENSION_PREFIX = "java://";

  private NamespaceMapping() {
  }

  private final static Map NS_TO_PACKAGE;

  static {
    Map temp = new HashMap();
    temp.put("xdXDocument", NS_EXTENSION_PREFIX + XDocument.class.getName());
    temp.put("xdFormatting", NS_EXTENSION_PREFIX + Formatting.class.getName());
    temp.put("xdDate", NS_EXTENSION_PREFIX + DateUtil.class.getName());
    NS_TO_PACKAGE = Collections.unmodifiableMap(temp);
  }

  public static Source uriToPackage(InputSource in) {
    XMLFilterImpl filter;
    try {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setNamespaceAware(true);
      filter = new NamespaceTranslatorFilter(spf.newSAXParser().getXMLReader());
    }
    catch (SAXException saxe) {
      throw new XmlException(saxe);
    }
    catch (ParserConfigurationException pce) {
      throw new XmlException(pce);
    }

    return new SAXSource(filter, in);
  }

  private static class NamespaceTranslatorFilter extends XMLFilterImpl {

    public NamespaceTranslatorFilter(XMLReader parent) throws SAXException, ParserConfigurationException {
      super(parent);
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
      if (NS_TO_PACKAGE.containsKey(prefix)) super.startPrefixMapping(prefix, (String) NS_TO_PACKAGE.get(prefix));
      else super.startPrefixMapping(prefix, uri);
    }

  }
}