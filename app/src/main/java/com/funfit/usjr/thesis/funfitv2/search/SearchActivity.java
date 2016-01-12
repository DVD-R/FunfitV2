package com.funfit.usjr.thesis.funfitv2.search;

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
import com.funfit.usjr.thesis.funfitv2.fatSecretImplementation.FatSecretPresenter;
import com.funfit.usjr.thesis.funfitv2.model.Food;
import com.funfit.usjr.thesis.funfitv2.searchFragment.MostEatenSearchFragment;
import com.funfit.usjr.thesis.funfitv2.searchFragment.RecentlyEatenSearchFragment;
import com.funfit.usjr.thesis.funfitv2.searchFragment.SearchFragment;
import com.funfit.usjr.thesis.funfitv2.views.ISearchView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements ISearchView{

    @Bind(R.id.toolbar)Toolbar mToolbar;
    @Bind(R.id.viewpager)ViewPager mViewPager;
    @Bind(R.id.tabs)TabLayout mTabs;
    private SearchView mSearchView;
    private FatSecretPresenter fatSecretPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        activitySetup();
        fatSecretPresenter = new FatSecretPresenter(this);
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
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);


        mTabs.setupWithViewPager(viewPager);
        mTabs.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabs.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void getFood(List<Food> items) {
        Intent intent = new Intent(this, SearchFragment.SearchService.class);
        intent.putExtra("FoodList", (Serializable) items);
        startService(intent);
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
                fatSecretPresenter.searchFoodQuery(newText);
                return true;
            }
        });
    }
}