package com.shouyubang.android.sybang.career;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.adapter.ApplicationAdapter;
import com.shouyubang.android.sybang.model.ApplicationInfo;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.presenters.UserServerHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KunLiu on 17/8/3.
 *
 */
public class ApplicationActivity extends AppCompatActivity {

    private static final String TAG = "ApplicationActivity";

    private RecyclerView mApplicationInfoRecyclerView;
    private List<ApplicationInfo> mApplicationInfos = new ArrayList<>();
    private ApplicationAdapter mAdapter;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, ApplicationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        mApplicationInfoRecyclerView = (RecyclerView) findViewById(R.id.application_recycler_view);
        mApplicationInfoRecyclerView.setLayoutManager(new LinearLayoutManager(ApplicationActivity.this));
        mApplicationInfoRecyclerView.addItemDecoration(new DividerItemDecoration(ApplicationActivity.this, DividerItemDecoration.VERTICAL));

        new ApplicationInfoListTask(MySelfInfo.getInstance().getId()).execute();
    }

    private void setupAdapter() {

        if (mAdapter == null) {
            mAdapter = new ApplicationAdapter(ApplicationActivity.this, mApplicationInfos);
            mApplicationInfoRecyclerView.setAdapter(mAdapter);
        } else {
            Log.d(TAG, "Size:" + mApplicationInfos.size());
            mAdapter.setApplications(mApplicationInfos);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ApplicationInfoListTask extends AsyncTask<Void, Void, List<ApplicationInfo>> {

        private String mUserId;

        ApplicationInfoListTask(String userId) {
            mUserId = userId;
        }

        @Override
        protected List<ApplicationInfo> doInBackground(Void... params) {
            return UserServerHelper.getApplications(mUserId);
        }

        @Override
        protected void onPostExecute(List<ApplicationInfo> jobs) {
            if(jobs.size() == 0) {
                return;
            }
            mApplicationInfos = jobs;
            setupAdapter();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupAdapter();
    }
}
