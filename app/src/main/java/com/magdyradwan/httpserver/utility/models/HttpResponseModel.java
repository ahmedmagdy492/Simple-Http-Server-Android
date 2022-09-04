package com.magdyradwan.httpserver.utility.models;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class HttpResponseModel {

    private String statusCode;
    private final Dictionary<String, String> headers;
    private List<String> files;

    public HttpResponseModel() {
        headers = new Hashtable<>();
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public Dictionary<String, String> getHeaders() {
        return headers;
    }
}
