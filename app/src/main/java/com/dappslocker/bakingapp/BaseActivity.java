package com.dappslocker.bakingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;


@SuppressWarnings("FieldCanBeLocal")
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FrameLayout mMasterFragmentContainer;
    private DrawerLayout mDrawer;
    private NavigationView  navigationView;
    private Toolbar toolbar;

    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mDrawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        mMasterFragmentContainer = findViewById(R.id.fragment_container);

        setSupportActionBar(toolbar);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_recipes) {
            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            this.startActivity(intent);
        }else if (id == R.id.nav_credits) {
            Intent intent = new Intent(getApplicationContext(), CreditsActivity.class);
            this.startActivity(intent);
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }
    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    @SuppressWarnings("EmptyMethod")
    private void overridePendingTransitionEnter() { }
    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    @SuppressWarnings("EmptyMethod")
    private void overridePendingTransitionExit() { }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    FrameLayout getMasterFragmentContainer(){
        return  mMasterFragmentContainer;
    }
}