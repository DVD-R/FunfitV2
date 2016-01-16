package com.funfit.usjr.thesis.funfitv2.history;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.adapters.HistoryAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.MapView;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class EventHistoryActivity extends AppCompatActivity {

    @Bind(R.id.historyRecycler)RecyclerView mHistoryRecycler;
    @Bind(R.id.toolbar)Toolbar mToolbar;
    protected HistoryAdapter mHistoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_history);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Event History");
        configureColumn();
    }

    protected void configureColumn(){
        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mHistoryRecycler.setLayoutManager(staggeredGridLayoutManager);

        mHistoryAdapter = createHistoryListAdapter();
    }

    protected abstract HistoryAdapter createHistoryListAdapter();

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        if (mHistoryAdapter != null){
            for (MapView m: mHistoryAdapter.getmMapViews()){
                m.onLowMemory();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHistoryAdapter != null){
            for (MapView m: mHistoryAdapter.getmMapViews()){
                m.onPause();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (resultCode == ConnectionResult.SUCCESS){
            mHistoryRecycler.setAdapter(mHistoryAdapter);
        }else {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1).show();
        }

        if (mHistoryAdapter != null){
            for (MapView m: mHistoryAdapter.getmMapViews()){
                m.onResume();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHistoryAdapter != null){
            for (MapView m: mHistoryAdapter.getmMapViews()){
                m.onDestroy();
            }
        }
    }

    /**
     * Show a full mapView when a mapView card is selected. This method is attached to each CardView
     * displayed within this activity's RecyclerView.
     *
     * @param view The view (CardView) that was clicked.
     */

    public abstract void showMapDetails(View view);
}
