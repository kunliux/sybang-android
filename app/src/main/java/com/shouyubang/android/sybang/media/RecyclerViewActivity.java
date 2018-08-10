package com.shouyubang.android.sybang.media;


import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.Window;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.adapter.VideoNormalAdapter;
import com.shouyubang.android.sybang.model.Video;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewActivity extends AppCompatActivity {


    @BindView(R.id.list_item_recycler)
    RecyclerView videoList;

    LinearLayoutManager linearLayoutManager;

    RecyclerBaseAdapter recyclerBaseAdapter;

    List<Video> dataList = new ArrayList<>();

    boolean mFull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置一个exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);

        resolveData();

        final VideoNormalAdapter recyclerNormalAdapter = new VideoNormalAdapter(this, dataList);
        linearLayoutManager = new LinearLayoutManager(this);
        videoList.setLayoutManager(linearLayoutManager);
        videoList.setAdapter(recyclerNormalAdapter);

        videoList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem   = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
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
                            recyclerNormalAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

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
    public void onBackPressed() {
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
    }


    private void resolveData() {
        for (int i = 0; i < 10; i++) {
            //dataList.add(videoModel);
            //for (int i = 0; i < 10; i++) {
                switch (i){
                    case 1:dataList.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160121_%E9%AB%98%E6%B8%85_1.mp4"));break;
                    case 2:dataList.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160122_%E9%AB%98%E6%B8%85_1.mp4"));break;
                    case 3:dataList.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160123_%E9%AB%98%E6%B8%85_1.mp4"));break;
                    case 4:dataList.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160124_%E9%AB%98%E6%B8%85_1.mp4"));break;
                    case 5:dataList.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160125_%E9%AB%98%E6%B8%85_1.mp4"));break;
                    case 6:dataList.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160126_%E9%AB%98%E6%B8%85_1.mp4"));break;
                    case 7:dataList.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160127_%E9%AB%98%E6%B8%85_1.mp4"));break;
                    case 8:dataList.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160128_%E9%AB%98%E6%B8%85_1.mp4"));break;
                    case 9:dataList.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%20160129_%E9%AB%98%E6%B8%85_1.mp4"));break;
                    case 10:dataList.add(new Video("http://sybangmp4-1254226083.cosbj.myqcloud.com/%E6%96%B0%E9%97%BB%E6%89%8B%E8%AF%AD%201601210_%E9%AB%98%E6%B8%85_1.mp4"));break;
               //}
            }
        }
        if (recyclerBaseAdapter != null)
            recyclerBaseAdapter.notifyDataSetChanged();
    }

}
