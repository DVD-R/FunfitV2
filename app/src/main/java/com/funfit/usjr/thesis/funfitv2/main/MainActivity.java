package com.funfit.usjr.thesis.funfitv2.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.funfit.usjr.thesis.funfitv2.FunfitApplication;
import com.funfit.usjr.thesis.funfitv2.login.LoginActivity;
import com.funfit.usjr.thesis.funfitv2.maps.MapsFragment;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.leaderBoard.LeaderBoardActivity;
import com.funfit.usjr.thesis.funfitv2.mealPlan.MealPlanFragment;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.model.ProfileRequestJson;
import com.funfit.usjr.thesis.funfitv2.model.Territory;
import com.funfit.usjr.thesis.funfitv2.notificationEvents.EventActivity;
import com.funfit.usjr.thesis.funfitv2.notificationEvents.NotificationActivity;
import com.funfit.usjr.thesis.funfitv2.services.CreatePolylineService;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;
import com.funfit.usjr.thesis.funfitv2.views.IMainView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IMainView {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.main_pager)
    ViewPager mMainPager;
    @Bind(R.id.navigation)
    NavigationView navigationView;

    @Bind(R.id.nav_layout)
    RelativeLayout mNavLayout;
    @Bind(R.id.txt_username)
    TextView mTextUsername;
    @Bind(R.id.txt_level)
    TextView mTextLevel;
    @Bind(R.id.img_cluster)
    ImageView mImageCluster;
    @Bind(R.id.img_profile)
    ImageView mImageProfile;
    private Activity activity;
    private SharedPreferences userData, rdi;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private final Handler mDrawerActionHandler = new Handler();
    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";
    private int mNavItemId;
    private MainPresenter mainPresenter;
    private List<Territory> listTerritory;
    private ProgressDialog mProgressDialog;
    private ProfileRequestJson profileRequestJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.getCluster(this).equals("impulse"))
            setTheme(R.style.ImpulseAppTheme);
        else
            setTheme(R.style.VelocityAppTheme);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.open,
                R.string.close);

        setupViewPager(mMainPager);
        mTabLayout.setupWithViewPager(mMainPager);

        if (null == savedInstanceState) {
            mNavItemId = R.id.home;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        mainPresenter = new MainPresenter(this);
        userData = getSharedPreferences(Constants.USER_PREF_ID, MODE_PRIVATE);
        rdi = getSharedPreferences(Constants.RDI_PREF_ID, MODE_PRIVATE);
        activity = MainActivity.this;
        setNavViews();
    }

    private void setNavViews() {

        String cluster = userData.getString(Constants.PROFILE_CLUSTER, null);

        if (cluster.equals("impulse")) {
            mNavLayout.setBackground(getResources().getDrawable(R.drawable.nav_header_impulse));
            mImageCluster.setImageResource(R.drawable.up_impulse);
        }
        else {
            mNavLayout.setBackground(getResources().getDrawable(R.drawable.nav_header_velocity));
            mImageCluster.setImageResource(R.drawable.up_velocity);
        }
        mTextUsername.setText(userData.getString(Constants.PROFILE_FNAME, null));
        Glide.with(this).load(userData.getString(Constants.PROFILE_IMG_URL, null))
                .centerCrop().crossFade().into(mImageProfile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                return true;
            case R.id.action_news:
                startActivity(new Intent(this, EventActivity.class));
                return true;
            default:
                Log.v("HEY", item.getItemId() + "");
                return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//            mainPresenter.setProfileRequestJson();
            mainPresenter.onResume();
    }

    private void navigate(final int itemId) {
        Intent i;
        switch (itemId) {
            case R.id.nav_arena:
                mMainPager.setCurrentItem(0);
                break;
            case R.id.nav_shack:
                mMainPager.setCurrentItem(1);
                break;
            case R.id.nav_leaderBoard:
                i = new Intent(this, LeaderBoardActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            case R.id.nav_event:
                startActivity(new Intent(this, EventActivity.class));
                break;
            case R.id.nav_logout:
                ((FunfitApplication) getApplicationContext()).logout();
                i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                getSharedPreferences(Constants.USER_PREF_ID, Context.MODE_PRIVATE).edit().clear();
                startActivity(i);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
                mDrawerLayout.closeDrawers();
            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MapsFragment(), "Arena");
        adapter.addFragment(new MealPlanFragment(), "Shack");
        viewPager.setAdapter(adapter);
    }

    @Override
    public String getHeight() {
        String [] height = userData.getString(Constants.PROFILE_HEIGHT, null).split(" ");
        return  height[0].toString();
    }

    @Override
    public String getWeight() {
        String [] weight = userData.getString(Constants.PROFILE_WEIGHT, null).split(" ");
        return weight[0].toString();
    }

    @Override
    public String getActivityLevel() {
        return userData.getString(Constants.PROFILE_ACTIVITY_LEVEL, null);
    }

    @Override
    public String getFirstName() {
        return userData.getString(Constants.PROFILE_FNAME, null);
    }

    @Override
    public String getLastName() {
        return userData.getString(Constants.PROFILE_LNAME, null);
    }

    @Override
    public int getAge() {
        return mainPresenter.calculateAge(userData.getString(Constants.PROFILE_DOB, null));
    }

    @Override
    public String getGender() {
        return userData.getString(Constants.PROFILE_GENDER, null);
    }

    @Override
    public String getEmail() {
        return userData.getString(Constants.PROFILE_EMAIL, null);
    }

    @Override
    public String getfactionDescription() {
        return userData.getString(Constants.PROFILE_CLUSTER, null);
    }

    @Override
    public String getGcmKey() {
        return userData.getString(Constants.GCM_KEY, null);
    }

    @Override
    public Context getContxt() {
        return activity;
    }

    @Override
    public void setProfileRequestJson(ProfileRequestJson profileRequestJson) {
        this.profileRequestJson = profileRequestJson;
    }

    @Override
    public ProfileRequestJson getProfileRequestJson() {
        return profileRequestJson;
    }

    @Override
    public void sendTerritory() {
        Intent i = new Intent(this, CreatePolylineService.class);
        i.putExtra("RESPONSETERRITORY", (Serializable) this.listTerritory);
        startService(i);
    }

    @Override
    public void setEndcodedPolylineList(List<Territory> listTerritory) {
        this.listTerritory = listTerritory;
        Log.i("Testing notification: ", listTerritory.get(0).getEncoded_polyline());
    }

    @Override
    public void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Processing...");
        mProgressDialog.setMessage("Application Components");
        mProgressDialog.setCancelable(false);
        this.mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        this.mProgressDialog.hide();
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}