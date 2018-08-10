package com.shouyubang.android.sybang;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.shouyubang.android.sybang.account.LoginActivity;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.presenters.AccountHelper;
import com.shouyubang.android.sybang.presenters.view.LoginView;

public class SplashActivity extends AppCompatActivity implements LoginView {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取个人数据本地缓存
        MySelfInfo.getInstance().getCache(getApplicationContext());

        AccountHelper loginHelper = new AccountHelper(SplashActivity.this, this);
        if (needLogin()) {//本地没有账户需要登录
            jumpIntoLoginActivity();
        } else {
            //有账户登录直接IM登录<<获取账户ID与用户名直接登录
            loginHelper.iLiveLogin(MySelfInfo.getInstance().getId(), MySelfInfo.getInstance().getUserSig());
        }
    }

    /**
     * 判断是否需要登录
     *
     * @return true 代表需要重新登录
     */
    public boolean needLogin() {
        if (MySelfInfo.getInstance().getId() != null) {
            return false;//有账号不需要登录
        } else {
            return true;//需要登录
        }

    }

    @Override
    public void loginSuccess() {
//        Toast.makeText(SplashActivity.this, "" + MySelfInfo.getInstance().getId() + " 登录 ", Toast.LENGTH_SHORT).show();
        jumpIntoMainActivity();
    }

    @Override
    public void loginFail(String module, int errCode, String errMsg) {
        Log.i(TAG, "Login FAIL " + errCode + " : " + errMsg);
//        Toast.makeText(SplashActivity.this, "登录失败" + MySelfInfo.getInstance().getId() + " : "+errMsg, Toast.LENGTH_SHORT).show();
        jumpIntoLoginActivity();
    }

    /**
     * 直接跳转主界面
     */
    private void jumpIntoMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent starter = MainActivity.newIntent(SplashActivity.this);
                startActivity(starter);
                finish();
                overridePendingTransition(R.anim.stand,R.anim.splash);
            }
        },2000);
    }

    /**
     * 直接跳转登录界面
     */
    private void jumpIntoLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent starter = LoginActivity.newIntent(SplashActivity.this);
                startActivity(starter);
                finish();
                overridePendingTransition(R.anim.stand,R.anim.splash);
            }
        },2000);//设置闪图停留的时间,时间不宜太短,会造成短暂黑屏
    }
}
