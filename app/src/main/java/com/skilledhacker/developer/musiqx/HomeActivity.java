package com.skilledhacker.developer.musiqx;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.skilledhacker.developer.musiqx.Player.MusicService;

/**
 * Created by Guy on 8/25/2017.
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public MusicService musicSrv;
    private Intent playIntent=null;
    private boolean musicBound=false;
    private boolean is_syncing=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_home);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);
        //CODE ABOVE NOT TO BE CHANGED
    }

    @Override
    protected void onDestroy() {
        musicSrv.updatePos();
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_home);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_music, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_listening:
                Intent intent=new Intent(HomeActivity.this,PlayerActivity.class);
                startActivity(intent);
                break;
            case R.id.action_search:
                break;
            case R.id.action_shuffle:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.nav_home:
                break;
            case R.id.nav_library:
                intent=new Intent(HomeActivity.this,MusicActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_discover:
                intent=new Intent(HomeActivity.this,DiscoverActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_sync:
                if (is_syncing){
                    is_syncing=false;
                    item.setTitle(R.string.nav_item_sync);
                }else {
                    is_syncing=true;
                    item.setTitle(R.string.nav_item_syncing);
                }
                break;
            case R.id.nav_settings:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_home);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
