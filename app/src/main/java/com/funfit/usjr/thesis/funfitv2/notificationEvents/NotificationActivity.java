package com.funfit.usjr.thesis.funfitv2.notificationEvents;

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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dj on 1/15/2016.
 */
public class NotificationActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_notification)
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

        list.add(new Notification(NOTIFICATION_ENEMY_ENVADE, "Timothy Estrada",
                "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/36507706_2112349905445852_5587791407449374720_n.jpg?_nc_cat=0&oh=9f122b4f5e3da9a61b1cda22fb62810a&oe=5BCFFD94", null));
        list.add(new Notification(NOTIFICATION_ALLY_FORTIFIED, "Shea Azarcón",
                "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/35331993_1873029966081472_6345356134462783488_n.jpg?_nc_cat=0&oh=fbf40f3b4bfe7d240d6e16ed04fbcdb9&oe=5C133A93", null));
        list.add(new Notification(NOTIFICATION_ALLY_FORTIFIED, "Kurt Estevon Suico",
                "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/37772651_10214966617296209_1688246073761464320_n.jpg?_nc_cat=0&oh=1291fa4c2ebc13250a80d5a5f079aaa1&oe=5C117E2D", null));
        list.add(new Notification(NOTIFICATION_ENEMY_ENVADE, "Karl Milo Pilapil",
                "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/35461859_10210162218406985_7532103366973849600_n.jpg?_nc_cat=0&oh=c54973c164980433186aae2d36fbce07&oe=5BD12B1C", null));
        list.add(new Notification(NOTIFICATION_ALLY_FORTIFIED, "Ricardo Javier",
                "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/17630078_10212235245337983_5815909305872402187_n.jpg?_nc_cat=0&oh=a9d98cd15f8ca45b39aed5f3eb43afe3&oe=5BD67F3E", null));

        mAdapter = new NotificationAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
    }
}