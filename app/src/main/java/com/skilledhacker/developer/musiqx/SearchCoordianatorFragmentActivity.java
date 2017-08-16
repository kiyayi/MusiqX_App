package com.skilledhacker.developer.musiqx;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.skilledhacker.developer.musiqx.Adapters.SearchMusicAdapter;
import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.Fragments.AlbumSearchFragment;
import com.skilledhacker.developer.musiqx.Fragments.ArtistSearchFragment;
import com.skilledhacker.developer.musiqx.Fragments.SongSearchFragment;
import com.skilledhacker.developer.musiqx.Models.Audio;

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
    private TabLayout.Tab CURRENT_TAB;
    private int CURRENT_TAB_POSITION = 0;
    SearchCoordianatorFragmentActivity.ViewPagerAdapter adapter_view;
    private final int SEARCH_SONG = 0;
    private final int SEARCH_ARTIST = 1;
    private final int SEARCH_ALBUM = 2;
    private String newText_tab = null;
    private String[] arrayList;
    private ImageButton return_button;

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
        return_button = (ImageButton)findViewById(R.id.imageButton_search);

        databaseHandler = new DatabaseHandler(this);
        audioList = databaseHandler.retrieve_library();
        arrayList = getResources().getStringArray(R.array.menu_item_player_activity);
        song_adapter = new SearchMusicAdapter(SEARCH_SONG,audioList,arrayList,this);
        album_adapter = new SearchMusicAdapter(SEARCH_ALBUM,audioList,arrayList,this);
        artist_adapter = new SearchMusicAdapter(SEARCH_ARTIST,audioList,arrayList,this);
        adapter_view = new SearchCoordianatorFragmentActivity.ViewPagerAdapter(getSupportFragmentManager());



        setSupportActionBar(toolbar_search);
        toggle_search = new ActionBarDrawerToggle(this,drawer_search,toolbar_search,R.string.drawer_open,R.string.drawer_close);
        drawer_search.addDrawerListener(toggle_search);
        toggle_search.syncState();
        setupViewPager(viewPager_search);
        tab_search.setupWithViewPager(viewPager_search);
        setupTabIcons();
        searchView.setQueryHint("Search "+tab_search.getTabAt(0).getText()+"...");
        searchView.setActivated(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                    //CURRENT_TAB_POSITION = tab_search.getSelectedTabPosition();
                    setAdapterPager(query);
                    setupTabIcons();

                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                //newText_tab = newText;
                    //CURRENT_TAB_POSITION = tab_search.getSelectedTabPosition();
                    setAdapterPager(newText);
                    setupTabIcons();

                return false;
            }
        });

        tab_search.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                searchView.setQueryHint("Search "+tab.getText()+"...");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setAdapterPager("");
                setupTabIcons();
                return true;
            }
        });

        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchCoordianatorFragmentActivity.this, MusicActivity.class);
                startActivity(intent);
            }
        });


    }

    private void setupTabIcons() {
        tab_search.getTabAt(0).setIcon(tabIcons[0]);
        tab_search.getTabAt(1).setIcon(tabIcons[1]);
        tab_search.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter_view.addFragment(new SongSearchFragment(song_adapter),getString(R.string.songs));
        adapter_view.addFragment(new ArtistSearchFragment(artist_adapter), getString(R.string.artists));
        adapter_view.addFragment(new AlbumSearchFragment(album_adapter), getString(R.string.albums));
        viewPager.setAdapter(adapter_view);
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

    public void setAdapterPager(String newText){
        song_adapter.getFilter().filter(newText);
        artist_adapter.getFilter().filter(newText);
        album_adapter.getFilter().filter(newText);
        adapter_view.notifyDataSetChanged();
    }
}
