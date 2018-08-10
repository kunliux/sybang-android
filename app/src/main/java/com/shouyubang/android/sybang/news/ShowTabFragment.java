package com.shouyubang.android.sybang.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.adapter.ShowAdapter;
import com.shouyubang.android.sybang.model.Show;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KunLiu on 17/8/3.
 *
 */

public class ShowTabFragment extends Fragment {

    private static final String TAG = "ShowFragment";
    private RecyclerView mShowRecyclerView;//
    private ShowAdapter mAdapter;
    private List<Show> mShowList = new ArrayList<>();

    private View view;

    public static ShowTabFragment newInstance(){
        return new ShowTabFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        //inflater是用来加载布局的,接受布局的ID,视图的父视图,是否加载布局布尔值
        //当布尔值为false时,第一个参数不会加载在第二个参数的容器里面,第二个参数是使Layout_length参数有效
        view = inflater.inflate(R.layout.fragment_tab_content, container,false);
        mShowRecyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        mShowRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mShowRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));//垂直方向添加
        mShowList =new ArrayList<>();
        //向列表中添加内容
        for(int i=0;i<4;i++){
            Show mShopping=new Show();
            mShowList.add(mShopping);
        }
        setupAdapter();//加载视图
        return view;
    }

    private void setupAdapter() {
        if (mAdapter == null) {
            mAdapter = new ShowAdapter(getActivity(), mShowList);
            mShowRecyclerView.setAdapter(mAdapter);
        } else {
            Log.d(TAG, "Size:" + mShowList.size());
            //mShowList.add(new Show());
            mAdapter.setShows(mShowList);
            mAdapter.notifyDataSetChanged();


        }
    }

    @Override
    //当从其它页面切回来的时候就会执行onResume()
    public void onResume() {
        super.onResume();
        setupAdapter();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();

    }
}