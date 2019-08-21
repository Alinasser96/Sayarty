package com.alyndroid.syarty.ui.base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.application.SyartyApp;
import com.google.android.material.navigation.NavigationView;

import io.reactivex.disposables.CompositeDisposable;

public class BaseNavigatorActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected MenuItem logoutMenuItem;
    //    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        navigationView.setNavigationItemSelectedListener(this);

//        navigationView.getMenu().findItem(R.id.nav_version).setTitle(String.format(getString(R.string.version_number),
//                SyartyApp.Application.VERSION_NAME));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    protected void showUserData() {
        //login action
    }


}

