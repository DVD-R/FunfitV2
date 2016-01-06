package com.funfit.usjr.thesis.funfitv2.healthPreference;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Spinner;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.listener.LeftGestureListener;
import com.funfit.usjr.thesis.funfitv2.listener.RightGestureListener;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by victor on 1/6/2016.
 */
public class HealthPreferenceActivity extends AppCompatActivity {


    GestureDetectorCompat gestureDetectorCompat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_preference_activity);

        gestureDetectorCompat = new GestureDetectorCompat(this, new RightGestureListener(this));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}