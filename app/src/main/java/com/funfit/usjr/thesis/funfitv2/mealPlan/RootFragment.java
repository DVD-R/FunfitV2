package com.funfit.usjr.thesis.funfitv2.mealPlan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.funfit.usjr.thesis.funfitv2.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 3/4/2016.
 */
public class RootFragment extends Fragment{
    @Bind(R.id.root_frame)
    FrameLayout mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.root_fragment, container, false);
        ButterKnife.bind(this, rootView);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.root_frame, new MealPlanFragment());

        transaction.commit();

        return rootView;
    }
}
