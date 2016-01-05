package com.funfit.usjr.thesis.funfitv2.healthPreference;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.funfit.usjr.thesis.funfitv2.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by victor on 1/6/2016.
 */
public class HealthPreferenceActivity extends AppCompatActivity {

    @Bind(R.id.activity_level_spnr)Spinner activity_level;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_preference_activity);
    }

    @OnClick(R.id.setupBtn)
    public void SetUp(){
    }

}