package com.funfit.usjr.thesis.funfitv2.main;

import android.content.IntentFilter;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.MapsActivity;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.history.HistoryActivity;
import com.funfit.usjr.thesis.funfitv2.mealPlan.MealPlanActivity;
import com.funfit.usjr.thesis.funfitv2.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getSupportFragmentManager().beginTransaction().replace(R.id.container,new MealPlanActivity()).commit();
    }

    private void navigate(final int itemId) {
        switch (itemId) {
            case R.id.home:
//                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_layout, new MapsActivity()).commit();
                break;
            case R.id.nav_health_pref:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_layout, new MealPlanActivity()).commit();
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
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_layout, new HistoryActivity()).commit();
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
        return true;
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MapsActivity(), "Arena");
        adapter.addFragment(new MealPlanActivity(), "Shack");
        viewPager.setAdapter(adapter);
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