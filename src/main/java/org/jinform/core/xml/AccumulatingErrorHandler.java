package org.jinform.core.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author David Dossot
 */
public class AccumulatingErrorHandler extends DefaultHandler {
	private List errors = new ArrayList();

	private List fatalErrors = new ArrayList();

	private List warnings = new ArrayList();

	/**
	 * @return Returns the errors.
	 */
	public List getErrors() {
		return errors;
	}

	/**
	 * @return Returns the fatalErrors.
	 */
	public List getFatalErrors() {
		return fatalErrors;
	}

	/**
	 * @return Returns the warnings.
	 */
	public List getWarnings() {
		return warnings;
	}

	public void error(SAXParseException spe) throws SAXException {
		errors.add(spe.getLocalizedMessage());
	}

	public void fatalError(SAXParseException spe) throws SAXException {
		fatalErrors.add(spe.getLocalizedMessage());
	}

	public void warning(SAXParseException spe) throws SAXException {
		warnings.add(spe.getLocalizedMessage());
	}

}