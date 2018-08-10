package com.shouyubang.android.sybang.account;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.shouyubang.android.sybang.BuildConfig;
import com.shouyubang.android.sybang.R;

public class AboutActivity extends AppCompatActivity {
	
	private TextView mVersionView;

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, AboutActivity.class);
        return i;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		findViews();
		initViewData();
	}

    private void findViews() {
		mVersionView = (TextView) findViewById(R.id.version_detail);
	}

	private void initViewData() {
        mVersionView.setText("手语帮 " + BuildConfig.VERSION_NAME +" 测试版");
	}
}
