package com.magdyradwan.httpserver.utility.models;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class HttpResponseModel {

    private String statusCode;
    private final Dictionary<String, String> headers;
    private List<FileModel> files;
    private boolean isFile;
    private byte[] fileContent;

    public HttpResponseModel() {
        headers = new Hashtable<>();
        isFile = false;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public boolean getIsFile() {
        return isFile;
    }

    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<FileModel> getFiles() {
        return files;
    }

    public void setFiles(List<FileModel> files) {
        this.files = files;
    }

    public Dictionary<String, String> getHeaders() {
        return headers;
    }
}
