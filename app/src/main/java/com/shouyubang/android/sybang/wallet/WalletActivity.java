package com.shouyubang.android.sybang.wallet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.account.AboutActivity;
import com.shouyubang.android.sybang.utils.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WalletActivity extends AppCompatActivity {
    @BindView(R.id.over)
    TextView tvOver;
    Unbinder unbinder;
    private FrameLayout rechargeLayout;

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, WalletActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        unbinder = ButterKnife.bind(this);
        initWallet();
        rechargeLayout = (FrameLayout) findViewById(R.id.item_recharge);
        rechargeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogUtil.showDialog(WalletActivity.this, "该功能即将上线", false);
                Intent i=RechargeActivity.newIntent(getApplicationContext());
                startActivity(i);

            }
        });
    }

    //=====================================================================
    void initWallet(){//初始化钱包
        SharedPreferences sharedPreferences=getSharedPreferences("Checkpay",MODE_PRIVATE);
        //如果第一次打开，key-isInit ，初始化totalWallet-1000,设置是否初始化标签为true
        //否则，获取totalWallet，并打印
        if(!sharedPreferences.getBoolean("isInit",false)){
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putString("totalWallet","1000");
            editor.putBoolean("isInit",true);
            editor.commit();
            tvOver.setText("1000");
            Log.d("初始化totalValue 1000","成功");
        }else{
            tvOver.setText(sharedPreferences.getString("totalWallet","获取失败"));
        }
    }
    /*
    private class checktask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

        }
    }
    */
}
