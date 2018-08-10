package com.shouyubang.android.sybang.call;

import android.content.Context;
import android.os.AsyncTask;

import com.shouyubang.android.sybang.presenters.UserServerHelper;
import com.shouyubang.android.sybang.utils.CallUtil;
import com.shouyubang.android.sybang.utils.DialogUtil;
import com.tencent.callsdk.ILVCallConstants;

/**
 * Created by KunLiu on 2017/9/16.
 */

public class StartCallTask extends AsyncTask<Void, Void, Boolean> {

    private Context mContext;
    private String mCallNum;

    public StartCallTask(Context context, String callNum) {
        mContext = context;
        mCallNum = callNum;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return UserServerHelper.startCall(mCallNum);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(success) {
            DialogUtil.showToast(mContext, "连接成功");
            CallUtil.makeCall(mContext, ILVCallConstants.CALL_TYPE_VIDEO, mCallNum);
        } else {
            DialogUtil.showToast(mContext, "连接失败");
        }
    }
}
