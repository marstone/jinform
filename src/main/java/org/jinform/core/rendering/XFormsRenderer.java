package org.jinform.core.rendering;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jinform.core.model.IXsn;
import org.jinform.core.xml.NamespaceMapping;
import org.jinform.core.xml.XmlUtil;
import org.jinform.core.xsl.extension.XDocument;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * @author David Dossot
 */
class XFormsRenderer implements IFormRenderer {
  private final IXsn form;

  private final String encoding;

  private final IXslDefinition postProcessXForms;

  private final Templates preProcessedXsl;

  private final Document templateDocument;

  private final Map templateNamespaces;

  XFormsRenderer(IXsn form, IXslDefinition preProcessXsl, IXslDefinition postProcessXForms, String encoding) {
    this.form = form;
    this.postProcessXForms = postProcessXForms;
    this.encoding = encoding;
    this.templateDocument = XmlUtil.buildNewDocument(form.getTemplate());
    this.templateNamespaces = XmlUtil.gatherNamespaces(new InputSource(form.getTemplate()));

    // we pre-transform the form XSL with the preProcess template and change namespaces to point from MS XSL extensions
    // to jinFORM ones
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      preProcessXsl.getTemplates().newTransformer().transform(new StreamSource(form.getView()), new StreamResult(baos));
      preProcessedXsl = TransformerFactory.newInstance().newTemplates(
        NamespaceMapping.uriToPackage(new InputSource(new ByteArrayInputStream(baos.toByteArray()))));
    }
    catch (TransformerFactoryConfigurationError tfce) {
      throw new RenderingException(tfce);
    }
    catch (FactoryConfigurationError fce) {
      throw new RenderingException(fce);
    }
    catch (TransformerConfigurationException tce) {
      throw new RenderingException(tce);
    }
    catch (TransformerException te) {
      throw new RenderingException(te);
    }
  }

  public void render(byte[] data, String jinformRootUrl, OutputStream result) {
    final Document formDataDocument = (data != null) ? XmlUtil.buildNewDocument(data) : templateDocument;
    final InputStream formDataInputStream = (data != null) ? new ByteArrayInputStream(data) : form.getTemplate();

    try {
      // place active DOM document and XSN form in thread local
      XDocument.setActiveContext(form, formDataDocument);

      // if this chain of transform via byte[] is not performant enough, consider
      // replacing with chained sax transforms, which do not support parameters
      // so a replacement mechanism (entity resolver?) would be needed
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Transformer transformer = preProcessedXsl.newTransformer();
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
      transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.transform(new StreamSource(formDataInputStream), new StreamResult(baos));

      transformer = postProcessXForms.getTemplates().newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
      transformer.setParameter("generator_name", this.getClass().getName());
      transformer.setParameter("form_name", form.getName());
      transformer.setParameter("server_root", jinformRootUrl);

      transformer.setURIResolver(new URIResolver() {
        public Source resolve(String href, String base) throws TransformerException {
          if (href.equals("form-data-instance")) return new DOMSource(formDataDocument);
          else if (href.equals("template-data-instance")) return new DOMSource(templateDocument);
          else return null;
        }
      });

      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      baos = new ByteArrayOutputStream();
      transformer.transform(new StreamSource(bais), new StreamResult(baos));

      // add any missing namespace from the template to the root of the resulting document
      transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
      transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "true");
      transformer.transform(new SAXSource(XmlUtil.newRootElementNamespaceAdder(templateNamespaces), new InputSource(
        new ByteArrayInputStream(baos.toByteArray()))), new StreamResult(result));

    }
    catch (TransformerConfigurationException tce) {
      throw new RenderingException(tce);
    }
    catch (TransformerException te) {
      throw new RenderingException(te);
    }
    finally {
      XDocument.resetActiveContext();
    }
  }

  public void renderEmpty(String jinformRootUrl, OutputStream result) {
    render(null, jinformRootUrl, result);
  }
}
