package com.shouyubang.android.sybang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.model.Show;

import java.util.List;

/**
 * Created by Lenovo on 2017/8/30.
 */

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {
    private List<Show> mShows;
    private Context mContext;

    public ShowAdapter(Context context, List<Show> shows){
        mContext = context;
        mShows = shows;
    }

    public void setShows(List<Show> shows) {
        mShows = shows;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_show, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Show show = mShows.get(position);
    }

    @Override
    public int getItemCount() {
        return mShows.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView showImage;
        TextView showTitle;
        TextView showDetail;
        TextView showPrice;

        public ViewHolder(View v){
            super(v);
            showImage = (ImageView) v.findViewById(R.id.show_img);
            showTitle = (TextView) v.findViewById(R.id.show_title);
            showDetail = (TextView) v.findViewById(R.id.show_detail);
            showPrice = (TextView) v.findViewById(R.id.show_price);
        }
    }
}
