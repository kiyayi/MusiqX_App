package com.skilledhacker.developer.musiqx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import com.skilledhacker.developer.musiqx.Adapters.SearchMusicAdapter;
import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.Database.DatabaseSynchronizer;
import com.skilledhacker.developer.musiqx.Fragments.AlbumSearchFragment;
import com.skilledhacker.developer.musiqx.Fragments.ArtistSearchFragment;
import com.skilledhacker.developer.musiqx.Fragments.SongSearchFragment;
import com.skilledhacker.developer.musiqx.Player.Audio;

import java.util.ArrayList;
import java.util.List;

public class SearchCoordianatorFragmentActivity extends AppCompatActivity {

    private TabLayout tab_search;
    private Toolbar toolbar_search;
    private ViewPager viewPager_search;
    private ActionBarDrawerToggle toggle_search;
    private DrawerLayout drawer_search;
    private SearchView searchView;
    private DatabaseHandler databaseHandler;
    private ArrayList<Audio> audioList;
    private SearchMusicAdapter song_adapter;
    private SearchMusicAdapter album_adapter;
    private SearchMusicAdapter artist_adapter;

    private int[] tabIcons = {
            R.drawable.song,
            R.drawable.artist,
            R.drawable.album
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_coordiantor);

        tab_search = (TabLayout)findViewById(R.id.tabs_search_coordinator);
        toolbar_search = (Toolbar)findViewById(R.id.toolbar_search_coordinator);
        viewPager_search = (ViewPager)findViewById(R.id.pager_search_coordinator);
        drawer_search = (DrawerLayout)findViewById(R.id.drawer_search_coordinator);
        searchView = (SearchView)findViewById(R.id.search_tool_select);
        databaseHandler = new DatabaseHandler(this);
        audioList = databaseHandler.retrieve_music();
        song_adapter = new SearchMusicAdapter(0,audioList);
        album_adapter = new SearchMusicAdapter(2,audioList);
        artist_adapter = new SearchMusicAdapter(1,audioList);

        setSupportActionBar(toolbar_search);
        toggle_search = new ActionBarDrawerToggle(this,drawer_search,toolbar_search,R.string.drawer_open,R.string.drawer_close);
        drawer_search.addDrawerListener(toggle_search);
        toggle_search.syncState();
        setupViewPager(viewPager_search);
        tab_search.setupWithViewPager(viewPager_search);
        setupTabIcons();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                song_adapter.getFilter().filter(newText);
                album_adapter.getFilter().filter(newText);
                artist_adapter.getFilter().filter(newText);
                setupViewPager(viewPager_search);
                tab_search.setupWithViewPager(viewPager_search);
                setupTabIcons();

                return false;
            }
        });

    }

    private void setupTabIcons() {
        tab_search.getTabAt(0).setIcon(tabIcons[0]);
        tab_search.getTabAt(1).setIcon(tabIcons[1]);
        tab_search.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {

        SearchCoordianatorFragmentActivity.ViewPagerAdapter adapter = new SearchCoordianatorFragmentActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SongSearchFragment(song_adapter),getString(R.string.songs));
        adapter.addFragment(new ArtistSearchFragment(artist_adapter), getString(R.string.artists));
        adapter.addFragment(new AlbumSearchFragment(album_adapter), getString(R.string.albums));
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
}
