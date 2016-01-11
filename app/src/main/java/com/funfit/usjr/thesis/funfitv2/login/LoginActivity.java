package com.funfit.usjr.thesis.funfitv2.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.healthPreference.HealthPreferenceActivity;
import com.funfit.usjr.thesis.funfitv2.main.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.text_funfit)
    TextView mTextFunfit;

    @Bind(R.id.img_login_bg)ImageView mImageBg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Glide.with(this)
                .load(Uri.parse("http://djunabel.com/images/pics/554_djuna-bel-nike-db8.jpg"))
                .centerCrop()
                .into(mImageBg);
        mTextFunfit.setTypeface(Typeface.createFromAsset(getAssets(), "HelveticaBold.otf"));
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