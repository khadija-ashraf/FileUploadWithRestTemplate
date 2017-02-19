package com.fileupload;

import com.fileupload.controller.FileUploadClient;
import org.junit.Test;

/**
 * Created by Khadija on 2/8/17.
 */
public class TestFileUpload {

    @Test
    public void testfileUpload(){
        FileUploadClient fileUploadClient = new FileUploadClient();
        try {
            fileUploadClient.sendImageUploadRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
