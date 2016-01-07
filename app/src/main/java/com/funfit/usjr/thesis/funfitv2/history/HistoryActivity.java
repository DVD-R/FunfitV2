package com.funfit.usjr.thesis.funfitv2.history;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.history.StaticData.ItemData;
import com.funfit.usjr.thesis.funfitv2.history.StaticData.myAdapter;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;

/**
 * Created by ocabafox on 1/8/2016.
 */
public class HistoryActivity extends Fragment {

    View rootView;
    RecyclerView recyclerView;

    HistoryService mHistoryService;
    HistoryObject mHistoryObject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.history_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_history);

        ItemData itemsData[] = { new ItemData("Ricky Ocaba","1km run!","IT Park","January 1 2016"),
                new ItemData("Victor Andales","10km run!","City Hall","January 8 2015"),
                new ItemData("DJ the great","3km run!","Colon","December 6 2015"),
                new ItemData("Matzuo Malicay","1km run!","Fuente","November 30 2015"),
                new ItemData("Paul Ryan","3km run!","Basak Pardo","October 29 2015"),
                new ItemData("Triszy","9km run!","Pasil Cebu City","May 28 2015")};

        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        myAdapter mAdapter = new myAdapter(itemsData);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }
}
