package com.willowtreeapps.hyperion.disk;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.Date;

class FileItem {

    File file;
    String path;
    String name;
    Uri uri;
    String extension;
    String mimeType;
    long size;
    boolean isDirectory;
    String lastModified;

    FileItem(Context context, File file) {
        this.file = file;
        path = file.getPath();
        name = file.getName();
        try {
            uri = FileProvider.getUriForFile(
                    context, "com.willowtreeapps.hyperion.disk.fileprovider", file);
            extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        } catch (Throwable e) {
            uri = null;
        }

        if (extension != null) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        size = file.length();
        isDirectory = file.isDirectory();
        lastModified = new Date(file.lastModified()).toString();
    }

    boolean isImage() {
        return name.endsWith("png") || name.endsWith("jpg") || name.endsWith("jpeg");
    }

    boolean isVideo() {
        return name.endsWith("mp4") || name.endsWith("mkv");
    }

}