package com.willowtreeapps.hyperion.disk;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import java.io.File;

class FileItem {

    String path;
    String name;
    Uri uri;
    String extension;
    String mimeType;
    long size;
    boolean isDirectory;

    FileItem(Context context, File file) {
        path = file.getPath();
        name = file.getName();
        uri = FileProvider.getUriForFile(
                context, "com.willowtreeapps.hyperion.recorder.fileprovider", file);
        extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        if (extension != null) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        size = file.length();
        isDirectory = file.isDirectory();
    }

    boolean isImage() {
        return name.endsWith("png") || name.endsWith("jpg") || name.endsWith("jpeg");
    }

}