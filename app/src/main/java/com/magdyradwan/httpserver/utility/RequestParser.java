package com.magdyradwan.httpserver.utility;

import com.magdyradwan.httpserver.utility.models.HttpRequestModel;

import java.util.Dictionary;

public class RequestParser {
    public HttpRequestModel parseRequest(String request) {
        HttpRequestModel httpRequestModel = new HttpRequestModel();

        String[] reqLines = request.split("\r\n");

        if(reqLines.length == 0)
            return new HttpRequestModel();

        String[] httpMetaData = reqLines[0].split(" ");

        if(httpMetaData.length <= 1)
            return new HttpRequestModel();

        httpRequestModel.setMethod(httpMetaData[0]);

        // extracting query params
        if(httpMetaData[1].split("/\\?").length == 2) {
            String queryString = httpMetaData[1].split("/\\?")[1];
            Dictionary<String, String> queryParams = httpRequestModel.getQueryParams();
            String[] queryComponents = queryString.split("&");

            for (String queryComponent : queryComponents) {
                String paramName = queryComponent.split("=")[0].toLowerCase();
                String value = queryComponent.split("=")[1].toLowerCase();
                queryParams.put(paramName, value);
            }
        }

        // extracting the headers
        Dictionary<String, String> headers = httpRequestModel.getHeaders();
        int length = httpRequestModel.getMethod().compareToIgnoreCase("GET") == 0 ? reqLines.length : reqLines.length - 1;

        for(int i = 1; i < length; i++) {
            String[] headerComponent = reqLines[i].split(": ");
            if(headerComponent.length == 2) {
                headers.put(headerComponent[0], headerComponent[1]);
            }
        }

        return httpRequestModel;
    }
}
