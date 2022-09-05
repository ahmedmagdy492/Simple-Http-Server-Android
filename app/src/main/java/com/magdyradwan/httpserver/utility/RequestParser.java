package com.magdyradwan.httpserver.utility;

import com.magdyradwan.httpserver.utility.models.HttpRequestModel;

import java.util.Dictionary;

public class RequestParser {
    public HttpRequestModel parseRequest(String request) {
        HttpRequestModel httpRequestModel = new HttpRequestModel();

        String[] reqLines = request.split("\r\n");
        String[] httpMetaData = reqLines[0].split(" ");
        httpRequestModel.setMethod(httpMetaData[0]);

        // extracting query params
        if(httpMetaData[1].split("/?").length == 2) {
            String queryString = httpMetaData[1].split("/?")[1];
            Dictionary<String, String> queryParams = httpRequestModel.getQueryParams();
            String[] queryComponents = queryString.split("&");

            for (String queryComponent : queryComponents) {
                String paramName = queryComponent.split("=")[0];
                String value = queryComponent.split("=")[1];
                queryParams.put(paramName, value);
            }
        }

        // extracting the headers
        Dictionary<String, String> headers = httpRequestModel.getHeaders();
        int length = httpRequestModel.getMethod().compareToIgnoreCase("GET") == 0 ? reqLines.length : reqLines.length - 1;

        for(int i = 1; i < length; i++) {
            String[] headerComponent = reqLines[i].split(": ");
            headers.put(headerComponent[0], headerComponent[1]);
        }

        return httpRequestModel;
    }
}
