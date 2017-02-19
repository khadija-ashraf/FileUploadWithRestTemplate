/*
 * Copyright (c) 2010, KaiTair
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are NOT permitted.
 */

package com.fileupload.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;


public enum ConfigProperties {
    INSTANCE;

    private Properties configProperties;
    private static final Logger log = LoggerFactory.getLogger(ConfigProperties.class);

    private ConfigProperties() {
        init();
    }

    private void init() {
        try {
            Resource resource = new ClassPathResource("config/config.properties");
            configProperties = PropertiesLoaderUtils.loadProperties(resource);
            
        }
        catch(IOException ex) {
            log.error("error loading config.properties", ex);
        }
    }

    public String getProperty(String key)
    {    	
        return configProperties.getProperty(key);
    }

    public boolean getBooleanProperty(String key) {
        return Boolean.valueOf(configProperties.getProperty(key, "false"));
    }

    public int getNumericProperty(String key)
    {
        return Integer.valueOf(configProperties.getProperty(key, "0"));
    }
    
    public void refresh()
    {
        init();
    }
}
