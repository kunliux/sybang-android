package com.shouyubang.android.sybang.call;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.adapter.StaffAdapter;
import com.shouyubang.android.sybang.model.Job;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.model.Staff;
import com.shouyubang.android.sybang.presenters.UserServerHelper;
import com.shouyubang.android.sybang.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

public class StaffListActivity extends AppCompatActivity {

    private static final String TAG = "StaffListActivity";
    private static final int REQUEST_PHONE_PERMISSIONS = 21;

    private RecyclerView mStaffRecyclerView;
    private List<Staff> mStaffs = new ArrayList<>();
    private StaffAdapter mAdapter;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, StaffListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);
        mStaffRecyclerView = (RecyclerView) findViewById(R.id.staff_recycler_view);
        mStaffRecyclerView.setLayoutManager(new LinearLayoutManager(StaffListActivity.this));
        askPermissions();
        //添加自定义分割线
        mStaffRecyclerView.addItemDecoration(new DividerItemDecoration(StaffListActivity.this, DividerItemDecoration.VERTICAL));
        new StaffListTask().execute();
    }

    private void setupAdapter() {

        if (mAdapter == null) {
            mAdapter = new StaffAdapter(StaffListActivity.this, mStaffs);
            mStaffRecyclerView.setAdapter(mAdapter);
        } else {
            Log.d(TAG, "Size:" + mStaffs.size());
            mAdapter.setStaffs(mStaffs);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class StaffListTask extends AsyncTask<Void, Void, List<Staff>> {
        private static final String TAG = "StaffListTask";

        @Override
        protected List<Staff> doInBackground(Void... params) {
            return UserServerHelper.getOnlineStaffs();
        }

        @Override
        protected void onPostExecute(List<Staff> staffs) {
            Log.d(TAG, "Staff List Size: "+staffs.size());
            if(staffs.size() == 0) {
                return;
            }
            mStaffs = staffs;
            setupAdapter();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupAdapter();
    }
    private void askPermissions() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ActivityCompat.checkSelfPermission(StaffListActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((ActivityCompat.checkSelfPermission(StaffListActivity.this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if ((ActivityCompat.checkSelfPermission(StaffListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionsList.size() != 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PHONE_PERMISSIONS);
            }
        } else {
            DialogUtil.showToast(StaffListActivity.this, getString(R.string.permissions_not_granted));
        }
    }
}
