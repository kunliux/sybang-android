package com.shouyubang.android.sybang;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shouyubang.android.sybang.career.QueryPreferences;
import com.shouyubang.android.sybang.career.TabCareerFragment;

import java.util.ArrayList;
import java.util.List;

public class CareerFragment extends Fragment {

    private static final String TAG = "CareerFragment";

    private TabLayout mTabTl;
    private ViewPager mContentVp;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;


    private OnFragmentInteractionListener mListener;

    public CareerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CareerFragment newInstance() {
        CareerFragment fragment = new CareerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
       // setHasOptionsMenu(true);//使得的fragment可以接受菜单调用
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_career, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        mToolbar = (Toolbar) v.findViewById(R.id.career_toolbar);//标题栏
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setTitle("");
        mToolbarTitle = (TextView) v.findViewById(R.id.toolbar_title);
        mTabTl = (TabLayout) v.findViewById(R.id.tl_tab);
        mContentVp = (ViewPager) v.findViewById(R.id.vp_content);

        initContent();
        initTab();

        return v;
    }

    private void initTab(){
        mTabTl.setTabMode(TabLayout.MODE_FIXED);
        mTabTl.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.material_textBlack_disable),
                ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        mTabTl.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        mTabTl.setupWithViewPager(mContentVp);
    }

    private void initContent(){
        tabIndicators = new ArrayList<>();
        tabIndicators.add("推荐");
        tabIndicators.add("服务");
        tabIndicators.add("生产");
        tabIndicators.add("技术");
        tabIndicators.add("教育");
        tabFragments = new ArrayList<>();
        for (int i =0; i<tabIndicators.size(); i++) {
            tabFragments.add(TabCareerFragment.newInstance(i));
        }
        contentAdapter = new ContentPagerAdapter(getChildFragmentManager());
        mContentVp.setAdapter(contentAdapter);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
       // menuInflater.inflate(R.menu.fragment_career, menu);//搜索

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "QueryTextSubmit:" + s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "QueryTextChange:" + s);
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = QueryPreferences.getStoredQuery(getActivity());
                searchView.setQuery(query, false);//query 质疑
            }
        });

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