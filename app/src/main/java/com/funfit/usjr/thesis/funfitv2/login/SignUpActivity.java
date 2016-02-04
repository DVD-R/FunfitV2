package com.funfit.usjr.thesis.funfitv2.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.funfit.usjr.thesis.funfitv2.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 2/3/2016.
 */
public class SignUpActivity extends AppCompatActivity {
    public static final String USER_PREF_ID = "user_info";
    public static final String PROFILE_IMG_URL = "img_profile";
    public static final String PROFILE_FNAME = "profile_fname";
    public static final String PROFILE_LNAME = "profile_lname";
    public static final String PROFILE_EMAIL = "profile_email";
    public static final String PROFILE_GENDER = "profile_gender";
    public static final String PROFILE_DOB = "profile_dob";
    public static final String PROFILE_WEIGHT = "profile_weight";
    public static final String PROFILE_HEIGHT = "profile_height";
    public static final String PROFILE_ACTIVITY_LEVEL = "profile_activity_level";
    public static final String PROFILE_CLUSTER = "profile_cluster";

    @Bind(R.id.viewpager_signup)
    ViewPager mViewPager;
    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        mViewPager.setOffscreenPageLimit(2);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SignUpFragment(), "User Details");
        adapter.addFragment(new LevelUpFragment(), "Choose Level");
        adapter.addFragment(new ClusterUpFragment(), "Cluster");
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position==1){
                    showSnackbar(SignUpActivity.this, "Choose your Level");
                }
                if(position==2){
                    showSnackbar(SignUpActivity.this, "Choose a Cluster");
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
        else if(mViewPager.getCurrentItem()==2)
            mViewPager.setCurrentItem(1);
        else
            super.onBackPressed();
    }

    public static void showSnackbar(SignUpActivity activity, String message){
        Snackbar snackbar = Snackbar
                .make(activity.mCoordinatorLayout, message, Snackbar.LENGTH_LONG);

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
}
