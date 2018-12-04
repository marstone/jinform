package org.jinform.core.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jinform.core.IAvailableForms;
import org.jinform.core.IFormCatalog;
import org.jinform.core.IFormCatalogEntry;
import org.jinform.core.model.IXsn;
import org.jinform.core.model.ModelException;
import org.jinform.core.model.XsnFactory;

import EDU.oswego.cs.dl.util.concurrent.Mutex;
import EDU.oswego.cs.dl.util.concurrent.ReadWriteLock;
import EDU.oswego.cs.dl.util.concurrent.ReaderPreferenceReadWriteLock;

/**
 * @author Stephane Joyeux
 * @author David Dossot
 */
public class FileFormCatalog implements IFormCatalog {
  private final static Log LOG = LogFactory.getLog(FileFormCatalog.class);

  private final static String INVALID_FORM_NAME = "INVALID";

  // when:JDK5 use from java.util.concurrent
  private final ReadWriteLock refreshSetsLock = new ReaderPreferenceReadWriteLock();

  // when:JDK5 use from java.util.concurrent
  private final Mutex refreshCatalogMutex = new Mutex();

  private final File folder;

  private final XsnFactory xsnFactory;

  // when:JDK5 Map<File, ValidFormEntry>
  private Map validFormFileCache = new HashMap();

  // when:JDK5 Map<String, IXsn>
  private Map validFormCache = new HashMap();

  // when:JDK5 Map<File, InvalidFormEntry>
  private Map invalidFormFileCache = new HashMap();

  // when:JDK5 Set<IFormCatalogEntry>
  private Set validForms;

  // when:JDK5 Set<IFormCatalogEntry>
  private Set invalidForms;

  private final static Comparator FORM_COMPARATOR = new Comparator() {
    public int compare(Object o1, Object o2) {
      return ((IFormCatalogEntry) o1).getFileName().compareToIgnoreCase(((IFormCatalogEntry) o2).getFileName());
    }
  };

  private final static FileFilter FILE_ONLY_FILTER = new FileFilter() {
    public boolean accept(File pathname) {
      return pathname.isFile();
    }
  };

  /**
   * Equality of ValidFormEntry is based on the form name, not the source file.
   */
  private static class ValidFormEntry implements IFormCatalogEntry {

    private final String fileName;

    private final IXsn form;

    private final long lastModified;

    public ValidFormEntry(File sourceFile, IXsn form) {
      if (sourceFile == null) throw new IllegalArgumentException("Null is not a valid FormEntry source file.");
      if (form == null) throw new IllegalArgumentException("Null is not a valid FormEntry form.");

      this.fileName = sourceFile.getName();
      this.form = form;
      this.lastModified = sourceFile.lastModified();
    }

    public long getLastModified() {
      return lastModified;
    }

    public String getFileName() {
      return fileName;
    }

    public String getFormName() {
      return form.getName();
    }

    public IXsn getForm() {
      return this.form;
    }

    public boolean equals(Object o) {
      if (o == null) return false;
      if (!(o instanceof ValidFormEntry)) return false;
      return getFormName().equals(((ValidFormEntry) o).getFormName());
    }

    public int hashCode() {
      return getFormName().hashCode();
    }

    public String toString() {
      return "FormEntry[LastModified: " + getLastModified() + " - File: " + getFileName() + " - Form: " + getFormName() + "]";
    }
  }

  /**
   * Equality of InvalidFormEntry is based on the source file.
   */
  private static class InvalidFormEntry implements IFormCatalogEntry {

    private final String fileName;

    private final long lastModified;

    public InvalidFormEntry(File sourceFile) {
      if (sourceFile == null) throw new IllegalArgumentException("Null is not a valid FormEntry source file.");

      this.fileName = sourceFile.getName();
      this.lastModified = sourceFile.lastModified();
    }

    public long getLastModified() {
      return lastModified;
    }

    public String getFileName() {
      return fileName;
    }

    public String getFormName() {
      return INVALID_FORM_NAME;
    }

    public boolean equals(Object o) {
      if (o == null) return false;
      if (!(o instanceof ValidFormEntry)) return false;
      return getFormName().equals(((ValidFormEntry) o).getFormName());
    }

    public int hashCode() {
      return getFormName().hashCode();
    }

    public String toString() {
      return "FormEntry[LastModified: " + getLastModified() + " - File: " + getFileName() + " - Form: " + getFormName() + "]";
    }
  }

  private static class AvailableForms implements IAvailableForms {
    private final Set validForms;

    private final Set invalidForms;

    public AvailableForms(Set validForms, Set invalidForms) {
      this.validForms = validForms;
      this.invalidForms = invalidForms;
    }

    public Set getInvalidForms() {
      return this.invalidForms;
    }

    public Set getValidForms() {
      return this.validForms;
    }
  }

  public FileFormCatalog(XsnFactory xsnFactory, File folder) {
    if (!folder.isDirectory()) throw new ModelException(folder + " is not a valid folder name !");
    this.xsnFactory = xsnFactory;
    this.folder = folder;
    if (LOG.isInfoEnabled()) LOG.info("FileFormCatalog has been initialized on " + folder.getAbsolutePath());
  }

  public IXsn getForm(String formName) throws InterruptedException {
    refreshSetsLock.readLock().acquire();
    try {
      return (IXsn) validFormCache.get(formName);
    }
    finally {
      refreshSetsLock.readLock().release();
    }
  }

  public IAvailableForms getForms() throws InterruptedException {
    refreshSetsLock.readLock().acquire();
    try {
      return new AvailableForms(validForms, invalidForms);
    }
    finally {
      refreshSetsLock.readLock().release();
    }
  }

  /**
   * Must be called by a <b>unique thread</b>, a timer in this case.
   */
  public void refreshCatalog() throws InterruptedException {
    if (!refreshCatalogMutex.attempt(0)) {
      if (LOG.isDebugEnabled()) LOG.debug("Can not acquire refreshCatalogMutex, try again later!");
    }
    else {
      try {
        if (LOG.isDebugEnabled()) LOG.debug("Refreshing Catalog");

        boolean mustRefreshState = false;
        Map newValidFormFileCache = new HashMap();
        Map newInvalidFormFileCache = new HashMap();
        Map newValidFormCache = new HashMap();

        File files[] = folder.listFiles(FILE_ONLY_FILTER);

        for (int i = 0; i < files.length; i++) {
          File formFile = files[i];
          if (LOG.isDebugEnabled()) LOG.debug("Analyzing file: " + formFile);

          boolean shouldProcessFile = false;

          IFormCatalogEntry existingFormEntry = (IFormCatalogEntry) validFormFileCache.remove(formFile);
          if (existingFormEntry == null) existingFormEntry = (IFormCatalogEntry) invalidFormFileCache.remove(formFile);

          if (existingFormEntry != null) {
            if (existingFormEntry.getLastModified() != formFile.lastModified()) {
              shouldProcessFile = true;
              if (LOG.isDebugEnabled()) LOG.debug("Must refresh form: " + formFile.getName());
            }
            else {
              if (LOG.isDebugEnabled()) LOG.debug("Form last modified unchanged: " + formFile.getName());
            }
          }
          else {
            shouldProcessFile = true;
            if (LOG.isDebugEnabled()) LOG.debug("Process new form: " + formFile.getName());
          }

          IFormCatalogEntry newFormEntry;

          if (shouldProcessFile) {
            mustRefreshState = true;
            boolean formIsValid = false;
            IXsn form = null;

            try {
              form = xsnFactory.newXsn(formFile);
              formIsValid = form.isValid();
            }
            catch (Exception e) {
              LOG.error("Error when analyzing form file: " + formFile, e);
            }

            if (LOG.isDebugEnabled()) LOG.debug("Refresh caches for: " + formFile + " - valid: " + formIsValid);

            if (formIsValid) {
              newFormEntry = new ValidFormEntry(formFile, form);
            }
            else {
              newFormEntry = new InvalidFormEntry(formFile);
            }

          }
          else {
            // file was not necessary to proceed, simply re-store in relevant new collection
            newFormEntry = existingFormEntry;
          }

          if (newFormEntry instanceof ValidFormEntry) {
            newValidFormFileCache.put(formFile, newFormEntry);
            newValidFormCache.put(newFormEntry.getFormName(), ((ValidFormEntry) newFormEntry).getForm());
          }
          else {
            newInvalidFormFileCache.put(formFile, newFormEntry);
          }
        }

        // if a valid or an invalid form is not present in the folder anymore, force refresh
        mustRefreshState = mustRefreshState || (validFormFileCache.values().size() > 0) || (invalidFormFileCache.values().size() > 0);

        validFormFileCache = newValidFormFileCache;
        invalidFormFileCache = newInvalidFormFileCache;

        if (mustRefreshState) {
          if (LOG.isDebugEnabled()) LOG.debug("Refreshing state");

          Set tmpValidForms = new TreeSet(FORM_COMPARATOR);
          tmpValidForms.addAll(validFormFileCache.values());
          Set tmpInvalidForms = new TreeSet(FORM_COMPARATOR);
          tmpInvalidForms.addAll(invalidFormFileCache.values());

          refreshSetsLock.writeLock().acquire();
          try {
            validFormCache = newValidFormCache;
            validForms = Collections.unmodifiableSet(tmpValidForms);
            invalidForms = Collections.unmodifiableSet(tmpInvalidForms);
          }
          finally {
            refreshSetsLock.writeLock().release();
          }
        }
      }
      finally {
        refreshCatalogMutex.release();
      }
    }
  }

}