package com.shouyubang.android.sybang.wallet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.model.RequestBackInfo;
import com.shouyubang.android.sybang.presenters.Presenter;
import com.shouyubang.android.sybang.presenters.UserServerHelper;
import com.shouyubang.android.sybang.utils.Constants;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import org.json.JSONException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wurengao on 17-10-27.
 */

public class RechargeActivity extends AppCompatActivity {

    private int initColor;
    private int clickColor;
    private String CreditsValue;
    int totalFee=0;
    @BindView(R.id.recharge_price)
    EditText mRecharge;
    @BindView(R.id.Btn_pay1)
    Button mBtnPay1;
    @BindView(R.id.Btn_pay2)
    Button mBtnPay2;
    @BindView(R.id.Btn_pay3)
    Button mBtnPay3;
    @BindView(R.id.Btn_pay4)
    Button mBtnPay4;
    @BindView(R.id.TV_credit)
    TextView mTvCredits;
    @BindView(R.id.wechat)
    FrameLayout mWecaht;
    Unbinder unbinder;

    public static Intent newIntent(Context packageContext){
        Intent i= new Intent(packageContext,RechargeActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        unbinder = ButterKnife.bind(this);

        initColor=getResources().getColor(R.color.cardview_light_background);;
        clickColor=getResources().getColor(R.color.colorPrimary);

        BtnClickAction();
        mRecharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                initColor();
                String money=s+"00";
                Integer moneyInt=Integer.parseInt(money);
                money=moneyInt+"";
                mTvCredits.setText(money);
            }
        });
    }

    //待重构
    private void BtnClickAction(){
        mBtnPay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecharge.setText("5");
                initColor();
                view.setBackgroundColor(clickColor);
                mTvCredits.setText("500");
            }
        });
        mBtnPay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecharge.setText("10");
                initColor();
                view.setBackgroundColor(clickColor);
                mTvCredits.setText("1000");
            }
        });
        mBtnPay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecharge.setText("20");
                initColor();
                view.setBackgroundColor(clickColor);
                mTvCredits.setText("2000");
            }
        });
        mBtnPay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecharge.setText("50");
                initColor();
                view.setBackgroundColor(clickColor);
                mTvCredits.setText("5000");
            }
        });
        //微信支付流程
        //传信息给服务器（ ....ip地址）
        //接受预付制单号
        //payUTil dopay(id,contact)
        //===>微信界面
        //finish();
        mWecaht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String priceStr=mTvCredits.getText().toString();
                totalFee=Integer.parseInt(priceStr);//得到价格
                //Log.d("支付金额",totalFee+"0");
                new payTask().execute();
                RechargeActivity.this.finish();
            }
        });
    }

    private void initColor(){
        mBtnPay1.setBackgroundColor(initColor);
        mBtnPay2.setBackgroundColor(initColor);
        mBtnPay3.setBackgroundColor(initColor);
        mBtnPay4.setBackgroundColor(initColor);
    }
    private class payTask extends AsyncTask<Void,Void,RequestBackInfo> {
        @Override
        protected RequestBackInfo doInBackground(Void... params) {
            RequestBackInfo requestBackInfo=null;
            //发送信息给服务器，订单号
            requestBackInfo=new UserServerHelper().
                    getPayid(ILiveLoginManager.getInstance().getMyUserId(),null,"积分充值",totalFee);
            return requestBackInfo;
        }
        @Override
        protected void onPostExecute(RequestBackInfo requestBackInfo){
            Log.d("订单号",requestBackInfo.getDate());
            String payID=requestBackInfo.getDate();
            setCheckPay();//将充值积分储存，用于以后刷新
            try {
                Log.d("Param",PayUtil.getParam(payID).toString());

                PayUtil.doWXPay(PayUtil.getParam(payID).toString(),getApplicationContext());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*
            WX1217752501201407033233368018 ；//微信开发平台的例子
            wx2017103020145768e5d2db670583517710；//你给的
            */
        }
    }

    void setCheckPay(){
        SharedPreferences sharedPreferences=getSharedPreferences("Checkpay",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("addValue",mTvCredits.getText().toString());
        Log.d("设置积分值","成功");
        editor.apply();
    }

}
