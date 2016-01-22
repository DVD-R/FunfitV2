package com.funfit.usjr.thesis.funfitv2.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.funfit.usjr.thesis.funfitv2.R;

/**
 * Created by Wasim on 11-06-2015.
 */
public class ViewPagerAdapter extends PagerAdapter implements View.OnClickListener{

    private Context mContext;
    private int mResources;
    private ImageView mImageView;
    private FrameLayout mActivityLevelLayout;
    private FrameLayout mActivityWeightLayout;
    private FrameLayout mActivityHeightLayout;
    private LinearLayout mSendentary;
    private LinearLayout mLowActive;
    private LinearLayout mActive;
    private LinearLayout mVeryActive;
    private Spinner mWeight;
    private Spinner mHeightSpinner;
    private Spinner mFtValue;
    private EditText mHeightEdt;
    private EditText mWeightEdt;
    private double height;
    private double weight;
    private String activityLevel;
    private SharedPreferences mPrefHealthSetup;
    private SharedPreferences.Editor editor;

    public ViewPagerAdapter(Context mContext, int mResources) {
        this.mContext = mContext;
        this.mResources = mResources;
    }

    @Override
    public int getCount() {
        return mResources;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);
        mSendentary = (LinearLayout) itemView.findViewById(R.id.sendentary);
        mLowActive = (LinearLayout) itemView.findViewById(R.id.lowActive);
        mActive = (LinearLayout) itemView.findViewById(R.id.active);
        mWeight = (Spinner) itemView.findViewById(R.id.weight);
        mVeryActive = (LinearLayout) itemView.findViewById(R.id.veryActive);
        mImageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        mActivityLevelLayout = (FrameLayout) itemView.findViewById(R.id.activityLevelLayout);
        mActivityWeightLayout = (FrameLayout) itemView.findViewById(R.id.activityWeightLayout);
        mActivityHeightLayout = (FrameLayout) itemView.findViewById(R.id.activityHeightLayout);
        mHeightEdt = (EditText) itemView.findViewById(R.id.heightEdt);
        mWeightEdt = (EditText) itemView.findViewById(R.id.weightEdt);
        mHeightSpinner = (Spinner) itemView.findViewById(R.id.heightSpnr);
        mFtValue = (Spinner) itemView.findViewById(R.id.ftValueSpnr);
        mPrefHealthSetup = mContext.getSharedPreferences("USER_HEALTH_DATA_PREF", Context.MODE_PRIVATE);
        editor = mPrefHealthSetup.edit();
        mSendentary.setOnClickListener(this);
        mLowActive.setOnClickListener(this);
        mActive.setOnClickListener(this);
        mVeryActive.setOnClickListener(this);
        mWeightEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mWeight.getSelectedItem().toString() == "kg") {

                    try {
                        weight = Double.parseDouble(s.toString());
                    } catch (Exception e) {
                        Log.e("Weight kg", String.valueOf(e));
                    }
                }else{
                    try {
                        weight = Double.parseDouble(s.toString())/2.2046;
                    }catch (Exception e){
                        Log.e("Weight lb", String.valueOf(e));
                    }
                }
            }
        });
        mHeightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getSelectedItem().toString()) {
                    case "ft/in":
                        mHeightEdt.setVisibility(View.GONE);
                        mFtValue.setVisibility(View.VISIBLE);
                        break;
                    case "cm":
                        mHeightEdt.setVisibility(View.VISIBLE);
                        mFtValue.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switch (position){
            case 0:
                Glide.with(mContext)
                        .load(Uri.parse("http://dve19nd2omvt4.cloudfront.net/mobile-living/wp-content/uploads/2013/11/campaign-socal-fitness-wallpapers-con-android-wallpaper-weak-of-will.jpg"))
                        .centerCrop()
                        .into(mImageView);
                break;
            case 1:
                mImageView.setVisibility(View.GONE);
                mActivityLevelLayout.setVisibility(View.VISIBLE);
                break;
            case 2:

                mImageView.setVisibility(View.GONE);
                mActivityWeightLayout.setVisibility(View.VISIBLE);

                break;
            case 3:
                mImageView.setVisibility(View.GONE);
                mActivityHeightLayout.setVisibility(View.VISIBLE);
                break;
        }
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.sendentary:
                    activityLevel = "Sendentary";
                    Toast.makeText(mContext, activityLevel,Toast.LENGTH_SHORT).show();

                    break;
                case R.id.lowActive:
                    activityLevel = "Low Active";
                    Toast.makeText(mContext, activityLevel,Toast.LENGTH_SHORT).show();

                    break;
                case R.id.active:
                    activityLevel = "Active";
                    Toast.makeText(mContext, activityLevel,Toast.LENGTH_SHORT).show();

                    break;
                case R.id.veryActive:
                    activityLevel = "Very Active";
                    Toast.makeText(mContext, activityLevel,Toast.LENGTH_SHORT).show();

                    break;
            }
        }catch (Exception e){}
    }


    public void sendHealthPref(){


        if (mHeightSpinner.getSelectedItem().toString() == "ft/in") {
            String a = mHeightSpinner.getSelectedItem().toString();
            String phrase[] = a.split("[']");
            int feet = 0;
            int inches = 0;

            for (int i = 0; i < phrase.length; i++) {
                feet = Integer.parseInt(phrase[i]);
                inches = Integer.parseInt(phrase[i]);
            }

            int partialResult = (feet * 12) + inches;
            height = partialResult * 2.54;
        }else {
            try {
                height = Double.parseDouble(mHeightEdt.getText().toString());
            }catch (Exception e){}
        }

        editor.putString("WEIGHT", String.valueOf(weight));
        editor.putString("HEIGHT", String.valueOf(height));
        editor.putString("ACTIVITY_LEVEL", String.valueOf(activityLevel));
        editor.commit();
    }
}