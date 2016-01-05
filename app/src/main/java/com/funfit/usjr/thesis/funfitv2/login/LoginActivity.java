package com.funfit.usjr.thesis.funfitv2.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.healthPreference.HealthPreferenceActivity;
import com.funfit.usjr.thesis.funfitv2.main.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.facebookBtn)
    public void loginFacebook(){
        Intent intent = new Intent(this, HealthPreferenceActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Toast.makeText(this,"CLick",Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.googleBtn)
    public void loginGoogle(){
        Intent intent = new Intent(this, HealthPreferenceActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}