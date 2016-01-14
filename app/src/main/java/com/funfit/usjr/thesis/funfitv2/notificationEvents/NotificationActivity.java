package com.funfit.usjr.thesis.funfitv2.notificationEvents;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.funfit.usjr.thesis.funfitv2.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 1/15/2016.
 */
public class NotificationActivity extends AppCompatActivity{
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
