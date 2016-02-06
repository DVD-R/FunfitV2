package com.funfit.usjr.thesis.funfitv2.search;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.fatSecretImplementation.FatSecretPresenter;
import com.funfit.usjr.thesis.funfitv2.model.Food;
import com.funfit.usjr.thesis.funfitv2.model.FoodServing;
import com.funfit.usjr.thesis.funfitv2.searchFragment.MostEatenSearchFragment;
import com.funfit.usjr.thesis.funfitv2.searchFragment.RecentlyEatenSearchFragment;
import com.funfit.usjr.thesis.funfitv2.searchFragment.SearchFragment;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;
import com.funfit.usjr.thesis.funfitv2.views.ISearchView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements ISearchView{

    private SearchFragment searchFragment;
    @Bind(R.id.toolbar)Toolbar mToolbar;
    @Bind(R.id.viewpager)ViewPager mViewPager;
    @Bind(R.id.tabs)TabLayout mTabs;
    @Bind(R.id.carddemo_progressContainer)LinearLayout mProgressBarContainer;
    private Intent intent;
    private String mealTime;
    private SearchView mSearchView;
    private FatSecretPresenter fatSecretPresenter;
    private String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.getCluster(this).equals("impulse"))
            setTheme(R.style.ImpulseAppThemeLight);
        else
            setTheme(R.style.VelocityAppThemeLight);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        searchFragment = new SearchFragment();
        activitySetup();
        Intent intent = getIntent();
        mealTime = intent.getExtras().getString("MEALTIME");
    }

    private void activitySetup() {
        fatSecretPresenter = new FatSecretPresenter(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);


        if (mViewPager != null) {
            setupViewPager(mViewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(searchFragment, "Search");
        adapter.addFragment(new RecentlyEatenSearchFragment(), "Recently Eaten");
        adapter.addFragment(new MostEatenSearchFragment(), "Most Eaten");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);


        mTabs.setupWithViewPager(viewPager);
        mTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabs.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void getFood(List<Food> items) {
            (searchFragment).sendFoodList(items, mealTime);
    }

    public interface DisplayList{
        public void sendFoodList(List<Food> items, String mealTime);
    }

    @Override
    public void mProgressBarGone() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressBarContainer != null)
                    mProgressBarContainer.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void mProgressInit() {
        mProgressBarContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public String getNewText() {
        return query;
    }

    static class TabAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do whatever you need
                return true; // KEEP IT TO TRUE OR IT DOESN'T OPEN !!
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                onBackPressed();
               return false;
            }
        });

        mSearchView = (SearchView)
                MenuItemCompat.getActionView(searchItem);
        searchItem.expandActionView();
        doSearch();
        return super.onCreateOptionsMenu(menu);
    }

    public void doSearch() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                fatSecretPresenter.searchFoodQuery();
                return true;
            }
        });
    }
}