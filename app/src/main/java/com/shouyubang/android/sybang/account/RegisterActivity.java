package com.shouyubang.android.sybang.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;
import com.shouyubang.android.sybang.MainActivity;
import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.presenters.AccountHelper;
import com.shouyubang.android.sybang.presenters.view.LoginView;
import com.shouyubang.android.sybang.utils.Constants;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity implements LoginView {

    private static final String TAG = "RegisterActivity";

    @BindView(R.id.input_phone)
    EditText mPhoneText;
    @BindView(R.id.input_password)
    EditText mPasswordText;
    @BindView(R.id.btn_signup)
    Button mRegisterButton;
    @BindView(R.id.link_login)
    TextView mLoginTextView;
    @BindView(R.id.input_auth_code)
    EditText mAuthCodeText;
    @BindView(R.id.check_button)
    Button mCheckButton;

    private ProgressDialog progressDialog;
    AccountHelper mRegisterHelper;

    private TimerTask tt;
    private Timer tm;
    private int TIME = 60;//倒计时60s这里应该多设置些因为mob后台需要60s,我们前端会有差异的建议设置90，100或者120
    public String country="86";//这是中国区号，如果需要其他国家列表，可以使用getSupportedCountries();获得国家区号
    private String phone;

    private static final int CODE_REPEAT = 1; //重新发送
    private static final int CODE_REGISTER = 1024; //重新发送
    Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_REGISTER) {
                register();
            } else if (msg.what == CODE_REPEAT) {
                mCheckButton.setEnabled(true);
                mRegisterButton.setEnabled(true);
                tm.cancel();//取消任务
                tt.cancel();//取消任务
                TIME = 60;//时间重置
                mCheckButton.setText("获取验证码");
            }else {
                mCheckButton.setText(TIME + "秒后重发");
            }
        }
    };
    //回调
    EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (data instanceof Throwable) {
                Throwable throwable = (Throwable)data;
                String msg = throwable.getMessage();
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
            } else if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    toast("验证成功");
                    hd.sendEmptyMessage(CODE_REGISTER);
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){       //获取验证码成功
                    toast("已发送");
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//如果你调用了获取国家区号类表会在这里回调
                    //返回支持发送验证码的国家列表
                }
            }
        }
    };
    //吐司的一个小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, RegisterActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        MobSDK.init(this, Constants.MOB_APP_KEY, Constants.MOB_APP_SECRET);
        SMSSDK.registerEventHandler(eh); //注册短信回调（记得销毁，避免泄露内存）

        mLoginTextView.setPaintFlags(mLoginTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Linkify.addLinks(mLoginTextView, Linkify.ALL);
        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Sign In Activity activated.");
                // this is where you should start the signin Activity
                Intent i = LoginActivity.newIntent(RegisterActivity.this);
                startActivity(i);

            }
        });

        mCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPhoneValid())
                    alterWarning();
            }
        });

        mRegisterHelper = new AccountHelper(this, this);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    public void register() {
        Log.d(TAG, "Start register...");

        progressDialog = new ProgressDialog(RegisterActivity.this, R.style.AppTheme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在注册...");
        progressDialog.show();

        String phone = mPhoneText.getText().toString().trim();
        String password = mPasswordText.getText().toString().trim();

        //注册一个账号
        mRegisterHelper.standardRegister("86-" + phone, password);
    }


    public void validate() {
        //获得用户输入的验证码
        String password = mPasswordText.getText().toString();
        //获得用户输入的验证码
        String code = mAuthCodeText.getText().toString().replaceAll("/s","");

        if (password.isEmpty() || password.length() < 5 || password.length() > 16) {
            mPasswordText.setError(getString(R.string.error_invalid_password));
        } else {
            mPasswordText.setError(null);
        }

        if (!TextUtils.isEmpty(code)) {//判断验证码是否为空
            //验证
            SMSSDK.submitVerificationCode(country, phone, code);
        }else{//如果用户输入的内容为空，提醒用户
            toast("请输入验证码");
        }
    }

    private boolean isPhoneValid() {
        phone = mPhoneText.getText().toString().trim().replaceAll("/s","");
        //定义需要匹配的正则表达式的规则
        String REGEX_MOBILE_SIMPLE =  "^1[34578][0-9]{9}$";
        //把正则表达式的规则编译成模板
        Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);
        //把需要匹配的字符给模板匹配，获得匹配器
        Matcher matcher = pattern.matcher(phone);
        // 通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
        if (matcher.find()) {//匹配手机号是否存在
            mPhoneText.setError(null);
            return true;
        } else {
            mPhoneText.setError(getString(R.string.error_invalid_phone));
            return false;
        }
    }

    //弹窗确认下发
    private void alterWarning() {
        // 通过sdk发送短信验证
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("确认手机号码"); //设置标题
        builder.setMessage("我们将发送验证码到下面的号码：" + phone); //设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                // 2. 通过sdk发送短信验证（请求获取短信验证码，在监听(eh)中返回）
                SMSSDK.getVerificationCode(country, phone);
                //做倒计时操作
                mCheckButton.setEnabled(false);
                mRegisterButton.setEnabled(true);
                tm = new Timer();
                tt = new TimerTask() {
                    @Override
                    public void run() {
                        hd.sendEmptyMessage(TIME--);
                    }
                };
                tm.schedule(tt,0,1000);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(RegisterActivity.this, "已取消", Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    @Override
    public void loginSuccess() {
        progressDialog.dismiss();
        jumpIntoMainActivity();
    }

    @Override
    public void loginFail(String module, int errCode, String errMsg) {
        progressDialog.dismiss();
        Toast.makeText(this, "code " + errCode + " : " + errMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 直接跳转主界面
     */
    private void jumpIntoMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //销毁短信注册
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
        SMSSDK.unregisterEventHandler(eh);
    }

}