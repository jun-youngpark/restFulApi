package com.mnwise.wiseu.web.common.upload.resolver;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.mnwise.wiseu.web.common.ui.upload.MonitoredDiskFileItemFactory;
import com.mnwise.wiseu.web.common.ui.upload.UploadListener;

@Component
public class AjaxFileUploadMultipartResolver extends CommonsMultipartResolver {
    private static final Logger log = LoggerFactory.getLogger(AjaxFileUploadMultipartResolver.class);

    public AjaxFileUploadMultipartResolver() {
        super();
    }

    public AjaxFileUploadMultipartResolver(ServletContext servletContext) {
        this();
        setServletContext(servletContext);
    }

    public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) {
        log.debug("ajax resolver --> 1 : " + new Date());
        String encoding = determineEncoding(request);
        FileUpload fileUpload1 = prepareFileUpload(encoding); // renamed fileUpload to fileUpload1

        log.debug("ajax resolver --> 2 : " + new Date());

        // Beginn of added code
        UploadListener listener = new UploadListener(request, 30);
        FileItemFactory factory = new MonitoredDiskFileItemFactory(listener);
        ServletFileUpload fileUpload = new ServletFileUpload(factory);
        fileUpload.setSizeMax(fileUpload1.getSizeMax());
        fileUpload.setHeaderEncoding(fileUpload1.getHeaderEncoding());
        // end of added code

        log.debug("ajax resolver --> 3 : " + new Date());

        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            log.debug("ajax resolver --> 4 : " + new Date());
            MultipartParsingResult parsingResult = parseFileItems(fileItems, encoding);
            log.debug("ajax resolver --> 5 : " + new Date());
            return new DefaultMultipartHttpServletRequest(request, parsingResult.getMultipartFiles(), parsingResult.getMultipartParameters(), null);
        } catch(FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        } catch(FileUploadException ex) {
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }
    }

}
