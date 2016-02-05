package com.funfit.usjr.thesis.funfitv2.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.User;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 2/3/2016.
 */
public class SignUpActivity extends AppCompatActivity {
    private static final String LOG_TAG = SignUpActivity.class.getSimpleName();

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

    public static void registerUserToFirebase(final SharedPreferences userData){
        final String email = Utils.encodeEmail(userData.getString(Constants.PROFILE_EMAIL, null));
        final Firebase userFirebase = new Firebase(Constants.FIREBASE_URL_USERS)
                .child(email);
        userFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    HashMap<String, Object> timestampJoined = new HashMap<>();
                    timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

                    User newUser = new User(userData.getString(Constants.PROFILE_FNAME, null),
                            userData.getString(Constants.PROFILE_LNAME, null),
                            email,
                            userData.getString(Constants.PROFILE_GENDER, null),
                            userData.getString(Constants.PROFILE_DOB, null),
                            userData.getString(Constants.PROFILE_IMG_URL, null),
                            userData.getString(Constants.PROFILE_WEIGHT, null),
                            userData.getString(Constants.PROFILE_HEIGHT, null),
                            userData.getString(Constants.PROFILE_ACTIVITY_LEVEL, null),
                            userData.getString(Constants.PROFILE_CLUSTER, null),
                            timestampJoined);
                    userFirebase.setValue(newUser);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(LOG_TAG, firebaseError.getMessage());
            }
        });
    }
}
