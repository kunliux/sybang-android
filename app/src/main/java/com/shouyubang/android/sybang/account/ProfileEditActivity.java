package com.shouyubang.android.sybang.account;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.shouyubang.android.sybang.App;
import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.model.UserProfile;
import com.shouyubang.android.sybang.presenters.UserServerHelper;
import com.shouyubang.android.sybang.utils.DialogUtil;

public class ProfileEditActivity extends AppCompatActivity {

    private static final String TAG = "ProfileEditActivity";

    int currentStep = 0;

    private static UserProfile mUserInfo;

    MenuItem menuItem;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, ProfileEditActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        mUserInfo = new UserProfile();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.profile_content, ProfileBasicFragment.newInstance());
        transaction.commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.profile_content, fragment);
//        if(!(fragment instanceof ProfileBasicFragment))
            transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_profile_edit, menu);
        menuItem = menu.findItem(R.id.action_next_step);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_next_step:
                if(currentStep >= 2) {
                    menuItem.setTitle("完成");
                }
                currentStep++;
                break;
            case android.R.id.home:
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                currentStep--;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(currentStep == 0)
            finish();
        else if(currentStep <= 2) {
            menuItem.setTitle("下一步");
        }
        currentStep--;
        super.onBackPressed();
    }

    public UserProfile getUserInfo() {
        return mUserInfo;
    }

    public void setUserBasicInfo(UserProfile userInfo) {
        mUserInfo.setAvatarUrl(userInfo.getAvatarUrl());
        mUserInfo.setNickname(userInfo.getNickname());
        mUserInfo.setNationality(userInfo.getNationality());
        mUserInfo.setNativePlace(userInfo.getNativePlace());
        mUserInfo.setPhone(userInfo.getPhone());
        mUserInfo.setEmergencyContact(userInfo.getEmergencyContact());
        mUserInfo.setIdNumber(userInfo.getIdNumber());
        mUserInfo.setDisabilityNumber(userInfo.getDisabilityNumber());
    }

    public void setUserPresentInfo(UserProfile userInfo) {
        mUserInfo.setReadingLevel(userInfo.getReadingLevel());
        mUserInfo.setWritingLevel(userInfo.getWritingLevel());
        mUserInfo.setInsurance(userInfo.getInsurance());
        mUserInfo.setCity(userInfo.getCity());
        mUserInfo.setEducation(userInfo.getEducation());
        mUserInfo.setSpeciality(userInfo.getSpeciality());
        mUserInfo.setInterests(userInfo.getInterests());
    }

    public void setUserWorkInfo(UserProfile userInfo) {
        mUserInfo.setExperience(userInfo.getExperience());
        mUserInfo.setPosition(userInfo.getPosition());
        mUserInfo.setSalary(userInfo.getSalary());
        mUserInfo.setIntroduction(userInfo.getIntroduction());
    }

    public void postUserInfo() {
        new UserInfoTask(mUserInfo).execute();
    }

    private class UserInfoTask extends AsyncTask<Void, Void, Boolean> {

        private UserProfile mUserInfo;

        UserInfoTask(UserProfile userInfo) {
            mUserInfo = userInfo;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i(TAG, "User submit user info");
            return UserServerHelper.addUserInfo(mUserInfo);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                DialogUtil.showToast(App.getContext(), "完善信息成功");
                finish();
            } else {
                DialogUtil.showToast(App.getContext(), "完善信息失败");
            }
        }
    }
}
