package com.funfit.usjr.thesis.funfitv2.leaderBoard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.funfit.usjr.thesis.funfitv2.FunfitApplication;
import com.funfit.usjr.thesis.funfitv2.R;
import com.funfit.usjr.thesis.funfitv2.login.LoginActivity;
import com.funfit.usjr.thesis.funfitv2.main.MainActivity;
import com.funfit.usjr.thesis.funfitv2.model.Constants;
import com.funfit.usjr.thesis.funfitv2.notificationEvents.EventActivity;
import com.funfit.usjr.thesis.funfitv2.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ocabafox on 1/20/2016.
 */
public class LeaderBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.recycler_leaderboard)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.navigation)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.nav_layout)
    RelativeLayout mNavLayout;
    @Bind(R.id.txt_username)
    TextView mTextUsername;
    @Bind(R.id.txt_level)
    TextView mTextLevel;
    @Bind(R.id.img_cluster)
    ImageView mImageCluster;
    @Bind(R.id.img_profile)
    ImageView mImageProfile;

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private final Handler mDrawerActionHandler = new Handler();
    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";
    private int mNavItemId;
    ItemData itemsData[];
    FilterData filterData[];
    LeaderBoardAdapter mAdapter;
    FilterLeaderBoardAdapter filterLeaderBoardAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.getCluster(this).equals("impulse"))
            setTheme(R.style.ImpulseAppTheme);
        else
            setTheme(R.style.VelocityAppTheme);
        setContentView(R.layout.activity_leaderboard);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        navigationView.setNavigationItemSelectedListener(this);
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
        itemsData = new ItemData[]{new ItemData("Usain Bolt", "http://www.themediaink.com/wp-content/uploads/2015/11/Usain_Bolt.jpg", "201212", 1),
                new ItemData("Tyson Gay", "http://media.aws.iaaf.org/media/LargeL/42652375-b281-4894-b5e6-859f031e5fa2.jpg?v=639806527", "120115", 0),
                new ItemData("Gregg", "https://lh3.googleusercontent.com/-Hat_xpwgTc4/VUlKcIdUUvI/AAAAAAAAHys/d3Dyv_w9Rf0/w506-h750/11008457_641902699286482_5452833823138190646_n.jpg", "74258", 0),
                new ItemData("Osama Bin Laden", "https://i.guim.co.uk/img/static/sys-images/Guardian/Pix/pictures/2015/5/11/1431358410754/Osama-bin-Laden-008.jpg?w=460&q=85&auto=format&sharp=10&s=a2cc0f3ccef1c6fa0bee016469b89c1e", "112008", 0),
                new ItemData("Binay", "http://newsinfo.inquirer.net/files/2014/05/Binay.jpg", "91230", 1),
                new ItemData("Eminem", "https://consequenceofsound.files.wordpress.com/2013/10/eminem.jpg", "90230", 1),
                new ItemData("Jordan", "http://www.bullsnation.net/wp-content/uploads/2014/12/uspw_5156722_crop_north.jpg", "88230", 1),
                new ItemData("Boy Abonda", "http://www.mb.com.ph/wp-content/uploads/2014/12/boy-abundaz.jpg", "87230", 1),
                new ItemData("Gon", "http://i2.kym-cdn.com/photos/images/facebook/000/787/356/d6f.jpg", "84230", 1),
                new ItemData("May Weather", "http://www.mayweathervsbertolivestreamon.com/wp-content/uploads/2015/09/floyd-mayweather.jpg", "70258", 0),
                new ItemData("Wayne", "http://cebudailynews.inquirer.net/files/2015/10/page1b.jpg", "68035", 0)};



        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new LeaderBoardAdapter(itemsData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        setNavViews();
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

    private void setNavViews() {
        SharedPreferences userData =
                getSharedPreferences(Constants.USER_PREF_ID, MODE_PRIVATE);
        String cluster = userData.getString(Constants.PROFILE_CLUSTER, null);

        if (cluster.equals("impulse")) {
            mNavLayout.setBackground(getResources().getDrawable(R.drawable.nav_header_impulse));
            mImageCluster.setImageResource(R.drawable.up_impulse);
        }
        else {
            mNavLayout.setBackground(getResources().getDrawable(R.drawable.nav_header_velocity));
            mImageCluster.setImageResource(R.drawable.up_velocity);
        }
        mTextUsername.setText(userData.getString(Constants.PROFILE_FNAME, null));
        Glide.with(this).load(userData.getString(Constants.PROFILE_IMG_URL, null))
                .centerCrop().crossFade().into(mImageProfile);
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
            case R.id.nav_logout:
                ((FunfitApplication) getApplicationContext()).logout();
                i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                getSharedPreferences(Constants.USER_PREF_ID, Context.MODE_PRIVATE).edit().clear();
                startActivity(i);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_leader_board, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.world:
                Toast.makeText(getBaseContext(), "You selected World", Toast.LENGTH_SHORT).show();

                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                mAdapter = new LeaderBoardAdapter(itemsData);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                break;

            case R.id.impulse:
                int count = 0;
                Toast.makeText(getBaseContext(), "You selected Impulse", Toast.LENGTH_SHORT).show();
                filterData = new FilterData[]{
                        new FilterData("Tyson Gay", "http://media.aws.iaaf.org/media/LargeL/42652375-b281-4894-b5e6-859f031e5fa2.jpg?v=639806527", "120115", 0),
                        new FilterData("Gregg", "https://lh3.googleusercontent.com/-Hat_xpwgTc4/VUlKcIdUUvI/AAAAAAAAHys/d3Dyv_w9Rf0/w506-h750/11008457_641902699286482_5452833823138190646_n.jpg", "74258", 0),
                        new FilterData("Osama Bin Laden", "https://i.guim.co.uk/img/static/sys-images/Guardian/Pix/pictures/2015/5/11/1431358410754/Osama-bin-Laden-008.jpg?w=460&q=85&auto=format&sharp=10&s=a2cc0f3ccef1c6fa0bee016469b89c1e", "112008", 0),
                        new FilterData("May Weather", "http://www.mayweathervsbertolivestreamon.com/wp-content/uploads/2015/09/floyd-mayweather.jpg", "70258", 0),
                        new FilterData("Wayne", "http://cebudailynews.inquirer.net/files/2015/10/page1b.jpg", "68035", 0)};

                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                filterLeaderBoardAdapter = new FilterLeaderBoardAdapter(filterData);
                mRecyclerView.setAdapter(filterLeaderBoardAdapter);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                break;

            case R.id.velocity:
                Toast.makeText(getBaseContext(), "You selected Velocity", Toast.LENGTH_SHORT).show();

                filterData = new FilterData[]{new FilterData("Usain Bolt", "http://www.themediaink.com/wp-content/uploads/2015/11/Usain_Bolt.jpg", "201212", 1),
                        new FilterData("Binay", "http://newsinfo.inquirer.net/files/2014/05/Binay.jpg", "91230", 1),
                        new FilterData("Eminem", "https://consequenceofsound.files.wordpress.com/2013/10/eminem.jpg", "90230", 1),
                        new FilterData("Jordan", "http://www.bullsnation.net/wp-content/uploads/2014/12/uspw_5156722_crop_north.jpg", "88230", 1),
                        new FilterData("Boy Abonda", "http://www.mb.com.ph/wp-content/uploads/2014/12/boy-abundaz.jpg", "87230", 1),
                        new FilterData("Gon", "http://i2.kym-cdn.com/photos/images/facebook/000/787/356/d6f.jpg", "84230", 1)};
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                filterLeaderBoardAdapter = new FilterLeaderBoardAdapter(filterData);
                mRecyclerView.setAdapter(filterLeaderBoardAdapter);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                break;
        }
        return true;

    }
}
