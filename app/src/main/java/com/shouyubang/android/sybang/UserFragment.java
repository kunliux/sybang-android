package com.shouyubang.android.sybang;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.shouyubang.android.sybang.account.LoginActivity;
import com.shouyubang.android.sybang.account.ProfileEditActivity;
import com.shouyubang.android.sybang.account.ProfileInfoActivity;
import com.shouyubang.android.sybang.account.SettingsActivity;
import com.shouyubang.android.sybang.career.ApplicationActivity;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.model.UserProfile;
import com.shouyubang.android.sybang.presenters.ProfileInfoHelper;
import com.shouyubang.android.sybang.presenters.view.ProfileView;
import com.shouyubang.android.sybang.record.VideoListActivity;
import com.shouyubang.android.sybang.utils.UIUtils;
import com.shouyubang.android.sybang.wallet.WalletActivity;
import com.tencent.ilivesdk.core.ILiveLoginManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserFragment extends Fragment implements ProfileView {

    private static final String TAG = "UserFragment";
    private static  String mNickname;

    @BindView(R.id.user_avatar)
    ImageView mAvatar;
    @BindView(R.id.name)
    TextView mProfileName;
    @BindView(R.id.user_panel)
    LinearLayout mUserPanel;
    @BindView(R.id.re_notifications)
    RelativeLayout mNotificationsBtn;
    @BindView(R.id.career_btn)
    RelativeLayout mCareerBtn;
    @BindView(R.id.credits_btn)
    RelativeLayout mCreditsBtn;
    @BindView(R.id.setting_btn)
    RelativeLayout mSettingBtn;
    Unbinder unbinder;

    private ProfileInfoHelper mProfileHelper;

    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        unbinder = ButterKnife.bind(this, view);
        mNotificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!needLogin()) {
                    Intent i = VideoListActivity.newIntent(getActivity());
                    startActivity(i);
                } else {
                    Intent i = LoginActivity.newIntent(getActivity());
                    startActivity(i);
                }
            }
        });
        mCareerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!needLogin()) {
                    Intent i = ApplicationActivity.newIntent(getActivity());
                    startActivity(i);
                } else {
                    Intent i = LoginActivity.newIntent(getActivity());
                    startActivity(i);
                }
            }
        });
        mCreditsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = WalletActivity.newIntent(getActivity());
                startActivity(i);
            }
        });
        mUserPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!needLogin()) {
                    Intent i = ProfileInfoActivity.newIntent(getActivity());
                    startActivity(i);
                } else {
                    Intent i = LoginActivity.newIntent(getActivity());
                    startActivity(i);
                }
            }
        });
        mSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = SettingsActivity.newIntent(getActivity());
                startActivity(i);
            }
        });
        //设置初始化用户名,防止用户信息更新时候造成闪烁
        mProfileName.setText(MySelfInfo.getInstance().getNickName());
        //设置头像
        Glide.with(getActivity()).load(MySelfInfo.getInstance().getAvatar())
                .apply(RequestOptions.circleCropTransform()).into(mAvatar);

        mProfileHelper = new ProfileInfoHelper(this);

        return view;
    }


    @Override
    public void updateUserProfile(UserProfile profile) {
        //获取信息
        if (null == getContext()){
            return;
        }
        if (TextUtils.isEmpty(profile.getNickname())) {
            MySelfInfo.getInstance().setNickName(profile.getPhone());//如果用户名为空,则设置NIckname为手机号
        } else {
            MySelfInfo.getInstance().setNickName(profile.getNickname());
        }
        if (!TextUtils.isEmpty(profile.getAvatarUrl())) {
            MySelfInfo.getInstance().setAvatar(profile.getAvatarUrl());
        }
        MySelfInfo.getInstance().writeToCache(getContext());//写道缓存
        //更新信息,此时的Nickname为手机号
        updateUserInfo(ILiveLoginManager.getInstance().getMyUserId(), MySelfInfo.getInstance().getNickName(),
                MySelfInfo.getInstance().getAvatar());
    }

    //更新用户名,账号,头像
    public void updateUserInfo(String id, String name, String url) {
        if(null != name && !TextUtils.isEmpty(name)){
            mProfileName.setText(name);//设置用户名
        }
        if (!TextUtils.isEmpty(url)) {
            Log.d(TAG, "profile avatar: " + url);
            Glide.with(getActivity()).load(url)//设置头像
                    .apply(RequestOptions.circleCropTransform()).into(mAvatar);
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

    @Override//------------1
    public void onResume() {
        super.onResume();
        if (null != mProfileHelper) {
            //获取用户ID并且,并且将ID传入方法updateUserProfile(),联网获取用户的详细信息
            mProfileHelper.getMyProfile();
        }
        /*
        if(needLogin()) {
            mProfileName.setText("请登录");
        } else {
            updateUserInfo(ILiveLoginManager.getInstance().getMyUserId(),
                    ILiveLoginManager.getInstance().getMyUserId(), null);
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
