package com.funfit.usjr.thesis.funfitv2.leaderBoard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.main.MainActivity;
import com.funfit.usjr.thesis.funfitv2.notificationEvents.EventActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ocabafox on 1/20/2016.
 */
public class LeaderBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.recycler_leaderboard)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.navigation)
    NavigationView mNavigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private final Handler mDrawerActionHandler = new Handler();
    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";
    private int mNavItemId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open,
                R.string.close);

        if (null == savedInstanceState) {
            mNavItemId = R.id.home;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        ItemData itemsData[] = {new ItemData("Usain Bolt", "http://www.themediaink.com/wp-content/uploads/2015/11/Usain_Bolt.jpg", "201212", 1),
                new ItemData("Tyson Gay", "http://media.aws.iaaf.org/media/LargeL/42652375-b281-4894-b5e6-859f031e5fa2.jpg?v=639806527", "120115", 0),
                new ItemData("Osama Bin Laden", "https://i.guim.co.uk/img/static/sys-images/Guardian/Pix/pictures/2015/5/11/1431358410754/Osama-bin-Laden-008.jpg?w=460&q=85&auto=format&sharp=10&s=a2cc0f3ccef1c6fa0bee016469b89c1e", "112008", 0),
                new ItemData("Binay", "http://newsinfo.inquirer.net/files/2014/05/Binay.jpg", "91230", 1),
                new ItemData("Eminem", "https://consequenceofsound.files.wordpress.com/2013/10/eminem.jpg", "90230", 1),
                new ItemData("Jordan", "http://www.bullsnation.net/wp-content/uploads/2014/12/uspw_5156722_crop_north.jpg", "88230", 1),
                new ItemData("Boy Abonda", "http://www.mb.com.ph/wp-content/uploads/2014/12/boy-abundaz.jpg", "87230", 1),
                new ItemData("Gon", "http://i2.kym-cdn.com/photos/images/facebook/000/787/356/d6f.jpg", "84230", 1),
                new ItemData("May Weather", "http://www.mayweathervsbertolivestreamon.com/wp-content/uploads/2015/09/floyd-mayweather.jpg", "70258", 0),
                new ItemData("Gregg", "https://lh3.googleusercontent.com/-Hat_xpwgTc4/VUlKcIdUUvI/AAAAAAAAHys/d3Dyv_w9Rf0/w506-h750/11008457_641902699286482_5452833823138190646_n.jpg", "74258", 0),
                new ItemData("Wayne", "http://cebudailynews.inquirer.net/files/2015/10/page1b.jpg", "68035", 0)};


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        LeaderBoardAdapter mAdapter = new LeaderBoardAdapter(itemsData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
                mDrawerLayout.closeDrawers();
            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    private void navigate(final int itemId) {
        Intent i;
        switch (itemId) {
            case R.id.nav_arena:
                i = new Intent(this, MainActivity.class);
                i.putExtra("TAB_ID", 0);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.nav_shack:
                i = new Intent(this, MainActivity.class);
                i.putExtra("TAB_ID", 1);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.nav_leaderBoard:
                break;
            case R.id.nav_event:
                startActivity(new Intent(this, EventActivity.class));
                break;
        }
    }

//    class LoadAsyntask extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            return null;
//        }
//    }
}
