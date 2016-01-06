package com.funfit.usjr.thesis.funfitv2.search;

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
import android.view.Menu;
import android.view.MenuItem;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.searchFragment.MostEatenSearchFragment;
import com.funfit.usjr.thesis.funfitv2.searchFragment.QuickPickSearchFragment;
import com.funfit.usjr.thesis.funfitv2.searchFragment.RecentlyEatenSearchFragment;
import com.funfit.usjr.thesis.funfitv2.searchFragment.SavedMealSearchFragment;
import com.funfit.usjr.thesis.funfitv2.searchFragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)Toolbar mToolbar;
    @Bind(R.id.viewpager)ViewPager mViewPager;
    @Bind(R.id.tabs)TabLayout mTabs;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        activitySetup();
    }

    private void activitySetup() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);


        if (mViewPager != null) {
            setupViewPager(mViewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchFragment(), "Search");
        adapter.addFragment(new RecentlyEatenSearchFragment(), "Recently Eaten");
        adapter.addFragment(new MostEatenSearchFragment(), "Most Eaten");
        adapter.addFragment(new SavedMealSearchFragment(), "Save Meal");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);


        mTabs.setupWithViewPager(viewPager);
//        tabs.getTabAt(0).setIcon(R.drawable.ic_home);
//        tabs.getTabAt(1).setIcon(R.drawable.ic_fav);
//        tabs.getTabAt(2).setIcon(R.drawable.ic_search);
        mTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabs.setTabMode(TabLayout.MODE_FIXED);
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

//                Intent intent = new Intent(BROADCAST_SEARCH_VISIBLE);
//                sendBroadcast(intent);
                return false; // OR FALSE IF YOU DIDN'T WANT IT TO CLOSE!
            }
        });

        mSearchView = (SearchView)
                MenuItemCompat.getActionView(searchItem);
        searchItem.expandActionView();
        return super.onCreateOptionsMenu(menu);

    }
}
