package com.funfit.usjr.thesis.funfitv2.tutorial;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.healthPreference.HealthStatisticsSetupPager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ocabafox on 1/18/2016.
 */
public class TutorialActivity extends AppCompatActivity {
    @Bind(R.id.pager)
    ViewPager mViewPager;
    TutorialViewPagerAdapter mTutorialPagerAdapter;
    AlertDialog alertDialog;
    final private static int DIALOG_POP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity);
        ButterKnife.bind(this);
        checkFirstRun();
    }

    //This is for Tutorial
    public void checkFirstRun() {
        /*return true if this is your first run*/
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {
            /*display dialogbox*/
            showDialog(DIALOG_POP);
            /*return false after identified the first run*/
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", true)
                    .apply();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog dialogDetails = null;
        /*initialize costom dialog with xml*/
        switch (id) {
            case DIALOG_POP:
                LayoutInflater inflater = LayoutInflater.from(this);
                View dialogView = inflater.inflate(R.layout.dialog_layout, null);

                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
                dialogbuilder.setTitle("Is this your first time?");
                dialogbuilder.setView(dialogView);
                dialogDetails = dialogbuilder.create();

                break;
        }
        return dialogDetails;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DIALOG_POP:
                alertDialog = (AlertDialog) dialog;
                Button yesbutton = (Button) alertDialog.findViewById(R.id.btn_yes);
                Button nobutton = (Button) alertDialog.findViewById(R.id.btn_no);
                final String[] getSizee = alertDialog.getContext().getResources().getStringArray(R.array.tutorial_names);

                yesbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*click ok to intent help_activity*/
                        mTutorialPagerAdapter = new TutorialViewPagerAdapter(getSupportFragmentManager(), v.getContext(), getSizee.length);
                        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                        mViewPager.setAdapter(mTutorialPagerAdapter);
//                        Toast.makeText(HealthStatisticsSetupPager.this, "Test", Toast.LENGTH_SHORT).show();

                        /*close dialogbox*/
                        alertDialog.dismiss();
                    }
                });

                nobutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), HealthStatisticsSetupPager.class);
                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        /*close dialogbox*/
                        alertDialog.dismiss();
                    }
                });
                break;
        }
    }
}
