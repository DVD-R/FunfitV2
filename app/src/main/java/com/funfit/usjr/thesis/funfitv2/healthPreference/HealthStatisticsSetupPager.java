package com.funfit.usjr.thesis.funfitv2.healthPreference;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.adapter.ViewPagerAdapter;
import com.funfit.usjr.thesis.funfitv2.main.MainActivity;
import com.funfit.usjr.thesis.funfitv2.tutorial.TutorialViewPagerAdapter;
import com.funfit.usjr.thesis.funfitv2.tutorial.ZoomOutPageTransformer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by victor on 1/6/2016.
 */
public class HealthStatisticsSetupPager extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener{
    @Bind(R.id.pager_introduction)ViewPager mViewPager;

    @Bind(R.id.btn_next)ImageButton mBtnNext;
    @Bind(R.id.btn_finish)ImageButton mBtnFinish;
    @Bind(R.id.viewPagerCountDots)LinearLayout mViewPagerCountDots;
    private SharedPreferences mPrefHealthSetup;

    private ViewPagerAdapter mViewPagerAdapter;
    private TutorialViewPagerAdapter mTutorialPagerAdapter;
    private int mDotsCount;
    private ImageView[] mDots;
    private int resource = 4;
    private double height;
    private double weight;
    private String activityLevel;
    AlertDialog alertDialog;
    final private static int DIALOG_POP = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        ButterKnife.bind(this);

        //checkFirstRun();
        configure();
    }

    private void configure(){
        mViewPagerAdapter = new ViewPagerAdapter(this, resource);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(this);
        setUiController();
    }

    private void setUiController() {

        mDotsCount = mViewPagerAdapter.getCount();
        mDots = new ImageView[mDotsCount];

        for (int i = 0; i < mDotsCount; i++) {
            mDots[i] = new ImageView(this);
            mDots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            mViewPagerCountDots.addView(mDots[i], params);
        }

        mDots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @OnClick(R.id.btn_next)
    public void next(){
        mViewPager.setCurrentItem((mViewPager.getCurrentItem() < mDotsCount) ?
                mViewPager.getCurrentItem() + 1 : 0);
    }

    @OnClick(R.id.btn_finish)
    public void finish(){
//        Intent i = new Intent(this, MainActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
        mViewPagerAdapter.sendHealthPref();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mDotsCount; i++){
            mDots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        mDots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        if (position + 1 == mDotsCount){
            mBtnNext.setVisibility(View.GONE);
            mBtnFinish.setVisibility(View.VISIBLE);
        }else {
            mBtnNext.setVisibility(View.VISIBLE);
            mBtnFinish.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

    }

}