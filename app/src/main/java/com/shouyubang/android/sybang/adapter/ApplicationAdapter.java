package com.shouyubang.android.sybang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.model.ApplicationInfo;

import java.util.List;

/**
 * Created by KunLiu on 2016/5/27.
 */
public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationInfoHolder> {

    private static final String TAG = "ApplicationAdapter";

    private Context mContext;

    private List<ApplicationInfo> mApplications;

    public ApplicationAdapter(Context packageContext, List<ApplicationInfo> accounts) {
        mContext = packageContext;
        mApplications = accounts;
    }


    @Override
    public ApplicationInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_application_item, parent, false);
        return new ApplicationInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(ApplicationInfoHolder holder, int position) {
        ApplicationInfo job = mApplications.get(position);
        holder.bindApplicationInfo(job);
    }

    @Override
    public int getItemCount() {
        return mApplications.size();
    }

    public void setApplications(List<ApplicationInfo> accounts) {
        mApplications = accounts;
    }

    public class ApplicationInfoHolder extends RecyclerView.ViewHolder {

        private ApplicationInfo mApplicationInfo;

        TextView mJobTitle;
        TextView mApplicationStatus;
        ImageView mCompanyLogo;
        TextView mCompanyName;

        ApplicationInfoHolder(View itemView) {
            super(itemView);
            mJobTitle = (TextView) itemView.findViewById(R.id.application_job_title);
            mApplicationStatus = (TextView) itemView.findViewById(R.id.application_status);
            mCompanyLogo = (ImageView) itemView.findViewById(R.id.application_company_logo);
            mCompanyName = (TextView) itemView.findViewById(R.id.application_company_name);
        }

        void bindApplicationInfo(ApplicationInfo job) {
            mApplicationInfo = job;
            mJobTitle.setText(mApplicationInfo.getJobTitle());
            mCompanyName.setText(mApplicationInfo.getCompanyName());
        }


    }
}
