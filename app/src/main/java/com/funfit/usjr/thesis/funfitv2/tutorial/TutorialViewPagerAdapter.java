package com.funfit.usjr.thesis.funfitv2.tutorial;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.funfit.usjr.thesis.funfitv2.R;

/**
 * Created by ocabafox on 1/15/2016.
 */
public class TutorialViewPagerAdapter extends FragmentPagerAdapter {
    String[] mFrogScientificNames;
    String[] mFrogGenera;
    String[] mFrogDescriptions;
    int size;

    public TutorialViewPagerAdapter(FragmentManager fm, Context context, int size) {
        super(fm);

        mFrogScientificNames = context.getResources().getStringArray(R.array.tutorial_names);
        mFrogGenera = context.getResources().getStringArray(R.array.tutorial_genera);
        mFrogDescriptions = context.getResources().getStringArray(R.array.tutorial_descriptions);
        this.size = size;
    }




    @Override
    public Fragment getItem(int i) {
        Bundle args = new Bundle();
        args.putString(TutorialFragment.TUTORIAL_NAME, mFrogScientificNames[i]);
        args.putString(TutorialFragment.TUTORIAL_DESCRIPTION, mFrogDescriptions[i]);
        args.putInt(TutorialFragment.TUTORIAL_IMAGE, adaptFrogImage(i));

        TutorialFragment tutorialFragment = new TutorialFragment();
        tutorialFragment.setArguments(args);

        return tutorialFragment;
    }




    private int adaptFrogImage(int i) {
        int frogImageId = 0;
        switch (i) {
            case 0:
                frogImageId = R.drawable.frog1;
                break;
            case 1:
                frogImageId = R.drawable.frog2;
                break;
            case 2:
                frogImageId = R.drawable.frog3;
                break;
            case 3:
                frogImageId = R.drawable.frog4;
                break;
            case 4:
                frogImageId = R.drawable.frog5;
                break;
            case 5:
                frogImageId = R.drawable.frog6;
                break;
            case 6:
                frogImageId = R.drawable.frog7;
                break;
            case 7:
                frogImageId = R.drawable.frog8;
                break;
        }
        return frogImageId;
    }

    @Override
    public int getCount() {
        return mFrogScientificNames.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFrogGenera[position];
    }
}
