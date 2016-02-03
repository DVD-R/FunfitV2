package com.funfit.usjr.thesis.funfitv2.login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.maps.MapsFragment;
import com.funfit.usjr.thesis.funfitv2.mealPlan.MealPlanActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dj on 2/3/2016.
 */
public class SignUpActivity extends AppCompatActivity {
    public static final String PROFILE_IMG_URL = "img_profile";
    public static final String PROFILE_FNAME = "profile_fname";
    public static final String PROFILE_LNAME = "profile_lname";
    public static final String PROFILE_EMAIL = "profile_email";

    @Bind(R.id.viewpager_signup)
    ViewPager mViewPager;
    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SignUpFragment(), "User Details");
        adapter.addFragment(new ClusterUpFragment(), "Cluster");
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position==1){
                    showSnackbar("Pick a Side");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem()==1)
            mViewPager.setCurrentItem(0);
        else
            super.onBackPressed();
    }

    private void showSnackbar(String message){
        Snackbar snackbar = Snackbar
                .make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static class SignUpFragment extends Fragment implements View.OnFocusChangeListener, RadioGroup.OnCheckedChangeListener{
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
            ButterKnife.bind(SignUpFragment.this, rootView);

            Intent i = getActivity().getIntent();
            mEditFname.setText(i.getStringExtra(PROFILE_FNAME));
            mEditLname.setText(i.getStringExtra(PROFILE_LNAME));
            mEditEmail.setText(i.getStringExtra(PROFILE_EMAIL));
            Glide.with(this).load(i.getStringExtra(PROFILE_IMG_URL))
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

    public static class ClusterUpFragment extends Fragment{
        @Bind(R.id.img_velocity)
        ImageView mImageVelocity;
        @Bind(R.id.img_impulse)
        ImageView mImageImpulse;
        @Bind(R.id.txt_velocity)
        TextView mTextVelocity;
        @Bind(R.id.txt_impulse)
        TextView mTextImpulse;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_cluster_up, container, false);
            ButterKnife.bind(ClusterUpFragment.this, rootView);

            Intent i = getActivity().getIntent();
            Glide.with(this).load("https://igcdn-photos-b-a.akamaihd.net/hphotos-ak-xft1/t51.2885-15/e15/11327164_866674696733105_1005355604_n.jpg")
                    .centerCrop().crossFade().into(mImageVelocity);
            Glide.with(this).load("https://igcdn-photos-e-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-15/e15/11256798_382539915278204_1079867520_n.jpg")
                    .centerCrop().crossFade().into(mImageImpulse);

            mTextVelocity.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
            mTextImpulse.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));

            return rootView;
        }
    }
}
