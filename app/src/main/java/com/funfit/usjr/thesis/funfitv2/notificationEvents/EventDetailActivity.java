package com.funfit.usjr.thesis.funfitv2.notificationEvents;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.maps.MapsFragment;
import com.funfit.usjr.thesis.funfitv2.model.Events;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dj on 1/22/2016.
 */
public class EventDetailActivity extends AppCompatActivity {
    private static final String TAG = EventDetailActivity.class.getSimpleName();
    private static final int REQUEST_CODE_QR_SCAN = 40000;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab_qr)
    FloatingActionButton mFabQr;
    @Bind(R.id.txt_event)
    TextView mTextEvent;
    @Bind(R.id.txt_bounty)
    TextView mTextBounty;
    @Bind(R.id.img_event)
    ImageView mImageEvent;

    private Events mEvents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextEvent.setTypeface(Typeface.createFromAsset(getAssets(), "HelveticaBold.otf"));
        mEvents = (Events) getIntent().getSerializableExtra("EVENT");

        Glide.with(this)
                .load(mEvents.getImgUrl())
                .asBitmap()
                .centerCrop()
                .into((new BitmapImageViewTarget(mImageEvent) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        super.onResourceReady(bitmap, anim);
                        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                try {
                                    Palette.Swatch swatch = palette.getLightMutedSwatch();
                                    int color = palette.getMutedColor(swatch.getTitleTextColor());
                                    mFabQr.setBackgroundTintList(ColorStateList.valueOf(color));
                                }catch (NullPointerException e){
                                    Log.e(TAG, "Failed to load color");
                                }
                            }
                        });
                    }
                }));
        mTextEvent.setText(mEvents.getEventName());
        mTextBounty.setText(mEvents.getReward());
    }

    @OnClick(R.id.fab_qr)
    public void onClickQr(){
        startActivityForResult(new Intent(this, QRScannerActivity.class), REQUEST_CODE_QR_SCAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK == resultCode) {

            if (REQUEST_CODE_QR_SCAN == requestCode) {

                Toast.makeText(this,data.getStringExtra("RAW_RESULT"),Toast.LENGTH_LONG).show();
            }
        }
    }
}