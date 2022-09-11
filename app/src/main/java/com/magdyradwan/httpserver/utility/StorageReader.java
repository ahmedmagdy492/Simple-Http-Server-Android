package com.magdyradwan.httpserver.utility;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;

import com.magdyradwan.httpserver.utility.models.FileModel;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StorageReader {

    public List<FileModel> readEntries(Context context, String path) {

        File rootDirectory = new File(path);
        File[] files = rootDirectory.listFiles();

        List<FileModel> fileModels = new ArrayList<>();

        if(files == null)
            files = new File[] {};

        for(File file : files) {
            if(file.isFile() && !file.canRead()) continue;

            FileModel fileModel = new FileModel();
            fileModel.setFullPath(file.getAbsolutePath());
            fileModel.setName(file.getName());
            fileModel.setFile(false);
            fileModel.setSizeInBytes(file.length());
            int index = file.getName().lastIndexOf(".");
            if(index != -1) {
                String fileType = file.getName().substring(index+1);
                if(fileType.equals("jpg") || fileType.equals("jpeg") || fileType.equals("png"))
                {
                    fileModel.setMimeType("image/" + fileType);
                }
                else if(fileType.equals("pdf")){
                    fileModel.setMimeType("application/pdf");
                }
                else {
                    fileModel.setMimeType("text/plain");
                }
            }
            fileModels.add(fileModel);
        }

        return fileModels;
    }
}
