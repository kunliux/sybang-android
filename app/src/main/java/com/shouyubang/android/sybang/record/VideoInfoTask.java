package com.shouyubang.android.sybang.record;

import android.os.AsyncTask;
import android.util.Log;

import com.shouyubang.android.sybang.model.CurVideoInfo;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.model.RequestBackInfo;
import com.shouyubang.android.sybang.model.VideoMsg;
import com.shouyubang.android.sybang.presenters.AccountHelper;
import com.shouyubang.android.sybang.presenters.UserServerHelper;
import com.shouyubang.android.sybang.utils.TimeUtil;

/**
 * Created by KunLiu on 2017/7/26.
 */

class VideoInfoTask extends AsyncTask<Void, Void, RequestBackInfo> {
    private static final String TAG = "VideoInfoTask";

    private AccountHelper mUploadHelper;

    VideoInfoTask(AccountHelper uploadHelper, String path) {
        mUploadHelper = uploadHelper;
        String userId = MySelfInfo.getInstance().getId();
        String fileName = getFileName(path)+".mp4";
        CurVideoInfo.getInstance().setUserId(userId);
        CurVideoInfo.getInstance().setVideoUrl(fileName);
        CurVideoInfo.getInstance().setUploadTime(TimeUtil.getNowTime());
    }

    @Override
    protected RequestBackInfo doInBackground(Void... params) {
        Log.i(TAG, "User submit video info");
        return UserServerHelper.getInstance().uploadVideo(CurVideoInfo.getInstance());
    }

    @Override
    protected void onPostExecute(RequestBackInfo result) {
        if (result.getErrorCode() >= 0) {
            mUploadHelper.mUploadView.uploadSuccess();
        } else {
            mUploadHelper.mUploadView.uploadFail(TAG, result.getErrorCode(), result.getErrorInfo());
        }
    }

    public String getFileName(String path){

        int start=path.lastIndexOf("/");
        int end=path.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return path.substring(start+1,end);
        }else{
            return null;
        }
    }


}
