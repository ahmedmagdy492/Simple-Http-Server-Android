package com.magdyradwan.httpserver.utility;

import java.io.File;

public class FileUtility {
    public static boolean isExist(String path) {
        File file = new File(path);

        return file.exists();
    }

    public static boolean isFile(String path) {
        File file = new File(path);
        return file.isFile();
    }
}
