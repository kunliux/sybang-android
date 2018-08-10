package com.shouyubang.android.sybang;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.shouyubang.android.sybang.account.LoginActivity;
import com.shouyubang.android.sybang.utils.BottomNavigationViewHelper;
import com.shouyubang.android.sybang.utils.CallUtil;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.tencent.callsdk.ILVCallListener;
import com.tencent.callsdk.ILVCallManager;
import com.tencent.callsdk.ILVIncomingListener;
import com.tencent.callsdk.ILVIncomingNotification;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ILVIncomingListener, ILVCallListener {

    private static final String TAG = "MainActivity";

    private CoordinatorLayout layoutRoot;

    private AlertDialog mIncomingDlg;
    private int mCurIncomingId;

    private static final int REQUEST_PHONE_PERMISSIONS = 20;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutRoot = (CoordinatorLayout) findViewById(R.id.main_container);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);//导航栏
        BottomNavigationViewHelper.disableShiftMode(navigation);//设置移动轮播的模式
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = HomeFragment.newInstance(null, null);
                        break;
                    case R.id.navigation_dashboard:
                        selectedFragment = CareerFragment.newInstance();
                        break;
                   case R.id.navigation_news:
                        selectedFragment = NewsFragment.newInstance(null, null);
                        break;
                    case R.id.navigation_user:
                        selectedFragment = UserFragment.newInstance();
                        break;
                    default:
                        return false;
                }
                //transaction:事务
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, selectedFragment);///用选择的fragment替换掉当前的fragment
                transaction.commit();
                return true;
            }

        });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, HomeFragment.newInstance(null, null));
        transaction.commit();
        // 设置通话回调
        ILVCallManager.getInstance().addIncomingListener(this);
        ILVCallManager.getInstance().addCallListener(this);
        checkPermission();//检查权限
    }

    /**
     * Permission Check
     */
    void checkPermission() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if ((checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.ACCESS_NETWORK_STATE);
            if ((checkSelfPermission(Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CHANGE_NETWORK_STATE);
            if (permissionsList.size() != 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PHONE_PERMISSIONS);
            }
        }
    }

    @Override
    public void onCallEstablish(int callId) {
        Log.i(TAG, "Call Establish :"+callId);
    }

    @Override
    public void onCallEnd(int callId, int endResult, String endInfo) {
        if (mCurIncomingId == callId){
            mIncomingDlg.dismiss();
        }
        Log.i(TAG, "End Call:"+endResult+"-"+endInfo+"/"+callId);
        Log.e("XDBG_END", "onCallEnd->id: "+callId+"|"+endResult+"|"+endInfo);
    }

    @Override
    public void onException(int iExceptionId, int errCode, String errMsg) {
        Log.i(TAG, "Exception id:"+iExceptionId+", "+errCode+"-"+errMsg);
    }

    @Override
    public void onNewIncomingCall(final int callId, final int callType, final ILVIncomingNotification notification) {
        Log.i(TAG, "New Call from:"+notification.getSender()+"/"+callId+"-"+notification);
        if (null != mIncomingDlg){  // 关闭遗留来电对话框
            mIncomingDlg.dismiss();
        }
        mCurIncomingId = callId;
        mIncomingDlg = new AlertDialog.Builder(this)
                .setTitle("新来电  "+notification.getSender())
                .setMessage(notification.getNotifDesc())
                .setPositiveButton("接听", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CallUtil.acceptCall(MainActivity.this, callId, notification.getSponsorId(), callType);
                        Log.i(TAG, "Accept Call :"+mCurIncomingId);
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int ret = ILVCallManager.getInstance().rejectCall(mCurIncomingId);
                        Log.i(TAG, "Reject Call:"+ret+"/"+mCurIncomingId);
                    }
                })
                .create();
        mIncomingDlg.setCanceledOnTouchOutside(false);
        mIncomingDlg.show();
    }


    @Override
    protected void onDestroy() {//清除监听器,防止内存溢出
        ILVCallManager.getInstance().removeIncomingListener(this);
        ILVCallManager.getInstance().removeCallListener(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }
}
