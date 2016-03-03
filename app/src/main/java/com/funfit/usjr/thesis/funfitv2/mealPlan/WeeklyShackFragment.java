package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.adapter.ViewPagerAdapter;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 3/3/2016.
 */
public class WeeklyShackFragment extends Fragment{
    @Bind(R.id.shackPager)
    ViewPager mShackPager;
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.fab_switch)
    FloatingActionButton mFabSwitch;

    private SharedPreferences datePref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shack, container, false);
        ButterKnife.bind(this, v);
        datePref = getActivity().getSharedPreferences(Constants.DATE_PREF_ID, getActivity().MODE_PRIVATE);

        setupViewPager(mShackPager);
        mTabLayout.setupWithViewPager(mShackPager);

        tabToday();
        setWeeklyPref();

        return v;
    }

    private void tabToday() {
        switch (Utils.getCurrentDayOfWeek()) {
            case Calendar.SUNDAY: mShackPager.setCurrentItem(0);break;
            case Calendar.MONDAY: mShackPager.setCurrentItem(1);break;
            case Calendar.TUESDAY: mShackPager.setCurrentItem(2);break;
            case Calendar.WEDNESDAY: mShackPager.setCurrentItem(3);break;
            case Calendar.THURSDAY: mShackPager.setCurrentItem(4);break;
            case Calendar.FRIDAY: mShackPager.setCurrentItem(5);break;
            case Calendar.SATURDAY: mShackPager.setCurrentItem(6);break;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MealPlanFragment(), "S");
        adapter.addFragment(new MealPlanFragment(), "M");
        adapter.addFragment(new MealPlanFragment(), "T");
        adapter.addFragment(new MealPlanFragment(), "W");
        adapter.addFragment(new MealPlanFragment(), "T");
        adapter.addFragment(new MealPlanFragment(), "F");
        adapter.addFragment(new MealPlanFragment(), "S");
        viewPager.setAdapter(adapter);
    }

    public void setWeeklyPref(){
        String dayDate="";

        switch (mShackPager.getCurrentItem()) {
            case Calendar.SUNDAY+1: dayDate = "Sunday"; break;
            case Calendar.MONDAY+1: dayDate = "Monday"; break;
            case Calendar.TUESDAY+1: dayDate = "Tuesday"; break;
            case Calendar.WEDNESDAY+1: dayDate = "Wednesday"; break;
            case Calendar.THURSDAY+1: dayDate = "Thursday"; break;
            case Calendar.FRIDAY+1: dayDate = "Friday"; break;
            case Calendar.SATURDAY+1: dayDate = "Saturday"; break;
        }

        datePref.edit().putString(Constants.DAY_DATE, dayDate).commit();
        datePref.edit().putString(Constants.WEEK_MONTH, "Weekly").commit();
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
