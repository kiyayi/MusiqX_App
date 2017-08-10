package com.skilledhacker.developer.musiqx;

import android.app.SearchManager;
import android.content.ClipData;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skilledhacker.developer.musiqx.Fragments.AlbumsLibraryFragment;
import com.skilledhacker.developer.musiqx.Fragments.ArtistsLibraryFragment;
import com.skilledhacker.developer.musiqx.Fragments.PlaylistsLibraryFragment;
import com.skilledhacker.developer.musiqx.Fragments.SongsLibraryFragment;
import com.skilledhacker.developer.musiqx.Player.MusicService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guy on 4/22/2017.
 */

public class MusicActivity extends AppCompatActivity


        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String text = "com.skilledhacker.developer.musiqx.PlayerActivity";

    private TabLayout tabLayout;
    private Toolbar toolbar_music;
    private Toolbar toolbar_temporary;
    private DrawerLayout drawer_music;
    ActionBarDrawerToggle toggle_music;
    private ViewPager viewPager;
    private boolean search_toolbar_is_open = true;
    private int[] tabIcons = {
            R.drawable.playlist,
            R.drawable.song,
            R.drawable.artist,
            R.drawable.album
    };

    private MusicService musicSrv;
    private Intent playIntent=null;
    private boolean musicBound=false;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        drawer_music = (DrawerLayout) findViewById(R.id.drawer_music);

        if(!search_toolbar_is_open) {

            toolbar_music = (Toolbar) findViewById(R.id.toolbar_music);
            toolbar_temporary = (Toolbar)findViewById(R.id.toolbar_search_music);
            toolbar_music.setVisibility(View.VISIBLE);
            toolbar_temporary.setVisibility(View.INVISIBLE);
            setSupportActionBar(toolbar_music);
            toggle_music = new ActionBarDrawerToggle(
                    this, drawer_music, toolbar_music, R.string.drawer_open, R.string.drawer_close);
            drawer_music.addDrawerListener(toggle_music);
            toggle_music.syncState();
        }

        if(search_toolbar_is_open){
            toolbar_music = (Toolbar)findViewById(R.id.toolbar_search_music);
            toolbar_temporary = (Toolbar) findViewById(R.id.toolbar_music);
            toolbar_temporary.setVisibility(View.INVISIBLE);
            toolbar_music.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar_music);
            toggle_music = new ActionBarDrawerToggle(
                    this, drawer_music, toolbar_music, R.string.drawer_open, R.string.drawer_close);
            drawer_music.addDrawerListener(toggle_music);
            toggle_music.syncState();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_music);
        navigationView.setNavigationItemSelectedListener(this);
        startService();

        viewPager = (ViewPager) findViewById(R.id.pager_music);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs_music);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        //CODE ABOVE NOT TO BE CHANGED


    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PlaylistsLibraryFragment(),getString(R.string.playlists));
        adapter.addFragment(new SongsLibraryFragment(), getString(R.string.songs));
        adapter.addFragment(new ArtistsLibraryFragment(), getString(R.string.artists));
        adapter.addFragment(new AlbumsLibraryFragment(), getString(R.string.albums));
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
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_music);
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
        MenuItem item = (MenuItem)menu.findItem(R.id.action_search);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_listening:
                Intent intent=new Intent(MusicActivity.this,PlayerActivity.class);
                startActivity(intent);
                break;

            case  R.id.action_search:
                break;

            case R.id.action_shuffle:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_library:
                break;
            case R.id.nav_discover:
                break;
            case R.id.nav_sync:
                item.setActionView(new ProgressBar(this));
                item.setActionView(null);
                break;
            case R.id.nav_settings:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_music);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
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
