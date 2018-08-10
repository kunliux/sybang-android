package com.shouyubang.android.sybang.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shouyubang.android.sybang.MainActivity;
import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.utils.Constants;
import com.shouyubang.android.sybang.wallet.RechargeActivity;
import com.shouyubang.android.sybang.wallet.WalletActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	@BindView(R.id.Btn_paySuccess)
	TextView tvPay;
	@BindView(R.id.img)
	ImageView img;
	Unbinder unbinder;

    private IWXAPI api;
	private String mbillId;
	AlertDialog.Builder builder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pay_result);
		unbinder = ButterKnife.bind(this);


    	api = WXAPIFactory.createWXAPI(this, Constants.APPID_WACHAT_PAY);
        api.handleIntent(getIntent(), this);

    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@SuppressLint("LongLogTag")
	@Override
	public void onResp(final BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			String text;
			switch (resp.errCode){
				case 0:text = "支付成功！";
					//读取本次充值的积分，读取总的积分
					//设置积分为总积分
					SharedPreferences sharedPreferences=getSharedPreferences("Checkpay",MODE_PRIVATE);
					String addValue=sharedPreferences.getString("addValue","0");
					String totalWallet=sharedPreferences.getString("totalWallet","0");
					Integer total=Integer.parseInt(addValue)+Integer.parseInt(totalWallet);

					Log.d("获取总积分totalValue",totalWallet);
					Log.d("获取积分addValue",addValue);
					Editor editor=sharedPreferences.edit();
					editor.putString("totalWallet",total.toString());
					Log.d("当前总积分addValue",total+"");
					editor.commit();
					break;
				case -2:text = "支付取消";
					break;
				default:text = "支付失败！";
					break;
			}
			builder = new AlertDialog.Builder(this);
			builder.setTitle("支付结果");
			builder.setMessage(text);
			if(resp.errCode==0) {
				//mbillId = Preferences.getPayBillId(WXPayEntryActivity.this);
				//new checkoutOrder().execute();
			} else {
				tvPay.setVisibility(View.INVISIBLE);
				img.setVisibility(View.INVISIBLE);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent intent = new Intent(WXPayEntryActivity.this, MainActivity.class);
						startActivity(intent);
						//Preferences.setPayBillId(WXPayEntryActivity.this, null);
						WXPayEntryActivity.this.finish();
					}
				});

				builder.setCancelable(false);
				builder.show();
			}

		}
	}

}