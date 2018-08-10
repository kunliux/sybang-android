package com.shouyubang.android.sybang.presenters.view;

/**
 * 登录回调
 */
public interface LoginView extends MVPView {

    void loginSuccess();

    void loginFail(String module, int errCode, String errMsg);
}
