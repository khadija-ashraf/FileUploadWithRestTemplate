package com.fileupload.listener;

import com.fileupload.controller.FileUploadClient;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * Created by Khadija on 2/7/17.
 */
public class CustomContextLoaderListener extends ContextLoaderListener {
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
    }
}
