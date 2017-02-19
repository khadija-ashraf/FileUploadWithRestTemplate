# File Upload

## Tutorial 1
### POSTing multipart requests with RestTemplate
This tutorial will make you acquaintance to File Uploading by two services where one is acting like a client, that is uploading and another is acting like a server, that is saving the file to a physical location.

### 1. Writing the Client.

#### Step 1:  Registering the message converters.

Since we are sending the multipart/formdata in JSON format so to serialize the whole request body we need message converters.

For converting Java objects to JSON, we need a **MappingJackson2HttpMessageConverter**. For the binary part of the message (the image, in this case), we need to use either a **ByteArrayHttpMessageConverter** (for byte[] format), or a **ResourceHttpMessageConverter** (for ByteArrayResource).

Usually these simple message converters are enough for producing web service requests, but in this case, -as we need them to kind of co-operate – a FormHttpMessageConverter is needed. This one differs a bit from the other message converters, because it wraps other HttpMessageConverters in a field called partConverters. These part converters are responsible for converting different entities like, form fields and byte array of uploading files, into one single request. In our case, this looks something like this:
```sh
--FormHttpMessageConverter
|
|-- MappingJackson2HttpMessageConverter
|-- ResourceHttpMessageConverter
```

Now, Initialize the RestTemplate.
```java
RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
```

Now, if we want to log all our HTTP request and response messages the we can write a custom interceptor and add it to our RestTemplate instance.

Say our custom interceptor is  **LogRequestResponseFilter**, then,
```java
List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
interceptors.add(new LogRequestResponseFilter());
restTemplate.setInterceptors(interceptors);
Lets add message converters now.
// register the converters
restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
restTemplate.getMessageConverters().add(new ResourceHttpMessageConverter());

FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
formHttpMessageConverter.addPartConverter(new MappingJackson2HttpMessageConverter());
formHttpMessageConverter.addPartConverter(new ResourceHttpMessageConverter());
restTemplate.getMessageConverters().add(formHttpMessageConverter);
```
#### Step 2: Before registering  MappingJackson2HttpMessageConverter we need to configure the “jackson-databind” in the application.
First, add the **"jackson-databind"** dependency to the POM.
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.2.3</version>
</dependency>
```
Second, add **MappingJackson2HttpMessageConverter** as a bean in applicationContex.xml.

```xml
<!-- Converts JSON to POJO and vice versa-->
<!-- Configure to plugin JSON as request and response in method handler -->
<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
    <property name="messageConverters">
        <util:list>
            <ref bean="jsonMessageConverter"/>
        </util:list>
    </property>
</bean>

<bean id="jsonMessageConverter" class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter"></bean>
```
#### Step 3: Construct the Request-Header.
Now, let’s construct the request message itself.
First, we need a message header, that will correctly identify our request as form/multipart:
```java
HttpHeaders headers = new HttpHeaders();
headers.set("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);
```
#### Step 4: Construct the Request-Body.
Request header is ready, let’s take some care about message body. The message body will be contained in a MultiValueMap. This map will contain the two parts of the message, in two different HttpEntity objects. Both these entities may have their own sub-headers. In this way they can be identified as application/json and image/png. But, I’ve verified that even if I don’t set sub-header for form fields they can be read as a parameter by HTTPServletRequest.getParameter(). Let’s see how it’s done:
```java
HttpHeaders pictureHeader = new HttpHeaders();
pictureHeader.setContentType(MediaType.IMAGE_PNG);
HttpEntity<ByteArrayResource> contentPic = new HttpEntity<ByteArrayResource>(bytes, pictureHeader);
multipartRequest.add("contentPic", contentPic);
```
Now we can add other form fields directly to the  MultiValueMap.
```java
multipartRequest.add("fileName", FILE_NAME);
multipartRequest.add("imageContentType", IMAGE_CONTENT_TYPE);
```
Now make the call.
```java
HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(multipartRequest, headers);
```


### 2. Writing the Server.

To enable the file uploading feature in Spring, first we need to have some configurations completed.
#### Step 1: Add Apache commons file Upload dependency.
Since we are using Apache commons file Upload so add the relevant dependency in your POM.
```xml
<dependency>
   <groupId>commons-fileupload</groupId>
   <artifactId>commons-fileupload</artifactId>
   <version>1.3</version>
</dependency>
```

#### Step 2: Add required config properties.
We also need to add the configuration properties required to upload file through Apache commons file upload.
```xml
<multipart-config>
   <location>/tmp</location>
   <max-file-size>5242880</max-file-size>
   <max-request-size>27262976</max-request-size>
   <file-size-threshold>32768</file-size-threshold>
</multipart-config>
```
#### Step 3: Upgrade servlet-api version.
Upgrade the version of servlet-api in POM to 3.0.1 if you are in lower version. Since lower versions do NOT support HTTPServletRequest.getPart() method, that is needed to read the multipart data from request. The desired dependency is given below:
```xml
<dependency>
   <groupId>javax.servlet</groupId>
   <artifactId>javax.servlet-api</artifactId>
   <version>3.1.0</version>
   <scope>provided</scope>
</dependency>
```
As a part of this upgrade we may need to update the schema definition of web.xml.

My web.xml schema definition is,
```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 						http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">
```
#### Step 4:
Now in the server end the service will consume ”multipart/form-data”
Below is the sample code how the service should be started.

```java
@ResponseBody
@RequestMapping(value = "upload/image", method = RequestMethod.POST, consumes = {"multipart/form-data"})
public boolean uploadPartnerPropertyImages(HttpServletRequest request) {
    boolean success = false;

    try {
        InputStream inputStream = request.getPart("contentPic").getInputStream();
        String fileName = request.getParameter("fileName");
        String imageContentType = request.getParameter("imageContentType");

        success = true;
    } catch (Exception ex) {
        success = false;
        ex.printStackTrace();
    }
    return success;
}
```
**This is it!!! We are done with file uploading using RestTemplate.**


#### Step 5:
Add below lines in the applicationContext.xml.
```xml
<context:component-scan base-package="com.fileupload.controller" />
<bean class="org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping" />
<bean class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter" />
```
**DefaultAnnotationHanlderMappings** and **AnnotationMethodHandlerAdapter**
Beans that will make the @ReqeustMapping annotation on the class or method to be processed by Spring.



##### Refs:
[https://tamasgyorfi.net/2015/03/27/posting-multipart-requests-with-resttemplate/](https://tamasgyorfi.net/2015/03/27/posting-multipart-requests-with-resttemplate/)
[http://www.ibm.com/developerworks/library/wa-spring3webserv/](http://www.ibm.com/developerworks/library/wa-spring3webserv/)
[http://moznion.hatenadiary.com/entry/2015/03/05/233142](http://moznion.hatenadiary.com/entry/2015/03/05/233142)
