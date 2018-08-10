package com.shouyubang.android.sybang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.media.RecyclerItemNormalHolder;
import com.shouyubang.android.sybang.model.Video;

import java.util.List;

/**
 * Created by guoshuyu on 2017/1/9.
 */

public class VideoNormalAdapter extends RecyclerView.Adapter {
    private final static String TAG = "RecyclerBaseAdapter";

    private List<Video> itemDataList = null;
    private Context context = null;

    public VideoNormalAdapter(Context context, List<Video> itemDataList) {
        this.itemDataList = itemDataList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_video_item_normal, parent, false);
        final RecyclerView.ViewHolder holder = new RecyclerItemNormalHolder(context, v);
        return holder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        RecyclerItemNormalHolder recyclerItemViewHolder = (RecyclerItemNormalHolder) holder;
        recyclerItemViewHolder.setRecyclerBaseAdapter(this);
        recyclerItemViewHolder.onBind(position, itemDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    public void setListData(List<Video> data) {
        itemDataList = data;
        notifyDataSetChanged();
    }
}
