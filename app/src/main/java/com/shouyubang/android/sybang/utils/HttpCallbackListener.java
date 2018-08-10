package com.shouyubang.android.sybang.utils;

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}