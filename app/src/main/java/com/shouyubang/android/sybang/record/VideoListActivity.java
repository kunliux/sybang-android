package com.shouyubang.android.sybang.record;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.adapter.VideoAdapter;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.model.VideoMsg;
import com.shouyubang.android.sybang.presenters.UserServerHelper;

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    private static final String TAG = "VideoListActivity";

    private RecyclerView mVideoRecyclerView;
    private List<VideoMsg> mVideos = new ArrayList<>();
    private VideoAdapter mAdapter;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, VideoListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        mVideoRecyclerView = (RecyclerView) findViewById(R.id.video_recycler_view);
        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(VideoListActivity.this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        new VideoListTask(MySelfInfo.getInstance().getId()).execute();
    }

    private void setupAdapter() {

        if (mAdapter == null) {
            mAdapter = new VideoAdapter(VideoListActivity.this, mVideos);
            mVideoRecyclerView.setAdapter(mAdapter);
        } else {
            Log.d(TAG, "Size:" + mVideos.size());
            mAdapter.setVideos(mVideos);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class VideoListTask extends AsyncTask<Void, Void, List<VideoMsg>> {
        private static final String TAG = "VideoListTask";

        private String mUserId;

        VideoListTask(String userId) {
            mUserId = userId;
        }

        @Override
        protected List<VideoMsg> doInBackground(Void... params) {
            Log.i(TAG, "User get video list.");
            return UserServerHelper.getVideos(mUserId);
        }

        @Override
        protected void onPostExecute(List<VideoMsg> tasks) {
            if(tasks.size() == 0) {
                return;
            }
            mVideos = tasks;
            setupAdapter();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
