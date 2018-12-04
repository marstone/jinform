package org.jinform.core.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jinform.core.rendering.IFormRenderer;
import org.jinform.core.rendering.IFormRendererFactory;
import org.jinform.core.xml.XmlUtil;
import org.springframework.util.FileCopyUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author David Dossot
 */
abstract class AbstractXsn2003 implements IXsn {
  private static final String INFOPATHSOLUTION_PI = "mso-infoPathSolution";

  private final static String NS_PREFIX = "xsf";

  private final static String NS_URI = "http://schemas.microsoft.com/office/infopath/2003/solutionDefinition";

  private final static String MANIFEST = "manifest.xsf";

  private final static Log LOG = LogFactory.getLog(AbstractXsn2003.class);

  private final boolean valid;

  private final String name;

  private final Document manifest;

  private final byte[] template;

  private final byte[] view;

  private final byte[] schema;

  private final String[] packagedDataObjectNames;

  // when:JDK5 Map<String, Document>
  private final Map packagedDataObjects = new HashMap();

  private final String[] packagedFileNames;

  // when:JDK5 Map<String, Byte[]>
  private final Map packagedFiles = new HashMap();

  private final IFormRenderer defaultRenderer;

  public AbstractXsn2003(File file, IFormRendererFactory rendererFactory) throws IOException {
    manifest = XmlUtil.buildNewDocument(readFromArchive(file, MANIFEST));
    name = XmlUtil.getString(manifest, NS_PREFIX, NS_URI, "/xsf:xDocumentClass/@name");

    if ((name == null) || (name.equals(""))) throw new ModelException("Form with empty names are not valid");

    template = FileCopyUtils.copyToByteArray(readFromArchive(file, XmlUtil.getString(manifest, NS_PREFIX, NS_URI,
      "/xsf:xDocumentClass/xsf:fileNew/xsf:initialXmlDocument/@href")));

    view = FileCopyUtils.copyToByteArray(readFromArchive(file, XmlUtil.getString(manifest, NS_PREFIX, NS_URI,
      "/xsf:xDocumentClass/xsf:views/xsf:view/xsf:mainpane/@transform")));

    String schemaUriAndLocation = XmlUtil.getString(manifest, NS_PREFIX, NS_URI,
      "/xsf:xDocumentClass/xsf:documentSchemas/xsf:documentSchema[@rootSchema='yes']/@location");
    if (schemaUriAndLocation == null) {
      schema = null;
    }
    else {
      String[] schemaLocationParts = schemaUriAndLocation.split(" ");
      String schemaLocation = null;
      if (schemaLocationParts.length > 1) schemaLocation = schemaLocationParts[1];
      if (schemaLocation != null) {
        schema = FileCopyUtils.copyToByteArray(readFromArchive(file, schemaLocation));
      }
      else {
        schema = FileCopyUtils.copyToByteArray(readFromArchive(file, schemaUriAndLocation));
      }
    }

    packagedDataObjectNames = getPackagedFileNamesFromXPath("/xsf:xDocumentClass/xsf:dataObjects/xsf:dataObject[xsf:query/xsf:xmlFileAdapter/@fileUrl=/xsf:xDocumentClass/xsf:package/xsf:files/xsf:file/@name]/@name");

    for (int i = 0; i < packagedDataObjectNames.length; i++) {
      String packagedDataObjectName = packagedDataObjectNames[i];
      packagedDataObjects.put(packagedDataObjectName, XmlUtil.buildNewDocument(readFromArchive(file,
        getPackagedDataObjectFileName(packagedDataObjectName))));

      if (LOG.isDebugEnabled()) LOG.debug("Cached data object: " + packagedDataObjectName);
    }

    packagedFileNames = getPackagedFileNamesFromXPath("/xsf:xDocumentClass/xsf:package/xsf:files/xsf:file/@name");

    for (int i = 0; i < packagedFileNames.length; i++) {
      String packagedFileName = packagedFileNames[i];
      packagedFiles.put(packagedFileName, FileCopyUtils.copyToByteArray(readFromArchive(file, packagedFileName)));

      if (LOG.isDebugEnabled()) LOG.debug("Cached file: " + packagedFileName);
    }

    valid = true;

    defaultRenderer = rendererFactory.newRenderer(this);
  }

  public InputStream getView() {
    checkValidity();
    return new ByteArrayInputStream(view);
  }

  public InputStream getTemplate() {
    checkValidity();
    return new ByteArrayInputStream(template);
  }

  public InputStream getSchema() {
    checkValidity();
    return new ByteArrayInputStream(schema);
  }

  public Document getDataObject(String dataObjectName) {
    checkValidity();
    Document result = (Document) packagedDataObjects.get(dataObjectName);

    if (result == null) {
      String packagedDataObjectFileName = getPackagedDataObjectFileName(dataObjectName);
      try {
        result = XmlUtil.buildNewDocument(new URL(packagedDataObjectFileName).openStream());
      }
      catch (MalformedURLException mue) {
        throw new ModelException("Invalid data object URL: " + dataObjectName + "(" + packagedDataObjectFileName + ")", mue);
      }
      catch (IOException ioe) {
        throw new ModelException("Can not read form data object: " + dataObjectName + "(" + packagedDataObjectFileName + ")", ioe);
      }
    }
    return result;
  }

  public byte[] getPackagedFile(String packagedFileName) {
    checkValidity();
    byte[] result = (byte[]) packagedFiles.get(packagedFileName);

    if (result != null) {
      return result;
    }
    else {
      throw new ModelException("File " + packagedFileName + " is not stored in the package.");
    }
  }

  public boolean isValid() {
    return valid;
  }

  public String getName() {
    checkValidity();
    return name;
  }

  public IFormRenderer getDefaultRenderer() {
    checkValidity();
    return defaultRenderer;
  }

  // --------------- NON PUBLIC METHODS ---------------

  abstract protected InputStream readFromArchive(File sourceFile, String fileName);

  private String getPackagedDataObjectFileName(String packagedDataObjectName) {
    return XmlUtil.getString(manifest, NS_PREFIX, NS_URI, "/xsf:xDocumentClass/xsf:dataObjects/xsf:dataObject[@name='"
                                                          + packagedDataObjectName
                                                          + "']/xsf:query/xsf:xmlFileAdapter/@fileUrl");
  }

  private String[] getPackagedFileNamesFromXPath(String xpath) {
    Node[] fileNames = XmlUtil.select(manifest, NS_PREFIX, NS_URI, xpath);

    String[] result = new String[fileNames.length];
    for (int i = 0; i < fileNames.length; i++)
      result[i] = fileNames[i].getNodeValue();

    return result;
  }

  private void checkValidity() {
    if (!isValid()) throw new IllegalStateException("Can not fetch content on an invalid form");
  }

  static String getFormNameFromDataInstance(Document doc) {
    // TODO SAX it?
    return XmlUtil.getProcessingInstructionAttribute(doc, INFOPATHSOLUTION_PI, "name");
  }

}