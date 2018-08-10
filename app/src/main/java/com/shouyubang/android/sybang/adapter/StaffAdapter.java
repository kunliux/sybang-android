package com.shouyubang.android.sybang.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.call.StartCallTask;
import com.shouyubang.android.sybang.model.Staff;
import com.shouyubang.android.sybang.utils.CallUtil;
import com.shouyubang.android.sybang.utils.Constants;
import com.shouyubang.android.sybang.utils.DialogUtil;
import com.tencent.callsdk.ILVCallConstants;

import java.util.List;

/**
 * Created by KunLiu on 2016/5/27.
 */
public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffHolder> {

    private static final String TAG = "StaffAdapter";

    private Context mContext;

    private List<Staff> mStaffs;

    public StaffAdapter(Context packageContext, List<Staff> accounts) {
        mContext = packageContext;
        mStaffs = accounts;
    }


    @Override
    public StaffHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_staff_item, parent, false);
        return new StaffHolder(view);
    }

    @Override
    public void onBindViewHolder(StaffHolder holder, int position) {
        Staff staff = mStaffs.get(position);
        holder.bindStaff(staff);
    }

    @Override
    public int getItemCount() {
        return mStaffs.size();
    }

    public void setStaffs(List<Staff> accounts) {
        mStaffs = accounts;
    }

    class StaffHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private Staff mStaff;

        private TextView mNickName;
        private ImageView mAvatar;
        private TextView mStatus;

        StaffHolder(View itemView) {
            super(itemView);
            mNickName = (TextView) itemView.findViewById(R.id.item_nickname);
            mAvatar = (ImageView) itemView.findViewById(R.id.item_avatar);
            mStatus = (TextView) itemView.findViewById(R.id.item_status);
            itemView.setOnClickListener(this);
        }

        void bindStaff(Staff staff) {
            mStaff = staff;
            mNickName.setText(mStaff.getNickname());
            switch(mStaff.getOnline()) {
                case Constants.CALL_ONLINE:
                    mStatus.setText("在线");
                    break;
                case Constants.CALL_OFFLINE:
                    mStatus.setText("离线");
                    break;
                case Constants.CALL_AWAY:
                    mStatus.setText("离开");
                    break;
                case Constants.CALL_BUSY:
                    mStatus.setText("忙碌");
                    break;
                default:
                    mStatus.setText("离线");
                    break;
            }
//            mAvatar.setImageResource(R.drawable.ic_default_user);
//            Log.i(TAG, mStaff.toString());
        }

        @Override
        public void onClick(View v) {
            ProgressDialog progressDialog = DialogUtil.getProgressDialog(mContext, "等待服务中", true);
            progressDialog.show();
            new StartCallTask(mContext, mStaff.getPhone()).execute();
        }
    }
}
