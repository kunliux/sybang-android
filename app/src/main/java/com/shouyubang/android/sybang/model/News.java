package com.shouyubang.android.sybang.model;



import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by KunLiu on 2017/8/31.
 */

public class News {
    private String mTitle;
    private String mClassify;
    private String mPress;
    private String mTime;
    private String mImageId;
    private int mId;//新闻的ID,temp



    public News(String t/*,String p,*/, String c, int id, String mImageId){
        mTitle=t;
        //mPress=p;
        mClassify =c;
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        mTime=dateFormater.format(new Date());
        mId=id;
        this.mImageId=mImageId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getClassify() {
        return mClassify;
    }

    public void setClassify(String classify) {
        mClassify = classify;
    }

    public String getPress() {
        return mPress;
    }

    public void setPress(String press) {
        mPress = press;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getImageId() {
        return mImageId;
    }

    public void setImageId(String imageId) {
        mImageId = imageId;
    }
}
