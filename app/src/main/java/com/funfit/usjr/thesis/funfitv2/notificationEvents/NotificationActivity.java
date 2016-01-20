package com.funfit.usjr.thesis.funfitv2.notificationEvents;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.model.Notification;
import com.funfit.usjr.thesis.funfitv2.viewmods.DividerItemDecoration;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dj on 1/15/2016.
 */
public class NotificationActivity extends AppCompatActivity{
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_notification)
    RecyclerView mRecyclerView;

    //Notification types
    private static final int NOTIFICATION_ENEMY_ENVADE = 0;
    private static final int NOTIFICATION_ALLY_FORTIFIED = 1;

    protected RecyclerView.LayoutManager mLayoutManager;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected NotificationAdapter mAdapter;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Activity Setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        fetchNotifications();
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
    private void fetchNotifications() {
        ArrayList<Notification> list = new ArrayList<Notification>();

        list.add(new Notification(NOTIFICATION_ENEMY_ENVADE,"ViviDub",
                "http://36.media.tumblr.com/6f6ee1b0efdcd68b0b42b3926b1813a8/tumblr_inline_ntoq4xTqIk1rzdmz3_400.jpg", null));
        list.add(new Notification(NOTIFICATION_ALLY_FORTIFIED,"MightyElmo",
                "http://data.whicdn.com/images/1274695/large.jpg", null));
        list.add(new Notification(NOTIFICATION_ALLY_FORTIFIED,"Sammmmmy",
                "http://41.media.tumblr.com/tumblr_m8d2cfhiG11rd0j11o1_r1_1280.jpg", null));
        list.add(new Notification(NOTIFICATION_ENEMY_ENVADE,"FatCat",
                "https://33.media.tumblr.com/a277c5fc90d5ec52b3b0122769ba9fed/tumblr_inline_n1ve4lGOBw1reu5mf.jpg", null));
        list.add(new Notification(NOTIFICATION_ALLY_FORTIFIED,"WowDoge",
                "http://i.imgur.com/z5k5ykt.jpg", null));

        mAdapter = new NotificationAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
    }
}