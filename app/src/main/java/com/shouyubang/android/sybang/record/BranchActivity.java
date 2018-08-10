package com.shouyubang.android.sybang.record;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.badge.DragIndicatorView;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.presenters.UserServerHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BranchActivity extends AppCompatActivity {

    @BindView(R.id.branch_record_btn)
    AppCompatButton mBranchRecordBtn;
    @BindView(R.id.branch_message_btn)
    AppCompatButton mBranchMessageBtn;
    @BindView(R.id.indicator_view)
    DragIndicatorView mIndicatorView;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, BranchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        ButterKnife.bind(this);
        mIndicatorView.setVisibility(View.INVISIBLE);
        new UnreadCountTask(MySelfInfo.getInstance().getId()).execute();
    }


    @OnClick({R.id.branch_record_btn, R.id.branch_message_btn})
    public void onViewClicked(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.branch_record_btn:
                i = RecordActivity.newIntent(BranchActivity.this);
                startActivity(i);
                break;
            case R.id.branch_message_btn:
                i = VideoListActivity.newIntent(BranchActivity.this);
                startActivity(i);
                break;
        }
    }

    private class UnreadCountTask extends AsyncTask<Void, Void, String> {
        private static final String TAG = "VideoInfoVideo";

        private String mUserId;

        UnreadCountTask(String userId) {
            mUserId = userId;
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.i(TAG, "Staff get task list.");
            return UserServerHelper.countUnreadVideo(mUserId);
        }

        @Override
        protected void onPostExecute(String unread) {
            if(!unread.equals("0")) {
                mIndicatorView.setVisibility(View.VISIBLE);
                mIndicatorView.setText(unread);
            }
        }
    }
}
