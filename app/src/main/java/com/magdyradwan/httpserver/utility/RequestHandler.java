package com.magdyradwan.httpserver.utility;

import android.content.Context;
import android.icu.util.Output;
import android.os.Environment;
import android.util.Log;

import com.magdyradwan.httpserver.utility.models.FileModel;
import com.magdyradwan.httpserver.utility.models.HttpRequestModel;
import com.magdyradwan.httpserver.utility.models.HttpResponseModel;
import com.magdyradwan.httpserver.utility.models.StatusCodes;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

        if(method != null && method.compareToIgnoreCase("get") != 0)
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

        String path = java.net.URLDecoder.decode(queryParams.get("path"), StandardCharsets.UTF_8.name());

        if(path.compareToIgnoreCase("/") == 0) {
            List<FileModel> entries = reader.readEntries(context, Environment.getExternalStorageDirectory().getPath() + "/");
            responseModel.setStatusCode(StatusCodes.OK);
            responseModel.setFiles(entries);
            return responseModel;
        }

        FileUtility fileUtility = new FileUtility();

        if(fileUtility.isFile(path)) {
            // read the file content and return it
            responseModel.setStatusCode(StatusCodes.OK);
            responseModel.setIsFile(true);
            File file = new File(path);
            int index = file.getName().lastIndexOf(".");
            if(index != -1) {
                String fileType = file.getName().substring(index+1);
                if(fileType.equals("jpg") || fileType.equals("jpeg") || fileType.equals("png"))
                {
                    responseModel.setFileType("image/" + fileType);
                }
                else if(fileType.equals("pdf")){
                    responseModel.setFileType("application/pdf");
                }
                else {
                    responseModel.setFileType("text/plain");
                }
            }
            Log.d("TAG", "handleRequest: it's a file: " + file.getAbsolutePath());
            responseModel.setFullPath(file.getAbsolutePath());
            return responseModel;
        }

        if(!fileUtility.isExist(path)) {
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
}
