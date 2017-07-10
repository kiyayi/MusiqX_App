package com.skilledhacker.developer.musiqx;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import com.skilledhacker.developer.musiqx.Fragments.AlbumSearchFragment;
import com.skilledhacker.developer.musiqx.Fragments.ArtistSearchFragment;
import com.skilledhacker.developer.musiqx.Fragments.SongSearchFragment;
import com.skilledhacker.developer.musiqx.Utilities.Utilities;

/**
 * Created by apostolus on 09/07/17.
 */

public class Search_Activity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;



    public void onCreate(Bundle saveStateInstance) {
        super.onCreate(saveStateInstance);
        setContentView(R.layout.search_content);


        viewPager = (ViewPager)findViewById(R.id.search_View_pager);
        tabLayout = (TabLayout)findViewById(R.id.tab_search);
        searchView = (SearchView)findViewById(R.id.search_edit);

       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_search);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();
        viewPagerUpdate(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        tabLayout.setupWithViewPager(viewPager);
    }

    public void viewPagerUpdate(ViewPager pager){

        Utilities.ViewPagerAdapter adapter = new Utilities.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AlbumSearchFragment(),"Album");
        adapter.addFragment(new ArtistSearchFragment(),"Artist");
        adapter.addFragment(new SongSearchFragment(),"Song");
        pager.setAdapter(adapter);
    }

}
