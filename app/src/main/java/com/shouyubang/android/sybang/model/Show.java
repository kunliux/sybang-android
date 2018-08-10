package com.shouyubang.android.sybang.model;

/**
 * Created by Lenovo on 2017/8/30.
 */

public class Show {
    private String mTitle;
    private String mDetail;
    private double mPrice;
    private int mImgId;

    public Show(){
        mTitle="default";
        mDetail="this is detail";
        mPrice=123.12;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public int getImageId() {
        return mImgId;
    }

    public void setImageId(int imageId) {
        mImgId = imageId;
    }
}
