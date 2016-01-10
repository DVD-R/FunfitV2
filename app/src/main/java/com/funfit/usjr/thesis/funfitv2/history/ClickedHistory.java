package com.funfit.usjr.thesis.funfitv2.history;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.R;

/**
 * Created by ocabafox on 1/11/2016.
 */
public class ClickedHistory extends AppCompatActivity {

    String getUser,getEvent,getArea,getTimedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clicked_history);

        Intent intent = getIntent();
        getUser = intent.getStringExtra("user");
        getEvent = intent.getStringExtra("event");
        getArea = intent.getStringExtra("area");
        getTimedate = intent.getStringExtra("timeDate");

        Toast.makeText(ClickedHistory.this, "1. "+getUser, Toast.LENGTH_SHORT).show();
        Toast.makeText(ClickedHistory.this, "2. "+getEvent, Toast.LENGTH_SHORT).show();
        Toast.makeText(ClickedHistory.this, "3. "+getArea, Toast.LENGTH_SHORT).show();
        Toast.makeText(ClickedHistory.this, "4. "+getTimedate, Toast.LENGTH_SHORT).show();
    }
}
