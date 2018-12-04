package org.jinform.server;

import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jinform.core.IAvailableForms;
import org.jinform.core.rendering.IFormRenderer;
import org.jinform.core.xml.XmlUtil;
import org.springframework.beans.BeansException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class FrontControllerServlet extends HttpServlet {
    private static final long serialVersionUID = -472861844848950L;

    private static final String PRESENTATION_SERVER_HOME = "presentationServerHome";

    private static final String JINFORM_ROOT_URL = "jinformRootUrl";

    private final static Log LOG = LogFactory.getLog(FrontControllerServlet.class);

    private ControllerContext context;

    private final SynchronizedBoolean firstRequest = new SynchronizedBoolean(true);

    public void init() throws ServletException, BeansException {
        context = (ControllerContext) WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("controllerContext");
        LOG.info("jinFORM v" + context.getVersionNumber() + " is initialized");

        if (getServletContext().getAttribute(PRESENTATION_SERVER_HOME) == null)
            getServletContext().setAttribute(PRESENTATION_SERVER_HOME, context.getPresentationServerHome());
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (firstRequest.commit(true, false)) {
            if (getServletContext().getAttribute(JINFORM_ROOT_URL) == null)
                getServletContext().setAttribute(
                        JINFORM_ROOT_URL,
                        new StringBuffer(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort())
                                .append(request.getContextPath()).toString());
        }

        super.service(request, response);
    }

    // ------- POST Processing Methods -------

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (LOG.isDebugEnabled())
            LOG.debug("Handling " + request.getMethod() + " request for content type:" + pathInfo);

        if ("/view".equals(pathInfo)) {
            doPostView(request, response);
        } else if ("/form".equals(pathInfo)) {
            doPostForm(request, response);
        } else {
            throw new ServerException(pathInfo + " is not a supported path info");
        }
    }

    protected void doPostForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String mode = request.getParameter("mode");
        if (StringUtils.isEmpty(mode)) throw new ServerException("Parameter 'mode' is missing");

        if ("save".equals(mode)) {
            Document submittedFormData = XmlUtil.buildNewDocument(request.getInputStream());

            // TODO redirect to save view instead of this direct outputting
            response.setContentType("text/xml");
            PrintWriter writer = response.getWriter();
            XmlUtil.writeTo(submittedFormData, writer);
            writer.flush();
        } else {
            throw new ServerException(mode + " is not a supported mode");
        }
    }

    protected void doPostView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // FIXME remove when handled by OPS pipelines

        // all posted views should submit an xml document whose root element is <viewData viewName="view-name" xmlns="">
        // let any error happening in the parsing explode as a runtime exception
        Element viewData = XmlUtil.buildNewDocument(request.getInputStream()).getDocumentElement();
        if (!"viewData".equals(viewData.getNodeName()))
            throw new ServerException("A view must post a viewData XML instance");

        if (LOG.isDebugEnabled()) {
            StringWriter viewDataDump = new StringWriter();
            XmlUtil.writeTo(viewData, viewDataDump);
            LOG.debug("Got view data:" + viewDataDump.toString());
        }

        String viewName = viewData.getAttribute("viewName");
        if (StringUtils.isEmpty(viewName))
            throw new ServerException("A viewData XML instance must contain a viewName attribute");

        // let us treat the known views
        if ("main".equals(viewName)) {
            // we either receive a form name or form data, if both, privilege is given to form data
            // String uploadedFormData = DomUtils.getTextValue((Element) viewData.getElementsByTagName("uploadedFormData").item(0));
            String formName = null;

            // redirect to form rendering
            if (StringUtils.isEmpty(formName))
                throw new ServerException("No form name has been extracted from this view submission");
            doPresentationServerRedirect(request, response);
        } else {
            throw new ServerException(viewName + " is not a supported view");
        }
    }

    // ------- GET Processing Methods -------

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String pathInfo = request.getPathInfo();
        if (LOG.isDebugEnabled()) LOG.debug("Handling request for content type:" + pathInfo);

        if ("/presentationServerHome".equals(pathInfo)) {
            doPresentationServerRedirect(request, response);
        } else if ("/view".equals(pathInfo)) {
            doGetView(request, response);
        } else if ("/form".equals(pathInfo)) {
            doGetForm(request, response);
        } else if ("/resource".equals(pathInfo)) {
            doGetResource(request, response);
        } else {
            throw new ServerException(pathInfo + " is not a supported path info");
        }
    }

    private void doPresentationServerRedirect(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/redirect.jsp").forward(request, response);
    }

    private void doGetView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        if (StringUtils.isEmpty(name)) throw new ServerException("Parameter 'name' is missing");

        if ("main".equals(name)) {
            Map formSets = new HashMap();
            try {
                IAvailableForms availableForms = context.getFormCatalog().getForms();
                formSets.put("validForms", availableForms.getValidForms());
                formSets.put("invalidForms", availableForms.getInvalidForms());
            } catch (InterruptedException ie) {
                // log and restore the interrupted state
                LOG.error("Can not fetch the sets of forms");
                Thread.currentThread().interrupt();
            }
            request.setAttribute("lists", formSets);
            request.setAttribute("formRenderingUrl", getServletContext().getAttribute(JINFORM_ROOT_URL) + "/cp/form?name=");
        }
        request.getRequestDispatcher("/WEB-INF/views/" + name + ".jsp").forward(request, response);
    }

    private void doGetResource(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resourceUrl = request.getParameter("src");
        if (LOG.isDebugEnabled())
            LOG.debug("Handling " + request.getMethod() + " request for resource URL: " + resourceUrl);

        ServletOutputStream out = response.getOutputStream();

        try {
            FileCopyUtils.copy(context.getFormCatalog().getForm(StringUtils.substringAfter(resourceUrl, "|")).getPackagedFile(
                    StringUtils.substringBefore(resourceUrl, "|")), out);
        } catch (Exception e) {
            // widely catch any trouble and log it: this prevents a useless top level
            // redirection to error page
            LOG.error(e);
        }
        out.flush();
    }

    private void doGetForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        if (StringUtils.isEmpty(name)) throw new ServerException("Parameter 'name' is missing");

        // get the precompiled XSL template
        try {
            IFormRenderer renderer = context.getFormCatalog().getForm(name).getDefaultRenderer();
            // this content is not cacheable because even empty forms can have
            // computed fields that vary (date/time default values...).
            response.setContentType("text/xml; charset=" + context.getEncoding());
            ServletOutputStream sos = response.getOutputStream();
            renderer.renderEmpty((String) getServletContext().getAttribute(JINFORM_ROOT_URL), sos);
            sos.flush();
        } catch (InterruptedException ie) {
            // log and restore the interrupted state
            LOG.error("Can not fetch the sets of forms");
            Thread.currentThread().interrupt();
        }
    }

}
