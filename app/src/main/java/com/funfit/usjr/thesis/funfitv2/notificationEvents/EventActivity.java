package com.funfit.usjr.thesis.funfitv2.notificationEvents;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.history.EventHistoryActivityImpl;
import com.funfit.usjr.thesis.funfitv2.model.Events;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 1/15/2016.
 */
public class EventActivity extends AppCompatActivity{
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_notification)
    RecyclerView mRecyclerView;

    protected RecyclerView.LayoutManager mLayoutManager;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected EventsAdapter mAdapter;


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Activity Setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        fetchEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                startActivity(new Intent(this, EventHistoryActivityImpl.class));
                return true;
            default:
                return false;
        }
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }


    //This method is currently for mock purposes
    private void fetchEvents() {
        ArrayList<Events> list = new ArrayList<Events>();

        list.add(new Events("runscapade","Ayala Grounds - Cebu Business Park","2,000 pts"
                ,"https://igcdn-photos-b-a.akamaihd.net/hphotos-ak-xap1/t51.2885-15/e35/12142218_1668259510111137_1609779316_n.jpg"));
        list.add(new Events("get hike","Osmeña Peak","2,500 pts"
                ,"https://igcdn-photos-h-a.akamaihd.net/hphotos-ak-xft1/t51.2885-15/e35/11374133_139099836427599_1190742139_n.jpg"));
        list.add(new Events("trackn'field","Cebu City Sports Center","1,800 pts"
                ,"https://igcdn-photos-c-a.akamaihd.net/hphotos-ak-xpa1/t51.2885-15/e35/11417444_590770674395930_220248231_n.jpg"));
        list.add(new Events("rise & shine","Cebu I.T. Park","2,000 pts"
                ,"https://igcdn-photos-e-a.akamaihd.net/hphotos-ak-xat1/t51.2885-15/e15/11357446_982947285082284_1328689597_n.jpg"));

        mAdapter = new EventsAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
    }
}
