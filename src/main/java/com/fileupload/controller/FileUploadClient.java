package com.fileupload.controller;

import com.fileupload.interceptor.LogRequestResponseFilter;
import com.fileupload.util.ConfigProperties;
import com.fileupload.util.FileUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khadija on 1/23/17.
 */
public class FileUploadClient {

    public static final String FILE_NAME = "TestFileName";
    public static final String IMAGE_CONTENT_TYPE = "image/jpeg";
    public static final String SERVICE_ENDPOINT = ConfigProperties.INSTANCE.getProperty("file.upload.service.url");


    public boolean sendImageUploadRequest() throws Exception {

        RestTemplate restTemplate = registerRestTemplate();

        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<String, Object>();

        String fullyQualifiedPath = ConfigProperties.INSTANCE.getProperty("file.path");

        ByteArrayResource bytes = new ByteArrayResource(FileUtils.convertImageToByteArray(fullyQualifiedPath));

        HttpHeaders pictureHeader = new HttpHeaders();
        pictureHeader.setContentType(MediaType.IMAGE_PNG);
        HttpEntity<ByteArrayResource> contentPic = new HttpEntity<ByteArrayResource>(bytes, pictureHeader);
        multipartRequest.add("contentPic", contentPic);


        multipartRequest.add("fileName", FILE_NAME);
        multipartRequest.add("imageContentType", IMAGE_CONTENT_TYPE);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(multipartRequest, headers);

        return restTemplate.postForObject(SERVICE_ENDPOINT, requestEntity, boolean.class);
    }

    private RestTemplate registerRestTemplate() throws Exception{
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new LogRequestResponseFilter());
        restTemplate.setInterceptors(interceptors);

        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        formHttpMessageConverter.addPartConverter(new MappingJackson2HttpMessageConverter());
        formHttpMessageConverter.addPartConverter(new ResourceHttpMessageConverter());

        restTemplate.getMessageConverters().add(formHttpMessageConverter);
        return restTemplate;
    }
}
