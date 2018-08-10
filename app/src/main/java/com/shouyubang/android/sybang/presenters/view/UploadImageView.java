package com.shouyubang.android.sybang.presenters.view;

/**
 * 图片上传页
 */
public interface UploadImageView {
    void onUploadProcess(int percent);
    void onUploadResult(int code, String url);
}
