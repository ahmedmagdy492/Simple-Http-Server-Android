package com.magdyradwan.httpserver.utility;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.magdyradwan.httpserver.utility.models.FileModel;

import java.io.File;
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
            FileModel fileModel = new FileModel();
            fileModel.setFullPath(file.getAbsolutePath());
            fileModel.setName(file.getName());
            fileModel.setFile(false);
            fileModels.add(fileModel);
        }

        return fileModels;
    }
}
