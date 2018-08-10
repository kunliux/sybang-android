package com.shouyubang.android.sybang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.utils.ConnectionChangeReceiver;
import com.shouyubang.android.sybang.utils.Constants;
import com.shouyubang.android.sybang.utils.LogConstants;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.core.ILiveLoginManager;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private BroadcastReceiver recv;
    private ConnectionChangeReceiver netWorkStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ILiveLoginManager.getInstance().setUserStatusListener(new ILiveLoginManager.TILVBStatusListener() {
            @Override
            public void onForceOffline(int error, String message) {
                switch (error){
                    case ILiveConstants.ERR_KICK_OUT:
                        Log.w(TAG, "onForceOffline->entered!");
                        Log.d(TAG, LogConstants.ACTION_HOST_KICK + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "on force off line");
                        Toast.makeText(BaseActivity.this, "您的帐号已在其它地方登陆", Toast.LENGTH_SHORT).show();
                        MySelfInfo.getInstance().clearCache(getBaseContext());
                        getBaseContext().sendBroadcast(new Intent(Constants.BD_EXIT_APP));
                        break;
                    case ILiveConstants.ERR_EXPIRE:
                        Log.w(TAG, "onUserSigExpired->entered!");
                        Toast.makeText(getBaseContext(), "onUserSigExpired|"+message, Toast.LENGTH_SHORT).show();
                        getBaseContext().sendBroadcast(new Intent(Constants.BD_EXIT_APP));
                        break;
                }
            }
        });

        recv = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.BD_EXIT_APP)){
                    Log.d("BaseActivity", LogConstants.ACTION_HOST_KICK + LogConstants.DIV + MySelfInfo.getInstance().getId() + LogConstants.DIV + "on force off line");
                    finish();
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BD_EXIT_APP);
        registerReceiver(recv, filter);

        //监听网络变化
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new ConnectionChangeReceiver();
        }
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(netWorkStateReceiver, filter2);
    }



    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(recv);
            unregisterReceiver(netWorkStateReceiver);
        }catch (Exception e){
        }
        super.onDestroy();
    }
}