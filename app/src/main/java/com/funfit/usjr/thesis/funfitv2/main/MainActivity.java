package com.funfit.usjr.thesis.funfitv2.main;

import android.content.IntentFilter;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.funfit.usjr.thesis.funfitv2.MapsActivity;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.history.HistoryActivity;
import com.funfit.usjr.thesis.funfitv2.mealPlan.MealPlanActivity;
import com.funfit.usjr.thesis.funfitv2.search.SearchActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)DrawerLayout mDrawerLayout;
    @Bind(R.id.toolbar)Toolbar mToolBar;
    @Bind(R.id.navigation)
    NavigationView navigationView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private final Handler mDrawerActionHandler = new Handler();
    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";
    private int mNavItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.open,
                R.string.close);


        if (null == savedInstanceState) {
            mNavItemId = R.id.home;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new MealPlanActivity()).commit();
    }

    private void navigate(final int itemId) {
        switch (itemId) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new MapsActivity()).commit();
                break;
            case R.id.nav_health_pref:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new MealPlanActivity()).commit();
                break;
            case R.id.nav_meal_archeive:
                break;
            case R.id.nav_food_description:
                break;
            case R.id.nav_challenge:
                break;
            case R.id.nav_weekly:
                break;
            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new HistoryActivity()).commit();
                break;
            case R.id.nav_event:
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
        return true;    }
}