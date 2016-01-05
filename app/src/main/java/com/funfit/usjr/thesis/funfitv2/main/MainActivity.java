package com.funfit.usjr.thesis.funfitv2.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.funfit.usjr.thesis.funfitv2.R;

public class MainActivity extends AppCompatActivity {


//    HealthPreferenceActivity healthPreferenceFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        healthPreferenceFragment = new HealthPreferenceActivity();
//        getSupportFragmentManager().beginTransaction().replace(R.id.container,healthPreferenceFragment).commit();
    }
}