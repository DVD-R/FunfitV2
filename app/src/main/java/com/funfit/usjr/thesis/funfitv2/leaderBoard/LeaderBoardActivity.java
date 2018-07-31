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
import android.view.View;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ocabafox on 1/20/2016.
 */
public class LeaderBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recycler_leaderboard)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    HeaderViewHolder mHeaderViewHolder;

    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private final Handler mDrawerActionHandler = new Handler();
    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";
    private int mNavItemId;
    ItemData itemsData[];
    FilterData filterData[];
    LeaderBoardAdapter mAdapter;
    FilterLeaderBoardAdapter filterLeaderBoardAdapter;

    protected static class HeaderViewHolder {

        @BindView(R.id.nav_layout)
        RelativeLayout mNavLayout;
        @BindView(R.id.txt_username)
        TextView mTextUsername;
        @BindView(R.id.txt_level)
        TextView mTextLevel;
        @BindView(R.id.img_cluster)
        ImageView mImageCluster;
        @BindView(R.id.img_profile)
        ImageView mImageProfile;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.getCluster(this).equals("impulse"))
            setTheme(R.style.ImpulseAppTheme);
        else
            setTheme(R.style.VelocityAppTheme);
        setContentView(R.layout.activity_leaderboard);
        ButterKnife.bind(this);
        View header = navigationView.getHeaderView(0);
        mHeaderViewHolder = new HeaderViewHolder(header);

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
        itemsData = new ItemData[]{new ItemData("Shea Azarcón", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/35331993_1873029966081472_6345356134462783488_n.jpg?_nc_cat=0&oh=fbf40f3b4bfe7d240d6e16ed04fbcdb9&oe=5C133A93", "201212", 1),
                new ItemData("Karl Milo Pilapil", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/35461859_10210162218406985_7532103366973849600_n.jpg?_nc_cat=0&oh=c54973c164980433186aae2d36fbce07&oe=5BD12B1C", "120115", 0),
                new ItemData("Timothy Estrada", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/36507706_2112349905445852_5587791407449374720_n.jpg?_nc_cat=0&oh=9f122b4f5e3da9a61b1cda22fb62810a&oe=5BCFFD94", "74258", 0),
                new ItemData("Janet Mención", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/37381208_10209420723273941_4641099921565417472_n.jpg?_nc_cat=0&oh=15ee66e64cb845eacc3fa8aec474e5e8&oe=5BCEA340", "112008", 0),
                new ItemData("Natasha Lansangan", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/38005627_1874541502637485_6362412849754537984_n.jpg?_nc_cat=0&oh=2349cb75007bd1469b9e3cdea984956b&oe=5C05C5B1", "91230", 1),
                new ItemData("Darren Zayne Rioja", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/32392071_1777578212264600_4825582021197692928_n.jpg?_nc_cat=0&oh=7404e6b9315e7f9624c5b190d2805f26&oe=5BC92C15", "90230", 1),
                new ItemData("Stephan Español", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/32643274_1909703092373937_1759311178166697984_n.jpg?_nc_cat=0&oh=aee5c53ad4b569dc1caee6b981172807&oe=5C122E29", "88230", 1),
                new ItemData("Ricardo Javier", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/17630078_10212235245337983_5815909305872402187_n.jpg?_nc_cat=0&oh=a9d98cd15f8ca45b39aed5f3eb43afe3&oe=5BD67F3E", "87230", 1),
                new ItemData("Kurt Estevon Suico", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/37772651_10214966617296209_1688246073761464320_n.jpg?_nc_cat=0&oh=1291fa4c2ebc13250a80d5a5f079aaa1&oe=5C117E2D", "84230", 1),
                new ItemData("Karmen Gonzaga", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/36755255_10205004694113744_5038489945266716672_n.jpg?_nc_cat=0&oh=4c29535f8ea85a606dac7cd952f74111&oe=5BC58C6F", "70258", 0),
                new ItemData("Brielle Degayo", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/34984630_10209891860328226_492009420247531520_n.jpg?_nc_cat=0&oh=82656e58c82639708c3bacf2bd37ac5b&oe=5C0D5193", "68035", 0)};


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
            mHeaderViewHolder.mNavLayout.setBackground(getResources().getDrawable(R.drawable.nav_header_impulse));
            mHeaderViewHolder.mImageCluster.setImageResource(R.drawable.up_impulse);
        } else {
            mHeaderViewHolder.mNavLayout.setBackground(getResources().getDrawable(R.drawable.nav_header_velocity));
            mHeaderViewHolder.mImageCluster.setImageResource(R.drawable.up_velocity);
        }
        mHeaderViewHolder.mTextUsername.setText(userData.getString(Constants.PROFILE_FNAME, null));
        Glide.with(this).load(userData.getString(Constants.PROFILE_IMG_URL, null))
                .centerCrop().crossFade().into(mHeaderViewHolder.mImageProfile);
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
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

        switch (item.getItemId()) {
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
                        new FilterData("Shea Azarcón", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/35331993_1873029966081472_6345356134462783488_n.jpg?_nc_cat=0&oh=fbf40f3b4bfe7d240d6e16ed04fbcdb9&oe=5C133A93", "120115", 0),
                        new FilterData("Karl Milo Pilapil", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/35461859_10210162218406985_7532103366973849600_n.jpg?_nc_cat=0&oh=c54973c164980433186aae2d36fbce07&oe=5BD12B1C", "74258", 0),
                        new FilterData("Timothy Estrada", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/36507706_2112349905445852_5587791407449374720_n.jpg?_nc_cat=0&oh=9f122b4f5e3da9a61b1cda22fb62810a&oe=5BCFFD94", "112008", 0),
                        new FilterData("Janet Mención", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/37381208_10209420723273941_4641099921565417472_n.jpg?_nc_cat=0&oh=15ee66e64cb845eacc3fa8aec474e5e8&oe=5BCEA340", "70258", 0),
                        new FilterData("Natasha Lansangan", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/38005627_1874541502637485_6362412849754537984_n.jpg?_nc_cat=0&oh=2349cb75007bd1469b9e3cdea984956b&oe=5C05C5B1", "68035", 0)};

                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                filterLeaderBoardAdapter = new FilterLeaderBoardAdapter(filterData);
                mRecyclerView.setAdapter(filterLeaderBoardAdapter);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                break;

            case R.id.velocity:
                Toast.makeText(getBaseContext(), "You selected Velocity", Toast.LENGTH_SHORT).show();

                filterData = new FilterData[]{new FilterData("Darren Zayne Rioja", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/32392071_1777578212264600_4825582021197692928_n.jpg?_nc_cat=0&oh=7404e6b9315e7f9624c5b190d2805f26&oe=5BC92C15", "201212", 1),
                        new FilterData("Stephan Español", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/32643274_1909703092373937_1759311178166697984_n.jpg?_nc_cat=0&oh=aee5c53ad4b569dc1caee6b981172807&oe=5C122E29", "91230", 1),
                        new FilterData("Ricardo Javier", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/17630078_10212235245337983_5815909305872402187_n.jpg?_nc_cat=0&oh=a9d98cd15f8ca45b39aed5f3eb43afe3&oe=5BD67F3E", "90230", 1),
                        new FilterData("Kurt Estevon Suico", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/37772651_10214966617296209_1688246073761464320_n.jpg?_nc_cat=0&oh=1291fa4c2ebc13250a80d5a5f079aaa1&oe=5C117E2D", "88230", 1),
                        new FilterData("Karmen Gonzaga", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/36755255_10205004694113744_5038489945266716672_n.jpg?_nc_cat=0&oh=4c29535f8ea85a606dac7cd952f74111&oe=5BC58C6F", "87230", 1),
                        new FilterData("Brielle Degayo", "https://scontent.fmnl8-1.fna.fbcdn.net/v/t1.0-9/34984630_10209891860328226_492009420247531520_n.jpg?_nc_cat=0&oh=82656e58c82639708c3bacf2bd37ac5b&oe=5C0D5193", "84230", 1)};
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
