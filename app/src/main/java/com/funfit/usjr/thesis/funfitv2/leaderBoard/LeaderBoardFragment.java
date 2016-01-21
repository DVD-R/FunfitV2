package com.funfit.usjr.thesis.funfitv2.leaderBoard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funfit.usjr.thesis.funfitv2.R;

import butterknife.ButterKnife;

/**
 * Created by ocabafox on 1/20/2016.
 */
public class LeaderBoardFragment extends Fragment {

    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.leaderboard_layout, container, false);
        ButterKnife.bind(this, rootView);

        new LoadAsyntask().execute();

        return rootView;
    }

    class LoadAsyntask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }
}
