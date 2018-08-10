package com.shouyubang.android.sybang.news;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.adapter.NewsAdapter;
import com.shouyubang.android.sybang.model.News;
import com.shouyubang.android.sybang.model.Show;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KunLiu on 2017/8/31.
 */

public class NewsTabFragment extends Fragment {
    private RecyclerView mNewsRecyclerView;
    private List<News> mNewsList = new ArrayList<>();
    private NewsAdapter mAdapter;

    private View view;

    public static NewsTabFragment newInstance(){
        return new NewsTabFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }

        view=inflater.inflate(R.layout.fragment_tab_content,container,false);//关联Adapter与容器
        mNewsRecyclerView=(RecyclerView) view.findViewById(R.id.news_recycler_view);//实例化Recycler并关联对应容器
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNewsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mNewsList = new ArrayList<>();
        //向列表中添加内容
        for(int i=1;i<11;i++){
            switch (i){
                case 1: mNewsList.add(new News("二十位聋人学子出国深造,为\"无声世界\"添加亮色","实时新闻",i,"http://www.shouyubang.com:8080/syb/sy/image/20171009/1507523862636004771.jpg"));break;
                case 2: mNewsList.add(new News("\"星之家烘培坊\"携爱来袭","爱心公益",i,"http://www.shouyubang.com:8080/syb/sy/image/20171009/1507527709543033615.jpg"));break;
                case 3: mNewsList.add(new News("一个被命运捉弄孩子的大学梦","实时新闻",i,"http://124.95.131.25/ecdomain/ecplatform/fileHandle.do?action=read&objectID=20170919101512498"));break;
                case 4: mNewsList.add(new News("残疾人民间足球争霸赛","实时新闻",i,"http://www.shouyubang.com:8080/syb/sy/image/20171009/1507528157774064648.jpg"));break;
                case 5: mNewsList.add(new News("全国残疾人岗位精英职业技能竞赛辽宁选拔赛拉下帷幕","实时新闻",i,"http://www.shouyubang.com:8080/syb/sy/image/20171009/1507528462562096817.jpg"));break;
                case 6: mNewsList.add(new News("闯入无声世界近5年,他教会40余名是从人开车","爱心公益",i,"http://www.shouyubang.com:8080/syb/sy/image/20171009/1507529051920074046.jpg"));break;
                case 7: mNewsList.add(new News("重庆多家医院联合为聋人开展健康普查","爱心公益",i,"http://www.shouyubang.com:8080/syb/sy/image/20171009/1507529319362032482.jpg"));break;
                case 8: mNewsList.add(new News("一双会说话的手套","实时新闻",i,"http://www.shouyubang.com:8080/syb/sy/image/20171009/1507529498877091914.jpg"));break;
                case 9: mNewsList.add(new News("是何等的巧手让废纸变成精美土陶罐?","传统文化",i,"http://www.shouyubang.com:8080/syb/sy/image/20171009/1507529594310020207.jpg"));break;
                case 10:mNewsList.add(new News("第60届国际聋人节 飞镖象棋友谊赛","实时新闻",i,"http://www.shouyubang.com:8080/syb/sy/image/20171009/1507529804550006279.jpg"));break;
            }
        }

        setupAdapter();
        return view;
    }
    private void setupAdapter(){
        mAdapter=new NewsAdapter(getActivity(),mNewsList);
        mNewsRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupAdapter();
    }
}
