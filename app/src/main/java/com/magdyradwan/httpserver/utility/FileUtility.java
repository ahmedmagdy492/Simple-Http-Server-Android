package com.magdyradwan.httpserver.utility;

import java.io.File;

public class FileUtility {
    public boolean isExist(String path) {
        File file = new File(path);

        return file.exists();
    }

    public boolean isFile(String path) {
        File file = new File(path);
        return file.isFile();
    }
}
