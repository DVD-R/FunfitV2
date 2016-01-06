package com.funfit.usjr.thesis.funfitv2.main;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.funfit.usjr.thesis.funfitv2.R;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigationView();
        setupToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(drawerLayout != null)
                    drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationView(){

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null)
            setSupportActionBar(toolbar);

        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    /*This method initialize the position of navigation drawer items*/
    /*after you click in navigation drawer icon it return integer position and focus a tab by the use of setCurrentItem()*/
    private void navigate(final int itemId) {
        switch (itemId) {
            case R.id.nav_health_pref:
                break;
            case R.id.nav_meal_archeive:
                break;
            case R.id.nav_food_description:
                break;
            case R.id.nav_challenge:
                break;
            case R.id.nav_weekly:
                break;
            case R.id.nav_history:
                break;
            case R.id.nav_event:
                break;
        }
    }

    /*if you click specific item in navigation drawer it automatically close the navigation drawer*/
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        navigate(menuItem.getItemId());
                        return true;
                    }
                });
    }
}