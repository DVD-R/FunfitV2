package com.funfit.usjr.thesis.funfitv2.healthPreference;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.listener.LeftGestureListener;
import com.funfit.usjr.thesis.funfitv2.listener.RightGestureListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by victor on 1/6/2016.
 */
public class HealthPreferenceActivity extends AppCompatActivity {

    @Bind(R.id.heightSpnr)Spinner mHeightSpinner;
    GestureDetectorCompat gestureDetectorCompat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_preference_activity);

        ButterKnife.bind(this);
        gestureDetectorCompat = new GestureDetectorCompat(this, new RightGestureListener(this));

        mHeightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void click(View view){
        Toast.makeText(this,mHeightSpinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
    }
}