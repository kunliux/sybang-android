package com.shouyubang.android.sybang.presenters;

import android.os.AsyncTask;
import android.util.Log;

import com.shouyubang.android.sybang.App;
import com.shouyubang.android.sybang.account.ProfileInfoActivity;
import com.shouyubang.android.sybang.career.JobDetailActivity;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.model.UserProfile;
import com.shouyubang.android.sybang.presenters.view.ProfileView;
import com.shouyubang.android.sybang.utils.DialogUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;

import java.util.List;

/**
 * 用户资料获取
 */
public class ProfileInfoHelper {
    private String TAG = getClass().getName();
    private ProfileView mView;

    public ProfileInfoHelper(ProfileView view){
        mView = view;
    }

    public void getMyProfile(){
        new ProfileInfoTask().execute();
    }

    public void setMyProfile(String nickName, String sign){

        new UpdateInfoTask(nickName, sign).execute();

        TIMFriendshipManager.getInstance().setNickName(nickName, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.w(TAG, "setNickName->error:" + i + "," + s);
            }

            @Override
            public void onSuccess() {
                getMyProfile();
            }
        });

        TIMFriendshipManager.getInstance().setSelfSignature(sign, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.w(TAG, "setSelfSignature->error:" + i + "," + s);
            }

            @Override
            public void onSuccess() {
                getMyProfile();
            }
        });
    }

    public void setMyAvatar(String url){

        new UpdateAvatarTask(url).execute();

        TIMFriendshipManager.getInstance().setFaceUrl(url, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.w(TAG, "setMyAvatar->error:" + i + "," + s);
            }

            @Override
            public void onSuccess() {
                getMyProfile();
            }
        });
    }
    //获取用户的ID
    private class ProfileInfoTask extends AsyncTask<Void, Void, UserProfile> {

        @Override
        protected UserProfile doInBackground(Void... params) {
            //通过用户的ID,联网获取一大堆的用户信息
            return UserServerHelper.getProfile(MySelfInfo.getInstance().getId());
        }

        @Override
        protected void onPostExecute(UserProfile profile) {
            if(null != profile);
            //将用户的一大堆信息传给实现的updateUserProfile()方法,实现用户信息的更新
                mView.updateUserProfile(profile);
        }
    }

    private class UpdateInfoTask extends AsyncTask<Void, Void, Boolean> {

        private String mNickname;
        private String mSign;

        UpdateInfoTask(String nickname, String sign) {
            mNickname = nickname;
            mSign = sign;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return UserServerHelper.updateProfile(MySelfInfo.getInstance().getId(), mNickname, mSign);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success) {
                DialogUtil.showToast(App.getContext(), "修改成功");
            } else {
                DialogUtil.showToast(App.getContext(), "修改失败");
            }
        }
    }

    private class UpdateAvatarTask extends AsyncTask<Void, Void, Boolean> {

        private String mAvatarUrl;

        UpdateAvatarTask(String avatarUrl) {
            mAvatarUrl = avatarUrl;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return UserServerHelper.updateAvatar(MySelfInfo.getInstance().getId(), mAvatarUrl);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success) {
                DialogUtil.showToast(App.getContext(), "修改成功");
            } else {
                DialogUtil.showToast(App.getContext(), "修改失败");
            }
        }
    }
}
