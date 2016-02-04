package com.funfit.usjr.thesis.funfitv2.login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.funfit.usjr.thesis.funfitv2.R;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dj on 2/4/2016.
 */
public class SignUpFragment extends Fragment implements View.OnFocusChangeListener, RadioGroup.OnCheckedChangeListener{
    @Bind(R.id.img_profile)
    ImageView mImageProfile;
    @Bind(R.id.edt_fname)
    EditText mEditFname;
    @Bind(R.id.edt_lname)
    EditText mEditLname;
    @Bind(R.id.edt_email)
    EditText mEditEmail;
    @Bind(R.id.edt_dob)
    EditText mEditDob;
    @Bind(R.id.edt_weight)
    EditText mEditWeight;
    @Bind(R.id.edt_height)
    EditText mEditHeight;
    @Bind(R.id.rad_group_gender)
    RadioGroup mRadioGroup;
    @Bind(R.id.fab_forward)
    FloatingActionButton mFabForward;

    private SharedPreferences mPrefUserData;

    private int mYear, mMonth, mDay;
    private String[] mWeightUnit, mHeightUnit, mFeetValues;
    private String mImageUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, rootView);
        mPrefUserData = getActivity().getSharedPreferences(SignUpActivity.USER_PREF_ID, getActivity().MODE_PRIVATE);

        Intent i = getActivity().getIntent();
        mImageUrl = i.getStringExtra(SignUpActivity.PROFILE_IMG_URL);
        mEditFname.setText(i.getStringExtra(SignUpActivity.PROFILE_FNAME));
        mEditLname.setText(i.getStringExtra(SignUpActivity.PROFILE_LNAME));
        mEditEmail.setText(i.getStringExtra(SignUpActivity.PROFILE_EMAIL));
        Glide.with(this).load(mImageUrl)
                .centerCrop().crossFade().into(mImageProfile);
        mFabForward.hide();

        mEditFname.setOnFocusChangeListener(this);
        mEditLname.setOnFocusChangeListener(this);
        mEditEmail.setOnFocusChangeListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);

        mFeetValues = getResources().getStringArray(R.array.ft_values);
        mWeightUnit = getResources().getStringArray(R.array.weight);
        mHeightUnit = getResources().getStringArray(R.array.height_measurement);

        return rootView;
    }

    private void checkFields(){
        if(!mEditFname.getText().toString().isEmpty() &&
                !mEditLname.getText().toString().isEmpty() &&
                !mEditEmail.getText().toString().isEmpty() &&
                !mEditDob.getText().toString().isEmpty() &&
                !mEditHeight.getText().toString().isEmpty() &&
                !mEditWeight.getText().toString().isEmpty()){
            mFabForward.show();
        }else
            mFabForward.hide();
    }

    @OnClick(R.id.edt_dob)
    public void onClickDob(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mEditDob.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);
                        checkFields();
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    @OnClick(R.id.edt_weight)
    public void onClickWeight(){
        final Dialog d = new Dialog(getActivity());
        d.setTitle("Weight");
        d.setContentView(R.layout.dialog_info);
        Button b1 = (Button) d.findViewById(R.id.btn_okay);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        final NumberPicker tp = (NumberPicker) d.findViewById(R.id.textPicker);
        tp.setMinValue(0);
        tp.setMaxValue(1);
        tp.setDisplayedValues( mWeightUnit );

        np.setMaxValue(250);
        np.setMinValue(90);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                mEditWeight.setText(String.valueOf(np.getValue() + " " + mWeightUnit[tp.getValue()]));
                d.dismiss();
                checkFields();
            }
        });
        d.show();
    }

    @OnClick(R.id.edt_height)
    public void onClickHeight(){
        final Dialog d = new Dialog(getActivity());
        d.setTitle("Height");
        d.setContentView(R.layout.dialog_info);
        Button b1 = (Button) d.findViewById(R.id.btn_okay);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        final NumberPicker tp = (NumberPicker) d.findViewById(R.id.textPicker);
        tp.setMinValue(0);
        tp.setMaxValue(1);
        tp.setDisplayedValues( mHeightUnit );

        np.setMaxValue(250);
        np.setMinValue(90);
        np.setWrapSelectorWheel(false);
        tp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(newVal==1){
                    np.setMinValue(0);
                    np.setMaxValue(mFeetValues.length-1);
                    np.setDisplayedValues(mFeetValues);
                }else{
                    np.setDisplayedValues(null);
                    np.setMinValue(90);
                    np.setMaxValue(250);
                }
            }
        });
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(tp.getValue()==0)
                    mEditHeight.setText(String.valueOf(np.getValue()
                            + " " + mHeightUnit[tp.getValue()]));
                else
                    mEditHeight.setText(String.valueOf(mFeetValues[np.getValue()]
                            + " " + mHeightUnit[tp.getValue()]));
                d.dismiss();
                checkFields();
            }
        });
        d.show();
    }

    @OnClick(R.id.fab_forward)
    public void onClickForward(){
        mPrefUserData.edit().putString(SignUpActivity.PROFILE_FNAME, mEditFname.getText().toString()).apply();
        mPrefUserData.edit().putString(SignUpActivity.PROFILE_LNAME, mEditLname.getText().toString()).apply();
        mPrefUserData.edit().putString(SignUpActivity.PROFILE_DOB, mEditDob.getText().toString()).apply();
        mPrefUserData.edit().putString(SignUpActivity.PROFILE_EMAIL, mEditEmail.getText().toString()).apply();
        mPrefUserData.edit().putString(SignUpActivity.PROFILE_IMG_URL, mImageUrl).apply();
        if(mRadioGroup.getCheckedRadioButtonId()==R.id.rad_btn_male)
            mPrefUserData.edit().putString(SignUpActivity.PROFILE_GENDER, "male").apply();
        else
            mPrefUserData.edit().putString(SignUpActivity.PROFILE_GENDER, "female").apply();
        mPrefUserData.edit().putString(SignUpActivity.PROFILE_WEIGHT, mEditWeight.getText().toString()).apply();
        mPrefUserData.edit().putString(SignUpActivity.PROFILE_HEIGHT, mEditHeight.getText().toString()).apply();

        ((ViewPager)getActivity().findViewById(R.id.viewpager_signup)).setCurrentItem(1);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        checkFields();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        checkFields();
    }
}