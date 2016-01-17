package com.funfit.usjr.thesis.funfitv2.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Constants;

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
        mImageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        mActivityLevelLayout = (FrameLayout) itemView.findViewById(R.id.activityLevelLayout);
        mActivityWeightLayout = (FrameLayout) itemView.findViewById(R.id.activityWeightLayout);
        mActivityHeightLayout = (FrameLayout) itemView.findViewById(R.id.activityHeightLayout);
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

    }
}
