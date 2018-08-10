package com.shouyubang.android.sybang.career;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.account.ProfileEditActivity;
import com.shouyubang.android.sybang.adapter.AppAdapter;
import com.shouyubang.android.sybang.model.AppInfo;
import com.shouyubang.android.sybang.model.Job;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.presenters.UserServerHelper;
import com.shouyubang.android.sybang.utils.Constants;
import com.shouyubang.android.sybang.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JobDetailActivity extends AppCompatActivity {

    private static final String TAG = "JobDetailActivity";

    private static final String EXTRA_JOB_ID = "job_id";

    private static final int REQUEST_APPLY_JOB = 1;
    private static final int REQUEST_MARK_JOB = 2;
    private static final int REQUEST_UNMARK_JOB = 3;

    @BindView(R.id.job_detail_title_text)
    TextView mJobTitle;
    @BindView(R.id.job_detail_salary_range_text)
    TextView mJobSalaryRange;
    @BindView(R.id.job_detail_city_text)
    TextView mJobCity;
    @BindView(R.id.job_detail_gender_text)
    TextView mJobGender;
    @BindView(R.id.job_detail_age_range_text)
    TextView mJobAgeRange;
    @BindView(R.id.job_detail_company_logo)
    ImageView mCompanyLogo;
    @BindView(R.id.job_detail_company_name)
    TextView mCompanyName;
    @BindView(R.id.job_content_text)
    TextView mJobContent;
    @BindView(R.id.job_requirement_text)
    TextView mJobRequirement;
    @BindView(R.id.work_time_text)
    TextView mWorkTime;
    @BindView(R.id.job_salary_text)
    TextView mJobSalary;
    @BindView(R.id.job_insurance_text)
    TextView mJobInsurance;
    @BindView(R.id.job_insurance_layout)
    LinearLayout mJobInsuranceLayout;
    @BindView(R.id.room_board_text)
    TextView mRoomBoard;
    @BindView(R.id.room_board_layout)
    LinearLayout mRoomBoardLayout;
    @BindView(R.id.labour_contract_text)
    TextView mLabourContract;
    @BindView(R.id.labour_contract_layout)
    LinearLayout mLabourContractLayout;
    @BindView(R.id.job_apply_btn)
    Button mJobApplyButton;
    @BindView(R.id.other_benefits_text)
    TextView mOtherBenefits;
    @BindView(R.id.other_benefits_layout)
    LinearLayout mOtherBenefitsLayout;
    @BindView(R.id.job_workplace_text)
    TextView mJobWorkplace;
    @BindView(R.id.job_content_layout)
    LinearLayout mJobContentLayout;
    @BindView(R.id.job_requirement_layout)
    LinearLayout mJobRequirementLayout;
    @BindView(R.id.work_time_layout)
    LinearLayout mWorkTimeLayout;
    @BindView(R.id.job_salary_layout)
    LinearLayout mJobSalaryLayout;

    Job mJob;
    Integer mJobId;
    boolean collected = false;
    MenuItem menuItem;

    BottomSheetDialog d;

    ArrayList<AppInfo> list;
    AppAdapter recyclerViewAdapter;
    RecyclerView mRecyclerView;

    public static Intent newIntent(Context packageContext, Integer jobId) {
        Intent intent = new Intent(packageContext, JobDetailActivity.class);
        intent.putExtra(EXTRA_JOB_ID, jobId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJobId = getIntent().getIntExtra(EXTRA_JOB_ID, 0);
        setContentView(R.layout.activity_job_detail);
        ButterKnife.bind(this);
        new JobDetailTask(mJobId).execute();

        mJobApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showApplyDialog();
            }
        });

    }

    private void showApplyDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(JobDetailActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("我已经仔细阅读了职位详情~");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new JobRequestTask(REQUEST_APPLY_JOB, mJobId).execute();
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }

    private void showProfileDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(JobDetailActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("申请职位前需完善个人资料。");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = ProfileEditActivity.newIntent(JobDetailActivity.this);
                startActivity(i);
                finish();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();
    }

    public void initView() {
        if(isCollected()) {
            menuItem.setIcon(R.drawable.ic_star_white_24dp);
        } else {
            menuItem.setIcon(R.drawable.ic_star_border_white_24dp);
        }
        mJobTitle.setText(mJob.getJobTitle());
        String salaryRange;
        if (mJob.getMaxSalary() != -1) {
            salaryRange = mJob.getMinSalary() + "-" + mJob.getMaxSalary() + "/月";
        } else {
            salaryRange = mJob.getMinSalary() + "+/月";
        }
        mJobSalaryRange.setText(salaryRange);
        mJobCity.setText(mJob.getCity());
        switch (mJob.getGender()) {
            case Constants.GENDER_MALE:
                mJobGender.setText("男");
                break;
            case Constants.GENDER_FEMALE:
                mJobGender.setText("女");
                break;
            case Constants.GENDER_NO_LIMIT:
                mJobGender.setText("不限");
        }
        String ageRange = mJob.getMinAge() + "-" + mJob.getMaxAge() + "岁";
        mJobAgeRange.setText(ageRange);
        mCompanyName.setText(mJob.getCompany());
        String content = mJob.getJobContent();
        String requirement = mJob.getRequirement();
        String workTime = mJob.getWorkTime();
        String salary = mJob.getSalary();
        String insurance = mJob.getInsurance();
        String roomBoard = mJob.getRoomBoard();
        String labourContract = mJob.getContract();
        String otherBenefits = mJob.getBenefits();
        if (content != null && !content.isEmpty()) {
            mJobContent.setText(content);
        } else {
            mJobContentLayout.setVisibility(View.GONE);
        }
        if (requirement != null && !requirement.isEmpty()) {
            mJobRequirement.setText(requirement);
        } else {
            mJobRequirementLayout.setVisibility(View.GONE);
        }
        if (workTime != null && !workTime.isEmpty()) {
            mWorkTime.setText(workTime);
        } else {
            mWorkTimeLayout.setVisibility(View.GONE);
        }
        if (salary != null && !salary.isEmpty()) {
            mJobSalary.setText(salary);
        } else {
            mJobSalaryLayout.setVisibility(View.GONE);
        }
        if (insurance != null && !insurance.isEmpty()) {
            mJobInsurance.setText(insurance);
        } else {
            mJobInsuranceLayout.setVisibility(View.GONE);
        }
        if (roomBoard != null && !roomBoard.isEmpty()) {
            mRoomBoard.setText(roomBoard);
        } else {
            mRoomBoardLayout.setVisibility(View.GONE);
        }
        if (labourContract != null && !labourContract.isEmpty()) {
            mLabourContract.setText(labourContract);
        } else {
            mLabourContractLayout.setVisibility(View.GONE);
        }
        if (otherBenefits != null && !otherBenefits.isEmpty()) {
            mOtherBenefits.setText(otherBenefits);
        } else {
            mOtherBenefitsLayout.setVisibility(View.GONE);
        }
        String workPlace = mJob.getWorkplace();
        if (workPlace != null && !workPlace.isEmpty()) {
            mJobWorkplace.setText(workPlace);
        } else {
            mJobWorkplace.setText("地址不详");
        }
    }

    private boolean isCollected() {
        return collected;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_job_detail, menu);
        menuItem = menu.findItem(R.id.action_mark_job);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_mark_job:
                if(!isCollected()) {
                    collected = true;
                    item.setIcon(R.drawable.ic_star_white_24dp);
                    new JobRequestTask(REQUEST_MARK_JOB, mJobId).execute();
                } else {
                    collected = false;
                    item.setIcon(R.drawable.ic_star_border_white_24dp);
                    new JobRequestTask(REQUEST_UNMARK_JOB, mJobId).execute();
                }
                return true;
            case R.id.action_share_job:
                showBottomDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showBottomDialog() {
        list = getShareAppList();
        d = DialogUtil.getBottomDialog(JobDetailActivity.this, R.layout.dialog_bottom_sheet);
        initBottomDialog(d);
        setBehaviorCallback();
        d.show();

    }

    private void initBottomDialog(final Dialog d) {
        recyclerViewAdapter = new AppAdapter(this, list);

        mRecyclerView = (RecyclerView) d.findViewById(R.id.app_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(recyclerViewAdapter);


        recyclerViewAdapter.setOnItemClickListener(new AppAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AppInfo parent, View view, int position) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setComponent(new ComponentName(parent.getPkgName(), parent.getLaunchClassName()));
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "www.shouyubang.com");
                startActivity(shareIntent);
            }

            @Override
            public void onItemLongClick(AppInfo parent, View view, int position) {

            }
        });
    }

    private void setBehaviorCallback() {
        View view = d.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    d.dismiss();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private ArrayList<AppInfo> getShareAppList() {
        ArrayList<AppInfo> shareAppInfos = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> resolveInfos = getShareApps(JobDetailActivity.this);
        if (null == resolveInfos) {
            return null;
        } else {
            for (ResolveInfo resolveInfo : resolveInfos) {
                AppInfo appInfo = new AppInfo();
                appInfo.setPkgName(resolveInfo.activityInfo.packageName);
                appInfo.setLaunchClassName(resolveInfo.activityInfo.name);
                appInfo.setAppName(resolveInfo.loadLabel(packageManager).toString());
                appInfo.setAppIcon(resolveInfo.loadIcon(packageManager));
                shareAppInfos.add(appInfo);
            }
        }

        return shareAppInfos;
    }

    public List<ResolveInfo> getShareApps(Context context) {
        List<ResolveInfo> mApps;
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        PackageManager pManager = context.getPackageManager();
        mApps = pManager.queryIntentActivities(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;
    }

    private class JobDetailTask extends AsyncTask<Void, Void, Job> {

        private int mJobId;

        JobDetailTask(Integer jobId) {
            mJobId = jobId;
        }

        @Override
        protected Job doInBackground(Void... params) {
            return UserServerHelper.getJob(MySelfInfo.getInstance().getId(), mJobId);
        }

        @Override
        protected void onPostExecute(Job job) {
            mJob = job;
            if(!TextUtils.isEmpty(mJob.getJobTitle())) {
                collected = (mJob.getCollected() == Constants.JOB_MARK);
                initView();
            } else {
                showProfileDialog();
            }
        }
    }

    private class JobRequestTask extends AsyncTask<Void, Void, Boolean> {

        private int mJobId;
        private int mRequestId;

        JobRequestTask(Integer requestId, Integer jobId) {
            mJobId = jobId;
            mRequestId = requestId;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            switch (mRequestId) {
                case REQUEST_APPLY_JOB:
                    return UserServerHelper.applyJob(MySelfInfo.getInstance().getId(), mJobId);
                case REQUEST_MARK_JOB:
                    return UserServerHelper.markJob(MySelfInfo.getInstance().getId(), mJobId);
                case REQUEST_UNMARK_JOB:
                    return UserServerHelper.unmarkJob(MySelfInfo.getInstance().getId(), mJobId);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            switch (mRequestId) {
                case REQUEST_APPLY_JOB:
                    if(success) {
                        DialogUtil.showToast(JobDetailActivity.this, "申请成功");
                    } else {
                        DialogUtil.showToast(JobDetailActivity.this, "申请失败");
                    }
                    break;
                case REQUEST_MARK_JOB:
                    if (success) {
                        DialogUtil.showToast(JobDetailActivity.this, "收藏成功");
                    } else {
                        DialogUtil.showToast(JobDetailActivity.this, "收藏失败");
                    }
                    break;
                case REQUEST_UNMARK_JOB:
                    if (success) {
                        DialogUtil.showToast(JobDetailActivity.this, "取消收藏成功");
                    } else {
                        DialogUtil.showToast(JobDetailActivity.this, "取消收藏失败");
                    }
                    break;
            }
        }
    }
}
