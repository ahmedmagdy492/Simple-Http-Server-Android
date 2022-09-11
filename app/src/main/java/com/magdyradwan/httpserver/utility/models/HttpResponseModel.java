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
    private String fileType;
    private String fullPath;

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }

    public HttpResponseModel() {
        headers = new Hashtable<>();
        isFile = false;
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
