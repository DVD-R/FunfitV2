package com.funfit.usjr.thesis.funfitv2.login;

import android.content.Intent;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dj on 2/4/2016.
 */
public class LevelUpFragment extends Fragment{
    @Bind(R.id.filter_sedentary)
    ImageView mFilterSedentary;
    @Bind(R.id.filter_lowactive)
    ImageView mFilterLowActive;
    @Bind(R.id.filter_active)
    ImageView mFilterActive;
    @Bind(R.id.filter_veryactive)
    ImageView mFilterVeryActive;

    @Bind(R.id.img_sedentary)
    ImageView mImageSedentary;
    @Bind(R.id.img_lowactive)
    ImageView mImageLowActive;
    @Bind(R.id.img_active)
    ImageView mImageActive;
    @Bind(R.id.img_veryactive)
    ImageView mImageVeryActive;

    @Bind(R.id.txt_sedentary)
    TextView mTextSedentary;
    @Bind(R.id.txt_lowactive)
    TextView mTextLowActive;
    @Bind(R.id.txt_active)
    TextView mTextActive;
    @Bind(R.id.txt_veryactive)
    TextView mTextVeryActive;

    @Bind(R.id.fab_forward)
    FloatingActionButton mFabForward;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_level_up, container, false);
        ButterKnife.bind(this, rootView);

        Glide.with(this).load("https://igcdn-photos-d-a.akamaihd.net/hphotos-ak-xtp1/t51.2885-15/e35/11849843_458857934284187_1515928877_n.jpg")
                .centerCrop().crossFade().into(mImageSedentary);
        Glide.with(this).load("https://igcdn-photos-h-a.akamaihd.net/hphotos-ak-xat1/t51.2885-15/e35/12132818_1637970616462087_2012634135_n.jpg")
                .centerCrop().crossFade().into(mImageLowActive);
        Glide.with(this).load("https://igcdn-photos-e-a.akamaihd.net/hphotos-ak-xat1/t51.2885-15/e35/11373868_1472205729769212_633445881_n.jpg")
                .centerCrop().crossFade().into(mImageActive);
        Glide.with(this).load("https://igcdn-photos-a-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-15/e15/11375943_1618372441780544_224968936_n.jpg")
                .centerCrop().crossFade().into(mImageVeryActive);

        mTextSedentary.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
        mTextLowActive.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
        mTextActive.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
        mTextVeryActive.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));

        mFabForward.hide();
        return rootView;
    }

    @OnClick(R.id.sedentary_container)
    public void onSedentaryClick(){
        mFilterSedentary.setImageResource(R.color.filter_sedentary);
        mFilterLowActive.setImageResource(R.color.filter_passive);
        mFilterActive.setImageResource(R.color.filter_passive);
        mFilterVeryActive.setImageResource(R.color.filter_passive);
        mFabForward.show();
        SignUpActivity.showSnackbar((SignUpActivity) getActivity(), getString(R.string.sedentary_description));
    }

    @OnClick(R.id.lowactive_container)
    public void onLowActiveClick(){
        mFilterSedentary.setImageResource(R.color.filter_passive);
        mFilterLowActive.setImageResource(R.color.filter_lowactive);
        mFilterActive.setImageResource(R.color.filter_passive);
        mFilterVeryActive.setImageResource(R.color.filter_passive);
        mFabForward.show();
        SignUpActivity.showSnackbar((SignUpActivity) getActivity(), getString(R.string.low_active_description));
    }

    @OnClick(R.id.active_container)
    public void onActiveClick(){
        mFilterSedentary.setImageResource(R.color.filter_passive);
        mFilterLowActive.setImageResource(R.color.filter_passive);
        mFilterActive.setImageResource(R.color.filter_active);
        mFilterVeryActive.setImageResource(R.color.filter_passive);
        mFabForward.show();
        SignUpActivity.showSnackbar((SignUpActivity) getActivity(), getString(R.string.active_description));
    }

    @OnClick(R.id.veryactive_container)
    public void onVeryActiveClick(){
        mFilterSedentary.setImageResource(R.color.filter_passive);
        mFilterLowActive.setImageResource(R.color.filter_passive);
        mFilterActive.setImageResource(R.color.filter_passive);
        mFilterVeryActive.setImageResource(R.color.filter_veryactive);
        mFabForward.show();
        SignUpActivity.showSnackbar((SignUpActivity) getActivity(), getString(R.string.very_active_description));
    }

    @OnClick(R.id.fab_forward)
    public void onClickForward(){
        ((ViewPager)getActivity().findViewById(R.id.viewpager_signup)).setCurrentItem(2);
    }
}
