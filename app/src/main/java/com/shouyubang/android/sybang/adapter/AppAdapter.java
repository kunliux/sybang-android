package com.shouyubang.android.sybang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by test on 2016/7/29.
 */

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppHolder> {

    private static final String TAG = "AppAdapter";

    private Context mContext;
    private List<Integer> mHeight;
    private ArrayList<AppInfo> appList;

    public interface OnItemClickListener {
        void onItemClick(AppInfo parent, View view, int position);
        void onItemLongClick(AppInfo parent, View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public AppAdapter(Context mContext, ArrayList<AppInfo> date) {
        this.appList = date;
        this.mContext = mContext;
    }


    @Override
    public AppHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_app_item, parent, false);
        return new AppHolder(view);
    }

    @Override
    public void onBindViewHolder(final AppHolder holder, int position) {
        holder.tv.setText(appList.get(position).getAppName());
        holder.img.setBackground(appList.get(position).getAppIcon());
        if (mOnItemClickListener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(appList.get(pos), holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(appList.get(pos),holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class AppHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView img;

        public AppHolder(View view) {
            super(view);
             tv = (TextView) view.findViewById(R.id.text_list_item);
            img = (ImageView) view.findViewById(R.id.img_list_item);
        }
    }


}
