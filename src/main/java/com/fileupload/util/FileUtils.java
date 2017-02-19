package com.fileupload.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Khadija on 1/23/17.
 */
public class FileUtils {

    public static final Map<String, String> mimeTypeToExtensionMap;

    static {
        mimeTypeToExtensionMap = new HashMap<String, String>();

        for (ImageMimeType imageMimeType : ImageMimeType.values()) {
            mimeTypeToExtensionMap.put(imageMimeType.getMimeTypeValue(), imageMimeType.getExtension());
        }
    }

    public static String getImageExtentionFromMimeType(String mimeTypeValue) {
        return mimeTypeToExtensionMap.get(mimeTypeValue);
    }

    public static byte[] convertImageToByteArray(String fullyQualifiedPath) throws FileNotFoundException {

        File file = new File(fullyQualifiedPath);

        FileInputStream fileInputStream = new FileInputStream(file);
        // create FileInputStream which obtains input bytes from a file in a file system
        // FileInputStream is meant for reading streams of raw bytes such as image data.
        // For reading streams of characters, consider using FileReader.

        byte[] buf = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            for (int readNum; (readNum = fileInputStream.read(buf)) != -1;) {
                //Writes to this byte array output stream
                byteArrayOutputStream.write(buf, 0, readNum);
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileInputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return byteArrayOutputStream.toByteArray();
    }

    public static String getContentRoot() {

        // Get tomcat base
        String fullDir = System.getProperty("catalina.base");

        // content.root should have a separator
        String contentRoot = ConfigProperties.INSTANCE.getProperty("content.root");
        if (!contentRoot.startsWith(File.separator)) {
            fullDir += File.separator + "webapps" + File.separator + contentRoot;
        } else {
            fullDir += File.separator + "webapps" + contentRoot;
        }

        File file = new File(fullDir);
        if (!file.exists()) {
            file.mkdirs();
        }

        return fullDir;
    }
}
