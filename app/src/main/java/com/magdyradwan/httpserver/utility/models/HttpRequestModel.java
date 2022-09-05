package com.magdyradwan.httpserver.utility.models;

import java.util.Dictionary;
import java.util.Hashtable;

public class HttpRequestModel {
    private final Dictionary<String, String> headers;
    private final Dictionary<String, String> queryParams;
    private String method;
    private String httpVersion;

    public HttpRequestModel() {
        headers = new Hashtable<>();
        queryParams = new Hashtable<>();
    }

    public Dictionary<String, String> getQueryParams() {
        return queryParams;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Dictionary<String, String> getHeaders() {
        return headers;
    }
}
