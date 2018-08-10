package com.shouyubang.android.sybang;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.shouyubang.android.sybang.account.LoginActivity;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.record.BranchActivity;
import com.shouyubang.android.sybang.record.PostVideoActivity;
import com.shouyubang.android.sybang.record.RecordActivity;
import com.shouyubang.android.sybang.call.StaffListActivity;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RollPagerView mRollViewPager;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRollViewPager = (RollPagerView) view.findViewById(R.id.ads_banner);
        mRollViewPager.setAdapter(new ImageNormalAdapter());
        mRollViewPager.setHintView(new ColorPointHintView(getActivity(),
                ContextCompat.getColor(getActivity(), R.color.colorAccent), Color.WHITE));
        CardView cardView = (CardView) view.findViewById(R.id.card_view);
        LinearLayout onlineButton = (LinearLayout) view.findViewById(R.id.btn_home_online);
        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!needLogin()) {
                    Intent i = StaffListActivity.newIntent(getActivity());
                    startActivity(i);
                } else {
                    Intent i = LoginActivity.newIntent(getActivity());
                    startActivity(i);
                }
            }
        });
        LinearLayout offlineButton = (LinearLayout) view.findViewById(R.id.btn_home_offline);
        offlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!needLogin()) {
                    Intent i = PostVideoActivity.newIntent(getActivity());
                    startActivity(i);
                } else {
                    Intent i = LoginActivity.newIntent(getActivity());
                    startActivity(i);
                }
            }
        });
        return view;
    }

    private class ImageNormalAdapter extends StaticPagerAdapter {
        int[] imgs = new int[]{
                R.mipmap.img1,
                R.mipmap.img2,
                R.mipmap.img3,
                R.mipmap.img4,
                R.mipmap.img5,
        };

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setImageResource(imgs[position]);
            return view;
        }


        @Override
        public int getCount() {
            return imgs.length;
        }
    }

    /**
     * 判断是否需要登录
     *
     * @return true 代表需要重新登录
     */
    public boolean needLogin() {
        if (MySelfInfo.getInstance().getId() != null) {
            return false;//有账号不需要登录
        } else {
            return true;//需要登录
        }

    }


}
