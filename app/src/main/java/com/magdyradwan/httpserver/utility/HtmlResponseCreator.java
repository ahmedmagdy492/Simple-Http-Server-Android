package com.magdyradwan.httpserver.utility;

import com.magdyradwan.httpserver.utility.models.HttpResponseModel;

import java.util.Dictionary;

public class HtmlResponseCreator implements IResponseCreator {

    private HttpResponseModel response;

    @Override
    public void setResponseModel(HttpResponseModel response) {
        this.response = response;
    }

    @Override
    public String createResponse() {

        if(response == null)
            throw new IllegalArgumentException("Http Response Model cannot be null");

        Dictionary<String, String> headers = response.getHeaders();
        StringBuilder finalResponse = new StringBuilder("HTTP/1.1 ");
        finalResponse.append(response.getStatusCode()).append("\r\n");
        finalResponse.append("Content-Type: ").append(headers.get("ContentType")).
        append("\r\nAge: 247555\r\n").
                append("Date: ").
                append(headers.get("Date")).
                append("\r\n\r\n");

        if(response.getFiles() != null && response.getFiles().size() > 0)
        {
            // TODO: change list of files to be of type FileModel not of type String
            for(String file : response.getFiles()) {
                finalResponse.append("<a href=''>").append(file).append("</a>");
            }
            //finalResponse.append("\r\n\r\n");
        }
        else {
            finalResponse.append("<h1>hello from http server</h1>");
            //finalResponse.append("\r\n\r\n");
        }

        return finalResponse.toString();
    }
}
