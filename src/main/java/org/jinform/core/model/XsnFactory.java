package org.jinform.core.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jinform.core.rendering.IFormRendererFactory;
import org.w3c.dom.Document;

/**
 * @author Stephane Joyeux
 * @author David Dossot
 */
public final class XsnFactory {
  private static final String MS_CAB_DECODER_CLASS = "com.ms.util.cab.CabDecoder";

  private final static Log LOG = LogFactory.getLog(XsnFactory.class);

  private static boolean msCabDetected = false;

  private final IFormRendererFactory rendererFactory;

  static {
    try {
      Thread.currentThread().getContextClassLoader().loadClass(MS_CAB_DECODER_CLASS);
      msCabDetected = true;
    }
    catch (Throwable t) {
      // NOOP
    }
    LOG.info(MS_CAB_DECODER_CLASS + (msCabDetected ? " " : " not ") + "detected.");
  }

  public XsnFactory(IFormRendererFactory rendererFactory) {
    // TODO Auto-generated constructor stub
    this.rendererFactory = rendererFactory;
  }

  public IXsn newXsn(File file) throws IOException {
    if (!file.isFile()) throw new FileNotFoundException(file.toString());

    String fileName = file.getName();
    String extensionName = StringUtils.substringAfterLast(fileName, ".");

    if (extensionName.equalsIgnoreCase("zip")) {
      return new Xsn2003Zip(file, rendererFactory);
    }
    else {
      if ((msCabDetected) && (extensionName.equalsIgnoreCase("xsn"))) {
        return new Xsn2003(file, rendererFactory);
      }
      else {
        throw new ModelException("This file type is not supported: " + extensionName);
      }
    }
  }

  public static String getFormNameFromDataInstance(Document doc) {
    return Xsn2003.getFormNameFromDataInstance(doc);
  }

}