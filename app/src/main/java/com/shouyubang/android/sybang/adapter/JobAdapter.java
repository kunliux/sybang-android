package com.shouyubang.android.sybang.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.career.JobDetailActivity;
import com.shouyubang.android.sybang.model.Job;
import com.shouyubang.android.sybang.utils.Constants;

import java.util.List;

import butterknife.BindView;

/**
 * Created by KunLiu on 2016/5/27.
 */
public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobHolder> {

    private static final String TAG = "JobAdapter";

    private Context mContext;

    private List<Job> mJobs;

    public JobAdapter(Context packageContext, List<Job> accounts) {
        mContext = packageContext;
        mJobs = accounts;
    }


    @Override
    public JobHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_job_item, parent, false);
        return new JobHolder(view);
    }

    @Override
    public void onBindViewHolder(JobHolder holder, int position) {
        Job job = mJobs.get(position);
        holder.bindJob(job);
    }

    @Override
    public int getItemCount() {
        return mJobs.size();
    }

    public void setJobs(List<Job> accounts) {
        mJobs = accounts;
    }

    public class JobHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Job mJob;

        TextView mJobTitle;
        TextView mJobSalaryRange;
        TextView mJobCity;
        TextView mJobDistrict;
        TextView mJobGender;
        TextView mJobAgeRange;
        ImageView mCompanyLogo;
        TextView mCompanyName;

        JobHolder(View itemView) {
            super(itemView);
            mJobTitle = (TextView) itemView.findViewById(R.id.job_title_text);
            mJobSalaryRange =  (TextView) itemView.findViewById(R.id.job_salary_range_text);
            mJobCity =  (TextView) itemView.findViewById(R.id.job_city_text);
            mJobDistrict =  (TextView) itemView.findViewById(R.id.job_district_text);
            mJobGender =  (TextView) itemView.findViewById(R.id.job_gender_text);
            mJobAgeRange =  (TextView) itemView.findViewById(R.id.job_age_range_text);
            mCompanyLogo =   (ImageView) itemView.findViewById(R.id.company_logo);
            mCompanyName = (TextView) itemView.findViewById(R.id.company_name);
            itemView.setOnClickListener(this);
        }

        void bindJob(Job job) {
            mJob = job;
            mJobTitle.setText(mJob.getJobTitle());
            String salaryRange;
            if(mJob.getMaxSalary() != -1) {
                salaryRange = mJob.getMinSalary()+"-"+mJob.getMaxSalary()+"/月";
            } else {
                salaryRange = mJob.getMinSalary()+"+/月";
            }
            mJobSalaryRange.setText(salaryRange);
            mJobCity.setText(mJob.getCity());
            String district = mJob.getDistrict();
            if (null != district && !district.isEmpty()) {
                mJobDistrict.setText(district);
            } else {
                mJobDistrict.setText("不详");
            }
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
            String ageRange = mJob.getMinAge()+"-"+mJob.getMaxAge()+"岁";
            mJobAgeRange.setText(ageRange);
            mCompanyName.setText(mJob.getCompany());
        }

        @Override
        public void onClick(View v) {
            Intent i = JobDetailActivity.newIntent(mContext, mJob.getId());
            mContext.startActivity(i);
        }


    }
}
