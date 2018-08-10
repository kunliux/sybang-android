package com.shouyubang.android.sybang.model;

/**
 * Created by KunLiu on 2017/8/2.
 */

public class RequestBackInfo {

    private int errorCode;
    private String errorInfo;
    private String Date;// by wurengao 10.29

    public RequestBackInfo(int code, String bad) {
        errorCode = code;
        errorInfo = bad;
    }
    // wurengao 10.29
    public RequestBackInfo(int code,String bad,String date){
        errorCode = code;
        errorInfo = bad;
        Date=date;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public String getDate(){return Date;}
}
