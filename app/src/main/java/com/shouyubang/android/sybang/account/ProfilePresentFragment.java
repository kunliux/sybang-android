package com.shouyubang.android.sybang.account;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.shouyubang.android.sybang.R;
import com.shouyubang.android.sybang.model.UserProfile;
import com.shouyubang.android.sybang.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePresentFragment extends Fragment {

    @BindView(R.id.city_edit_text)
    TextInputEditText mCityEditText;
    @BindView(R.id.address_edit_text)
    TextInputEditText mAddressEditText;
    @BindView(R.id.education_edit_text)
    TextInputEditText mEducationEditText;
    @BindView(R.id.academy_edit_text)
    TextInputEditText mAcademyEditText;
    @BindView(R.id.speciality_edit_text)
    TextInputEditText mSpecialityEditText;
    @BindView(R.id.interests_edit_text)
    TextInputEditText mInterestsEditText;
    @BindView(R.id.reading_radio_group)
    RadioGroup mReadingRadioGroup;
    @BindView(R.id.writing_radio_group)
    RadioGroup mWritingRadioGroup;
    @BindView(R.id.insurance_radio_group)
    RadioGroup mInsuranceRadioGroup;
    Unbinder unbinder;

    UserProfile mUserInfo;
    ProfileEditActivity mActivity;

    public static ProfilePresentFragment newInstance() {
        ProfilePresentFragment fragment = new ProfilePresentFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile_present, container, false);
        unbinder = ButterKnife.bind(this, view);

        mReadingRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.reading_good:
                        mUserInfo.setReadingLevel(Constants.READING_GOOD);
                        break;
                    case R.id.reading_medium:
                        mUserInfo.setReadingLevel(Constants.READING_MEDIUM);
                        break;
                    case R.id.reading_poor:
                        mUserInfo.setReadingLevel(Constants.READING_POOR);
                        break;
                    default:
                        mUserInfo.setReadingLevel(Constants.READING_DEFAULT);
                }
            }
        });
        mWritingRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.writing_good:
                        mUserInfo.setGender(Constants.WRITING_GOOD);
                        break;
                    case R.id.writing_medium:
                        mUserInfo.setGender(Constants.WRITING_MEDIUM);
                        break;
                    case R.id.writing_poor:
                        mUserInfo.setGender(Constants.WRITING_POOR);
                        break;
                    default:
                        mUserInfo.setGender(Constants.WRITING_DEFAULT);
                }
            }
        });
        mInsuranceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.insurance_true:
                        mUserInfo.setGender(Constants.INSURANCE_TRUE);
                        break;
                    case R.id.insurance_false:
                        mUserInfo.setGender(Constants.INSURANCE_FALSE);
                        break;
                    default:
                        mUserInfo.setGender(Constants.INSURANCE_FALSE);
                }
            }
        });

        return view;
    }

    public boolean validate() {
        boolean valid = true;

        String city = mCityEditText.getText().toString().trim();
        String address = mAddressEditText.getText().toString().trim();
        String education = mEducationEditText.getText().toString().trim();

        if (city.isEmpty()) {
            mCityEditText.setError(getString(R.string.field_required));
            valid = false;
        } else {
            mUserInfo.setCity(city);
            mCityEditText.setError(null);
        }

        if (address.isEmpty()) {
            mAddressEditText.setError(getString(R.string.field_required));
            valid = false;
        } else {
            mUserInfo.setAddress(address);
            mAddressEditText.setError(null);
        }

        if (education.isEmpty()) {
            mEducationEditText.setError(getString(R.string.field_required));
            valid = false;
        } else {
            mUserInfo.setEducation(education);
            mEducationEditText.setError(null);
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
                    mActivity.replaceFragment(ProfileWorkFragment.newInstance());
                    mActivity.setUserPresentInfo(mUserInfo);
                }
                return true;
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
