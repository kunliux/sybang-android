package com.shouyubang.android.sybang;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shouyubang.android.sybang.news.NewsTabFragment;
import com.shouyubang.android.sybang.news.ShowTabFragment;
import com.shouyubang.android.sybang.news.VideoTabFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TabLayout mNewsTab;
    private ViewPager mContentVp;//ViewPager

    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    private OnFragmentInteractionListener mListener;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        mNewsTab = (TabLayout) v.findViewById(R.id.news_tab);//实例化tab
        mContentVp = (ViewPager) v.findViewById(R.id.vp_content);//实例化viewppager

        initContent();//c初始化tab中的内容
        initTab();

        return v;
    }

    private void initTab(){
        mNewsTab.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.material_textBlack_disable),
                ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        mNewsTab.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        mNewsTab.setupWithViewPager(mContentVp);//Tab设置ViewPager
    }

    private void initContent(){
        tabIndicators = new ArrayList<>();
        tabIndicators.add("付费精品");
        tabIndicators.add("天天阅读");
        tabIndicators.add("手语视频");

        tabFragments = new ArrayList<>();
        tabFragments.add(ShowTabFragment.newInstance());
        tabFragments.add(NewsTabFragment.newInstance());
        tabFragments.add(VideoTabFragment.newInstance());
        contentAdapter = new ContentPagerAdapter(getChildFragmentManager());
        mContentVp.setAdapter(contentAdapter);//关联tab与tabFragment
    }

    private class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
