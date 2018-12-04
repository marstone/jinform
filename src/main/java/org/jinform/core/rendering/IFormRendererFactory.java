package org.jinform.core.rendering;

import org.jinform.core.model.IXsn;

/**
 * @author David Dossot
 */
public interface IFormRendererFactory {
  IFormRenderer newRenderer(IXsn form);
}
