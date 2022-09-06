package com.magdyradwan.httpserver.utility;

import android.os.Environment;
import android.util.Log;

import com.magdyradwan.httpserver.utility.models.FileModel;
import com.magdyradwan.httpserver.utility.models.HttpResponseModel;
import com.magdyradwan.httpserver.utility.models.StatusCodes;

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

        if(!response.getStatusCode().equals(StatusCodes.OK)) {
            finalResponse.append("<h1>").append(response.getStatusCode()).append("</h1>");
            finalResponse.append("\r\n\r\n");
            return finalResponse.toString();
        }

        if(response.getFiles() != null && response.getFiles().size() > 0) {
            finalResponse.append("<h1>Index Of </h1><ul style='list-style: none;'>");
            finalResponse.append("<li><a href='..'>..</a></li>");
            for(FileModel file : response.getFiles()) {
                finalResponse.append("<li><a href='/?path=")
                        .append(file.getFullPath())
                        .append("'>").append(file.getName()).append("</a></li>");
            }
            finalResponse.append("</ul>\r\n\r\n");
        }
        else {
            finalResponse.append("<h1>Index of /</h1><ul style='list-style: none;'><li><a href=\"..\">..</a></li></ul>");
            finalResponse.append("\r\n\r\n");
        }

        return finalResponse.toString();
    }
}
