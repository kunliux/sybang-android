package com.shouyubang.android.sybang.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shouyubang.android.sybang.MainActivity;
import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.model.News;
import com.shouyubang.android.sybang.news.NewsDetailActivity;
import com.shouyubang.android.sybang.news.NewsTabFragment;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Lenovo on 2017/8/31.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> mNewsList;
    private Context mContext;
    //static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        News mNews;
        TextView NewsTitle;
        TextView NewsType;
        TextView NewsTime;
        ImageView NewsImg;

        public ViewHolder(View v){
            super(v);
            NewsTitle= (TextView) v.findViewById(R.id.news_title);
            NewsType = (TextView) v.findViewById(R.id.news_classify);
            NewsTime= (TextView) v.findViewById(R.id.news_time);
            NewsImg= (ImageView) v.findViewById(R.id.news_img);
        }
        void bindItem(News Item){
            mNews=Item;
            itemView.setOnClickListener(this);
            NewsTitle.setText(Item.getTitle());
            NewsType.setText(Item.getClassify());
            NewsTime.setText(Item.getTime());
            Glide.with(mContext)
                    .load(Item.getImageId())
                    //.placeholder(R.drawable.news_img)//设置占位图
                    // .fitCenter()//缩小使满足宽或者高 ,centerCrop()放大使满足宽或者高
                    .into(NewsImg);//加载照片
        }

        @Override
        public void onClick(View v){
            Intent intent=new Intent(mContext,NewsDetailActivity.class);
            intent.putExtra("extra_id",mNews.getId());//传入选中新闻的ID
            mContext.startActivity(intent);
        }
    }

    public NewsAdapter(Context context, List<News> readingList){
        mContext=context;
        mNewsList=readingList;
    }
    public void setNewsList(List<News> readingList){
        mNewsList=readingList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        News reading=mNewsList.get(position);
            holder.bindItem(reading);
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}
