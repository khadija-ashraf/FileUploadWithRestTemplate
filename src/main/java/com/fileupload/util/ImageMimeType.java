package com.fileupload.util;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Khadija on 1/23/17.
 */
public enum ImageMimeType
{
    IMAGE_FIF("image/fif", "fif"),
    IMAGE_X_ICON("image/x-icon", "ico"),
    IMAGE_GIF("image/gif", "gif"),
    IMAGE_IEF("image/ief", "ief"),
    IMAGE_IFS("image/ifs", "ifs"),
    IMAGE_JPEG("image/jpeg", "jpeg"),
    IMAGE_PNG("image/png", "png"),
    IMAGE_TIFF("image/tiff", "tiff"),
    IMAGE_VND("image/vnd", "dwg"),
    IMAGE_WAVELET("image/wavelet", "wi"),
    IMAGE_BMP("image/bmp", "bmp"),
    IMAGE_X_PHOTO_CD("image/x-photo-cd", "pcd"),
    IMAGE_X_CMU_RASTER("image/x-cmu-raster", "ras"),
    IMAGE_X_PORTABLE_ANYMAP("image/x-portable-anymap", "pnm"),
    IMAGE_X_PORTABLE_BITMAP("image/x-portable-bitmap", "pbm"),
    IMAGE_X_PORTABLE_GRAYMAP("image/x-portable-graymap", "pgm"),
    IMAGE_X_PORTABLE_PIXMAP("image/x-portable-pixmap", "ppm"),
    IMAGE_X_RGB("image/x-rgb", "rgb"),
    IMAGE_X_XBITMAP("image/x-xbitmap", "xbm"),
    IMAGE_X_XPIXMAP("image/x-xpixmap", "xpm"),
    IMAGE_X_XWINDOWDUMP("image/x-xwindowdump", "xwd");


    private String mimeTypeValue;
    private String extension;

    ImageMimeType(String mimeTypeValue, String extension ) {

        this.mimeTypeValue = mimeTypeValue;
        this.extension = extension;
    }


    public static ImageMimeType getTypeByMimeTypeValue(String mimeTypeValue){
        if(StringUtils.isNotBlank(mimeTypeValue)) {
            for (ImageMimeType type : ImageMimeType.values()) {
                if(type.getMimeTypeValue().equalsIgnoreCase(mimeTypeValue.trim())) {
                    return type;
                }
            }
        }
        return null;
    }

    public static ImageMimeType getTypeByExtension(String extension){
        if(StringUtils.isNotBlank(extension)) {
            for (ImageMimeType type : ImageMimeType.values()) {
                if(type.getExtension().equalsIgnoreCase(extension.trim())) {
                    return type;
                }
            }
        }
        return null;
    }
    public String getMimeTypeValue() {
        return mimeTypeValue;
    }

    public void setMimeTypeValue(String mimeTypeValue) {
        this.mimeTypeValue = mimeTypeValue;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
