package com.funfit.usjr.thesis.funfitv2.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
    @Bind(R.id.rad_group_gender)
    RadioGroup mRadioGroup;
    @Bind(R.id.fab_forward)
    FloatingActionButton mFabForward;

    private int mYear, mMonth, mDay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, rootView);

        Intent i = getActivity().getIntent();
        mEditFname.setText(i.getStringExtra(SignUpActivity.PROFILE_FNAME));
        mEditLname.setText(i.getStringExtra(SignUpActivity.PROFILE_LNAME));
        mEditEmail.setText(i.getStringExtra(SignUpActivity.PROFILE_EMAIL));
        Glide.with(this).load(i.getStringExtra(SignUpActivity.PROFILE_IMG_URL))
                .centerCrop().crossFade().into(mImageProfile);
        mFabForward.hide();

        mEditFname.setOnFocusChangeListener(this);
        mEditLname.setOnFocusChangeListener(this);
        mEditEmail.setOnFocusChangeListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);

        return rootView;
    }

    private void checkFields(){
        if(!mEditFname.getText().toString().isEmpty() &&
                !mEditLname.getText().toString().isEmpty() &&
                !mEditEmail.getText().toString().isEmpty() &&
                !mEditDob.getText().toString().isEmpty()){
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

    @OnClick(R.id.fab_forward)
    public void onClickForward(){
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