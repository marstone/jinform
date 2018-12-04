package org.jinform.core.xsl.extension;

import org.jinform.core.model.IXsn;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XDocument {

  private static ThreadLocal activeForm = new ThreadLocal() {
    // NOOP
  };

  private static IXsn getActiveForm() {
    return (IXsn) activeForm.get();
  }

  private static ThreadLocal activeDocument = new ThreadLocal() {
    // NOOP
  };

  private static Document getActiveDocument() {
    return (Document) activeDocument.get();
  }

  public static void setActiveContext(IXsn form, Document document) {
    activeForm.set(form);
    activeDocument.set(document);
  }

  public static void resetActiveContext() {
    activeForm.set(null);
    activeDocument.set(null);
  }

  /**
   * Returns the current document on which the XSL is applying, a strange anti-pattern from Redmond we have to live with.
   * 
   * @return
   */
  public static Node getDOM() {
    return getActiveDocument();
  }

  /**
   * Returns a reference to the XML Document Object Model (DOM) of the specified DataObject object associated with the XDocument object.
   * See: http://msdn.microsoft.com/library/default.asp?url=/library/en-us/ipsdk/html/xdmthGetNamedNodeProperty_HV01103978.asp
   * 
   * @param name
   *          The name of a DataObject object.
   * @return A reference to an XML DOM document object.
   */
  public static Node GetDOM(String name) {
    return getActiveForm().getDataObject(name);
  }

  /**
   * Returns the value of a named property for the specified XML node, which must be a nonattribute node in the main data source. See:
   * http://msdn.microsoft.com/library/default.asp?url=/library/en-us/ipsdk/html/xdmthGetNamedNodeProperty_HV01103978.asp
   * 
   * @param targetNode
   *          An XML node corresponding to a nonattribute node in the main data source, for which a named property is to be set.
   * @param attributeName
   *          Specifies the name of the property whose value is to be returned.
   * @param defaultValue
   *          Specifies the default value to be returned if the property has not been set.
   * @return A string corresponding to the current value of the named property for the specified XML node in the main data source. If the
   *         specified property has not been set for this XML node, the specified default string is returned.
   */
  public static String GetNamedNodeProperty(Node targetNode, String attributeName, String defaultValue) {
    if (targetNode == null) return defaultValue;

    NamedNodeMap attributes = targetNode.getAttributes();
    if (attributes == null) return defaultValue;

    Node attribute = attributes.getNamedItem(attributeName);
    if (attribute == null) return defaultValue;

    String value = attribute.getNodeValue();
    if (value == null) return defaultValue;
    else return value;
  }
}