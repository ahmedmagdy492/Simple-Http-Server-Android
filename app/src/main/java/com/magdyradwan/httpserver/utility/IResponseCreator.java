package com.magdyradwan.httpserver.utility;

import com.magdyradwan.httpserver.utility.models.HttpResponseModel;

public interface IResponseCreator {
    String createResponse();
    void setResponseModel(HttpResponseModel response);
}
