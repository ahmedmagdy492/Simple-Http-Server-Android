package com.magdyradwan.httpserver.utility;

import com.magdyradwan.httpserver.utility.models.HttpRequestModel;
import com.magdyradwan.httpserver.utility.models.HttpResponseModel;
import com.magdyradwan.httpserver.utility.models.StatusCodes;

import java.util.Hashtable;

public class RequestHandler {
    public HttpResponseModel handleRequest(HttpRequestModel requestModel) {
        HttpResponseModel responseModel = new HttpResponseModel();
        String method = requestModel.getMethod();
        Hashtable<String, String> queryParams = (Hashtable<String, String>) requestModel.getQueryParams();

        if(method.compareToIgnoreCase("get") != 0)
        {
            responseModel.setStatusCode(StatusCodes.MethodNotAllowed);
            responseModel.setFiles(null);
            return responseModel;
        }
        else if(queryParams.size() == 0 || !queryParams.containsKey("path")) {
            // TODO: return the entries of the root directory
        }
        else if(!FileUtility.isExist(queryParams.get("path"))) {
            responseModel.setStatusCode(StatusCodes.NotFound);
            responseModel.setFiles(null);
            return responseModel;
        }

        String path = queryParams.get("path");
        if(FileUtility.isFile(path)) {
            // TODO: read the file content and return it
            responseModel.setIsFile(true);
            return responseModel;
        }

        // TODO: read system entries and return it
        return responseModel;
    }
}
