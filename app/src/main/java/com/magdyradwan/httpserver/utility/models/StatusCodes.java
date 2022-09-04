package com.magdyradwan.httpserver.utility.models;

public class StatusCodes {
    private StatusCodes() {}

    public final static String NotFound = "404 Not Found";
    public final static String OK = "200 OK";
    public final static String BadRequest = "400 Bad Request";
    public final static String Forbidden = "403 Forbidden";
    public final static String MethodNotAllowed = "405 Method Not Allowed";
}
