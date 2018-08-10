package com.shouyubang.android.sybang.news;


import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.adapter.VideoNormalAdapter;
import com.shouyubang.android.sybang.media.RecyclerItemNormalHolder;
import com.shouyubang.android.sybang.model.Video;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

public class VideoTabFragment extends Fragment {

    RecyclerView mVideoRecyclerView;

    VideoNormalAdapter mRecyclerNormalAdapter;
    LinearLayoutManager mLinearLayoutManager;
    List<Video> mVideos = new ArrayList<>();

    boolean mFull = false;

    public static VideoTabFragment newInstance(){
        //设置一个exit transition

        return new VideoTabFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //  getActivity().getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //getActivity().getWindow().setEnterTransition(new Explode());
        //getActivity().getWindow().setExitTransition(new Explode());
        //}

        View view=inflater.inflate(R.layout.fragment_tab_content,container,false);//关联Adapter与容器
        resolveData();//生成数据

        mVideoRecyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);//实例化Recycler并关联对应容器
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mVideoRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerNormalAdapter = new VideoNormalAdapter(getActivity(), mVideos);//生成Adapter
        mVideoRecyclerView.setAdapter(mRecyclerNormalAdapter);
        //监听器
        mVideoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem   = mLinearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(RecyclerItemNormalHolder.TAG)
                            && (position < firstVisibleItem || position > lastVisibleItem)) {

                        //如果滑出去了上面和下面就是否，和今日头条一样
                        //是否全屏
                        if(!mFull) {
                            GSYVideoPlayer.releaseAllVideos();
                            mRecyclerNormalAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        return view;
    }

    private void resolveData() {
        for (int i = 1; i <= 10; i++) {
            switch (i){
                case 1:mVideos.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160121_%E9%AB%98%E6%B8%85_1.mp4"));break;
                case 2:mVideos.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160122_%E9%AB%98%E6%B8%85_1.mp4"));break;
                case 3:mVideos.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160123_%E9%AB%98%E6%B8%85_1.mp4"));break;
                case 4:mVideos.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160124_%E9%AB%98%E6%B8%85_1.mp4"));break;
                case 5:mVideos.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160125_%E9%AB%98%E6%B8%85_1.mp4"));break;
                case 6:mVideos.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160126_%E9%AB%98%E6%B8%85_1.mp4"));break;
                case 7:mVideos.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160127_%E9%AB%98%E6%B8%85_1.mp4"));break;
                case 8:mVideos.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160128_%E9%AB%98%E6%B8%85_1.mp4"));break;
                case 9:mVideos.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160129_%E9%AB%98%E6%B8%85_1.mp4"));break;
                case 10:mVideos.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%201601210_%E9%AB%98%E6%B8%85_1.mp4"));break;
            }
        }
        if (mRecyclerNormalAdapter != null)
            mRecyclerNormalAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (newConfig.orientation != ActivityInfo.SCREEN_ORIENTATION_USER) {
            mFull = false;
        } else {
            mFull = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
    }

}
