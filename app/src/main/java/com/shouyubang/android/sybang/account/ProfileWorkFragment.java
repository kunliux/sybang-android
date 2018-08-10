package com.shouyubang.android.sybang.account;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.model.UserProfile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileWorkFragment extends Fragment {

    @BindView(R.id.experience_edit_text)
    TextInputEditText mExperienceEditText;
    @BindView(R.id.position_edit_text)
    TextInputEditText mPositionEditText;
    @BindView(R.id.salary_edit_text)
    TextInputEditText mSalaryEditText;
    @BindView(R.id.introduction_edit_text)
    TextInputEditText mIntroductionEditText;
    Unbinder unbinder;

    UserProfile mUserInfo;
    ProfileEditActivity mActivity;

    public static ProfileWorkFragment newInstance() {
        ProfileWorkFragment fragment = new ProfileWorkFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mActivity = (ProfileEditActivity) getActivity();
        mUserInfo = mActivity.getUserInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_work, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public boolean validate() {
        boolean valid = true;

        String position = mPositionEditText.getText().toString().trim();
        String salary = mSalaryEditText.getText().toString().trim();

        if (position.isEmpty()) {
            mPositionEditText.setError(getString(R.string.field_required));
            valid = false;
        } else {
            mUserInfo.setPosition(position);
            mPositionEditText.setError(null);
        }

        if (salary.isEmpty()) {
            mSalaryEditText.setError(getString(R.string.field_required));
            valid = false;
        } else {
            mUserInfo.setSalary(salary);
            mSalaryEditText.setError(null);
        }

        return valid;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_next_step:
                if(validate()) {
                    mActivity.setUserWorkInfo(mUserInfo);
                    mActivity.postUserInfo();
                }
                return true;
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
