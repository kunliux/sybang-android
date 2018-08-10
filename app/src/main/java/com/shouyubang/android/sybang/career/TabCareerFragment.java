package com.shouyubang.android.sybang.career;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.adapter.JobAdapter;
import com.shouyubang.android.sybang.model.Job;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.presenters.UserServerHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KunLiu on 17/8/3.
 *
 */
public class TabCareerFragment extends Fragment {

    private static final String TAG = "TabCareerFragment";

    private static final String EXTRA_TYPE = "type";

    private int type;

    private RecyclerView mJobRecyclerView;
    private List<Job> mJobs = new ArrayList<>();
    private JobAdapter mAdapter;
    private View v;

    public static TabCareerFragment newInstance(Integer type){
        TabCareerFragment tabCareerFragment = new TabCareerFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_TYPE, type);
        tabCareerFragment.setArguments(args);
        return tabCareerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(EXTRA_TYPE, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 为了防止第二次加载的时候重复调用这个方法onCreateView()，重新new了一个pageadapter导致fragment不显示，显示空白
        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null) {
                parent.removeView(v);
            }
            return v;
        }
        v = inflater.inflate(R.layout.fragment_tab_career, container, false);
        mJobRecyclerView = (RecyclerView) v.findViewById(R.id.job_recycler_view);
        mJobRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mJobRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        new JobListTask(MySelfInfo.getInstance().getId(), type).execute();
        return v;
    }

    private void setupAdapter() {

        if (mAdapter == null) {
            mAdapter = new JobAdapter(getActivity(), mJobs);
            mJobRecyclerView.setAdapter(mAdapter);
        } else {
            Log.d(TAG, "Size:" + mJobs.size());
            mAdapter.setJobs(mJobs);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class JobListTask extends AsyncTask<Void, Void, List<Job>> {

        private String mUserId;
        private Integer mType;

        JobListTask(String userId, Integer type) {
            mUserId = userId;
            mType = type;
        }

        @Override
        protected List<Job> doInBackground(Void... params) {
            return UserServerHelper.getJobs(mUserId, mType);
        }

        @Override
        protected void onPostExecute(List<Job> jobs) {
            if(jobs.size() == 0) {
                return;
            }
            mJobs = jobs;
            setupAdapter();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupAdapter();
    }
}
