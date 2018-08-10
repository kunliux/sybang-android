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
public class ProfileBasicFragment extends Fragment {


    @BindView(R.id.name_edit_text)
    TextInputEditText mNameEditText;
    @BindView(R.id.nation_edit_text)
    TextInputEditText mNationEditText;
    @BindView(R.id.native_place_edit_text)
    TextInputEditText mNativePlaceEditText;
    @BindView(R.id.phone_edit_text)
    TextInputEditText mPhoneEditText;
    @BindView(R.id.emergency_contact_edit_text)
    TextInputEditText mEmergencyContactEditText;
    @BindView(R.id.id_number_edit_text)
    TextInputEditText mIdNumberEditText;
    @BindView(R.id.disabled_number_edit_text)
    TextInputEditText mDisabledNumberEditText;
    @BindView(R.id.gender_radio_group)
    RadioGroup mGenderRadioGroup;
    Unbinder unbinder;

    UserProfile mUserInfo;
    ProfileEditActivity mActivity;

    public static ProfileBasicFragment newInstance() {
        ProfileBasicFragment fragment = new ProfileBasicFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile_basic, container, false);
        unbinder = ButterKnife.bind(this, view);

        mGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        mUserInfo.setGender(Constants.GENDER_MALE);
                        break;
                    case R.id.female:
                        mUserInfo.setGender(Constants.GENDER_FEMALE);
                        break;
                    default:
                        mUserInfo.setGender(Constants.GENDER_DEFAULT);
                }
            }
        });

        return view;
    }

    public boolean validate() {
        boolean valid = true;

        String nickname = mNameEditText.getText().toString().trim();
        String phone = mPhoneEditText.getText().toString().trim();
        String emergencyContact = mEmergencyContactEditText.getText().toString().trim();
        String idNumber = mIdNumberEditText.getText().toString().trim();
        String disabilityNumber = mDisabledNumberEditText.getText().toString().trim();

        if (nickname.isEmpty()) {
            mNameEditText.setError(getString(R.string.field_required));
            valid = false;
        } else {
            mUserInfo.setNickname(nickname);
            mNameEditText.setError(null);
        }

        if (phone.isEmpty() || !isPhoneValid(phone)) {
            mPhoneEditText.setError(getString(R.string.error_invalid_phone));
            valid = false;
        } else {
            mUserInfo.setPhone("86-"+phone);
            mPhoneEditText.setError(null);
        }

        if (emergencyContact.isEmpty() || !isPhoneValid(emergencyContact)) {
            mEmergencyContactEditText.setError(getString(R.string.error_invalid_phone));
            valid = false;
        } else {
            mUserInfo.setEmergencyContact("86-"+emergencyContact);
            mEmergencyContactEditText.setError(null);
        }

        if (idNumber.isEmpty() || !isIdNumberValid(idNumber)) {
            mIdNumberEditText.setError(getString(R.string.error_invalid_id_number));
            valid = false;
        } else {
            mUserInfo.setIdNumber(idNumber);
            mIdNumberEditText.setError(null);
        }

        if (disabilityNumber.isEmpty() || !isDisabilityNumberValid(disabilityNumber)) {
            mDisabledNumberEditText.setError(getString(R.string.error_invalid_disability_number));
            valid = false;
        } else {
            mUserInfo.setDisabilityNumber(disabilityNumber);
            mDisabledNumberEditText.setError(null);
        }

        return valid;
    }

    private boolean isPhoneValid(String phone) {
        return phone.matches("^1[34578][0-9]{9}$");
    }

    private boolean isIdNumberValid(String idNumber) {
        return idNumber.matches("\\d{17}[[0-9],0-9xX]");
    }

    private boolean isDisabilityNumberValid(String number) {
        return number.length() == 20;
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
                    mActivity.replaceFragment(ProfilePresentFragment.newInstance());
                    mActivity.setUserBasicInfo(mUserInfo);
                }
                return true;
            case android.R.id.home:
                mActivity.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
