package com.skilledhacker.developer.musiqx;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.skilledhacker.developer.musiqx.Fragments.ChartsFragment;
import com.skilledhacker.developer.musiqx.Fragments.GenreMoodFragment;
import com.skilledhacker.developer.musiqx.Fragments.NewReleasesFragment;
import com.skilledhacker.developer.musiqx.Fragments.RadiosFragment;
import com.skilledhacker.developer.musiqx.Fragments.TrendingFragment;
import com.skilledhacker.developer.musiqx.Player.MusicService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guy on 8/25/2017.
 */

public class DiscoverActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public MusicService musicSrv;
    private Intent playIntent=null;
    private boolean musicBound=false;
    private boolean is_syncing=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_discover);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_discover);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_discover);
        navigationView.setNavigationItemSelectedListener(this);
        startService();

        viewPager = (ViewPager) findViewById(R.id.pager_discover);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs_discover);
        tabLayout.setupWithViewPager(viewPager);
        //CODE ABOVE NOT TO BE CHANGED
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TrendingFragment(),getString(R.string.trending));
        adapter.addFragment(new ChartsFragment(), getString(R.string.charts));
        adapter.addFragment(new GenreMoodFragment(), getString(R.string.genre_mood));
        adapter.addFragment(new RadiosFragment(), getString(R.string.radios));
        adapter.addFragment(new NewReleasesFragment(), getString(R.string.new_releases));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_discover);
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
                Intent intent=new Intent(DiscoverActivity.this,PlayerActivity.class);
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
                intent=new Intent(DiscoverActivity.this,HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_library:
                intent=new Intent(DiscoverActivity.this,MusicActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_discover:
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_discover);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void startService(){
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }
}
