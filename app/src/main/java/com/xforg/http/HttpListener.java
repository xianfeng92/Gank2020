package com.xforg.http;



public interface HttpListener {
    void onSuccess(Object result);

    void onFailure(int errorType, String message);
}
