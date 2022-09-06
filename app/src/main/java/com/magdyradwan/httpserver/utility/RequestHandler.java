package com.magdyradwan.httpserver.utility;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.magdyradwan.httpserver.utility.models.FileModel;
import com.magdyradwan.httpserver.utility.models.HttpRequestModel;
import com.magdyradwan.httpserver.utility.models.HttpResponseModel;
import com.magdyradwan.httpserver.utility.models.StatusCodes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class RequestHandler {

    public HttpResponseModel handleRequest(Context context, HttpRequestModel requestModel)
            throws IOException {
        HttpResponseModel responseModel = new HttpResponseModel();
        String method = requestModel.getMethod();
        StorageReader reader = new StorageReader();
        Hashtable<String, String> queryParams = (Hashtable<String, String>) requestModel.getQueryParams();

        if(method.compareToIgnoreCase("get") != 0)
        {
            responseModel.setStatusCode(StatusCodes.MethodNotAllowed);
            responseModel.setFiles(null);
            return responseModel;
        }
        else if(queryParams.size() == 0 || !queryParams.containsKey("path")) {
            // return the entries of the root directory
            responseModel.setStatusCode(StatusCodes.OK);
            responseModel.setFiles(reader.readEntries(context,
                    Environment.getExternalStorageDirectory().getPath() + "/"));
            return responseModel;
        }

        String path = queryParams.get("path");

        if(path.compareToIgnoreCase("/") == 0) {
            List<FileModel> entries = reader.readEntries(context, Environment.getExternalStorageDirectory().getPath() + "/");
            responseModel.setStatusCode(StatusCodes.OK);
            responseModel.setFiles(entries);
            return responseModel;
        }

        if(FileUtility.isFile(path)) {
            // read the file content and return it
            responseModel.setStatusCode(StatusCodes.OK);
            responseModel.setFileContent(readFileContent(path));
            responseModel.setIsFile(true);
            return responseModel;
        }

        if(!FileUtility.isExist(queryParams.get("path"))) {
            responseModel.setStatusCode(StatusCodes.NotFound);
            responseModel.setFiles(null);
            return responseModel;
        }

        // read system entries and return it
        List<FileModel> entries = reader.readEntries(context, path);
        responseModel.setStatusCode(StatusCodes.OK);
        responseModel.setFiles(entries);
        return responseModel;
    }

    private byte[] readFileContent(String path) throws IOException {
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();

        while(line != null) {
            stringBuilder.append(line);
            line = bufferedReader.readLine();
        }

        return stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }
}
