package com.fileupload.controller;

import com.fileupload.util.FileUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Khadija on 1/23/17.
 */
@Controller
@RequestMapping("/")
public class FileUploadController {

    @RequestMapping(value = "upload/image", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public boolean uploadPartnerPropertyImages(HttpServletRequest request) {

        boolean success = false;

        try {
            InputStream inputStream = request.getPart("contentPic").getInputStream();

            String fileName = request.getParameter("fileName");
            String imageContentType = request.getParameter("imageContentType");

            String fileType = FileUtils.getImageExtentionFromMimeType(imageContentType);

            String baseFilePath = FileUtils.getContentRoot() + File.separator + fileName + "." + fileType;

            this.saveImage(baseFilePath, inputStream);

            success = true;
        } catch (Exception ex) {
            success = false;
            ex.printStackTrace();
        }

        return success;
    }

    @RequestMapping(method = RequestMethod.GET, value="test/{id}", headers="Accept=*/*")
    public @ResponseBody void testRestService(@PathVariable String id){
        System.out.println("This is a test service......"+ id);
    }

    private void saveImage(String baseFilePath, InputStream inputStream) throws Exception {
        OutputStream outputStream = null;

        try {

            File targetFile = new File(baseFilePath);
            targetFile.getParentFile().mkdirs();

            outputStream = new FileOutputStream(targetFile);
            IOUtils.copy(inputStream, outputStream);

        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
