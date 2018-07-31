package com.funfit.usjr.thesis.funfitv2.login;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dj on 2/4/2016.
 */
public class LevelUpFragment extends Fragment {
    @BindView(R.id.filter_sedentary)
    ImageView mFilterSedentary;
    @BindView(R.id.filter_lowactive)
    ImageView mFilterLowActive;
    @BindView(R.id.filter_active)
    ImageView mFilterActive;
    @BindView(R.id.filter_veryactive)
    ImageView mFilterVeryActive;

    @BindView(R.id.img_sedentary)
    ImageView mImageSedentary;
    @BindView(R.id.img_lowactive)
    ImageView mImageLowActive;
    @BindView(R.id.img_active)
    ImageView mImageActive;
    @BindView(R.id.img_veryactive)
    ImageView mImageVeryActive;

    @BindView(R.id.txt_sedentary)
    TextView mTextSedentary;
    @BindView(R.id.txt_lowactive)
    TextView mTextLowActive;
    @BindView(R.id.txt_active)
    TextView mTextActive;
    @BindView(R.id.txt_veryactive)
    TextView mTextVeryActive;

    @BindView(R.id.fab_forward)
    FloatingActionButton mFabForward;

    private SharedPreferences mPrefUserData;
    private String mUserLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_level_up, container, false);
        ButterKnife.bind(this, rootView);
        mPrefUserData = getActivity().getSharedPreferences(Constants.USER_PREF_ID, getActivity().MODE_PRIVATE);

        Glide.with(this).load("http://media.fashionnetwork.com/m/858d/badb/6b5c/056a/3f0a/9af3/2287/3515/f45c/faa6/faa6.jpg")
                .centerCrop().crossFade().into(mImageSedentary);
        Glide.with(this).load("https://3.bp.blogspot.com/-U1IGj7QIUKQ/V7bjjEAq4qI/AAAAAAAAALU/xFZzmdJp_7Q7nKICT-8zvzgH8zBtTCmlQCLcB/s1600/nike6.jpg")
                .centerCrop().crossFade().into(mImageLowActive);
        Glide.with(this).load("https://cached.imagescaler.hbpl.co.uk/resize/scaleWidth/815/cached.offlinehbpl.hbpl.co.uk/news/OMC/fitbitcropped-20150915075924465.jpg")
                .centerCrop().crossFade().into(mImageActive);
        Glide.with(this).load("https://instagram.fmnl8-1.fna.fbcdn.net/vp/57ab702bb687f2590c7856b19e27da99/5BD74DDD/t51.2885-15/e35/37262452_2060945007273657_6842814206056595456_n.jpg")
                .centerCrop().crossFade().into(mImageVeryActive);

        mTextSedentary.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
        mTextLowActive.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
        mTextActive.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
        mTextVeryActive.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));

        mFabForward.hide();
        return rootView;
    }

    @OnClick(R.id.sedentary_container)
    public void onSedentaryClick() {
        mUserLevel = "sedentary";
        mFilterSedentary.setImageResource(R.color.filter_sedentary);
        mFilterLowActive.setImageResource(R.color.filter_passive);
        mFilterActive.setImageResource(R.color.filter_passive);
        mFilterVeryActive.setImageResource(R.color.filter_passive);
        mFabForward.show();
        SignUpActivity.showSnackbar((SignUpActivity) getActivity(), getString(R.string.sedentary_description));
    }

    @OnClick(R.id.lowactive_container)
    public void onLowActiveClick() {
        mUserLevel = "low active";
        mFilterSedentary.setImageResource(R.color.filter_passive);
        mFilterLowActive.setImageResource(R.color.filter_lowactive);
        mFilterActive.setImageResource(R.color.filter_passive);
        mFilterVeryActive.setImageResource(R.color.filter_passive);
        mFabForward.show();
        SignUpActivity.showSnackbar((SignUpActivity) getActivity(), getString(R.string.low_active_description));
    }

    @OnClick(R.id.active_container)
    public void onActiveClick() {
        mUserLevel = "active";
        mFilterSedentary.setImageResource(R.color.filter_passive);
        mFilterLowActive.setImageResource(R.color.filter_passive);
        mFilterActive.setImageResource(R.color.filter_active);
        mFilterVeryActive.setImageResource(R.color.filter_passive);
        mFabForward.show();
        SignUpActivity.showSnackbar((SignUpActivity) getActivity(), getString(R.string.active_description));
    }

    @OnClick(R.id.veryactive_container)
    public void onVeryActiveClick() {
        mUserLevel = "very active";
        mFilterSedentary.setImageResource(R.color.filter_passive);
        mFilterLowActive.setImageResource(R.color.filter_passive);
        mFilterActive.setImageResource(R.color.filter_passive);
        mFilterVeryActive.setImageResource(R.color.filter_veryactive);
        mFabForward.show();
        SignUpActivity.showSnackbar((SignUpActivity) getActivity(), getString(R.string.very_active_description));
    }

    @OnClick(R.id.fab_forward)
    public void onClickForward() {
        mPrefUserData.edit().putString(Constants.PROFILE_ACTIVITY_LEVEL, mUserLevel).apply();
        ((ViewPager) getActivity().findViewById(R.id.viewpager_signup)).setCurrentItem(2);
    }
}
