package com.funfit.usjr.thesis.funfitv2.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.main.MainActivity;
import com.funfit.usjr.thesis.funfitv2.model.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dj on 2/4/2016.
 */
public class ClusterUpFragment extends Fragment {
    @Bind(R.id.img_velocity)
    ImageView mImageVelocity;
    @Bind(R.id.img_impulse)
    ImageView mImageImpulse;
    @Bind(R.id.filter_impulse)
    ImageView mFilterImpulse;
    @Bind(R.id.filter_velocity)
    ImageView mFilterVelocity;
    @Bind(R.id.txt_velocity)
    TextView mTextVelocity;
    @Bind(R.id.txt_impulse)
    TextView mTextImpulse;
    @Bind(R.id.fab_forward)
    FloatingActionButton mFabForward;

    private SharedPreferences mPrefUserData;
    private String mUserCluster;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cluster_up, container, false);
        ButterKnife.bind(this, rootView);
        mPrefUserData = getActivity().getSharedPreferences(Constants.USER_PREF_ID, getActivity().MODE_PRIVATE);

        Glide.with(this).load("https://igcdn-photos-b-a.akamaihd.net/hphotos-ak-xft1/t51.2885-15/e15/11327164_866674696733105_1005355604_n.jpg")
                .centerCrop().crossFade().into(mImageVelocity);
        Glide.with(this).load("https://igcdn-photos-e-a.akamaihd.net/hphotos-ak-xfa1/t51.2885-15/e15/11256798_382539915278204_1079867520_n.jpg")
                .centerCrop().crossFade().into(mImageImpulse);

        mTextVelocity.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
        mTextImpulse.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "HelveticaBold.otf"));
        mFabForward.hide();

        return rootView;
    }

    @OnClick(R.id.velocity_container)
    public void onVelocityClick(){
        mUserCluster = "velocity";
        mFilterVelocity.setImageResource(R.color.filter_velocity);
        mFilterImpulse.setImageResource(R.color.filter_passive);
        mFabForward.show();
    }

    @OnClick(R.id.impulse_container)
    public void onImpulseClick(){
        mUserCluster = "impulse";
        mFilterImpulse.setImageResource(R.color.filter_impulse);
        mFilterVelocity.setImageResource(R.color.filter_passive);
        mFabForward.show();
    }

    @OnClick(R.id.fab_forward)
    public void onClickForward(){
        mPrefUserData.edit().putString(Constants.PROFILE_CLUSTER, mUserCluster).apply();

        SignUpActivity.registerUserToFirebase(mPrefUserData);
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}