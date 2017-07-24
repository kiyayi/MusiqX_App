package com.skilledhacker.developer.musiqx;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.skilledhacker.developer.musiqx.Adapters.SearchMusicAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView list_container;
    SearchMusicAdapter adapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (SearchView)findViewById(R.id.search_final);
        setSupportActionBar(toolbar);

        searchView.setActivated(true);
        searchView.setQueryHint("Type your keyword...");
        searchView.setIconified(false);
        searchView.clearFocus();

        List<String> items = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            items.add("test " + i);
        }

        list_container = (RecyclerView)findViewById(R.id.recycler_search_view);
        adapter = new SearchMusicAdapter(items);
        list_container.setAdapter(adapter);
        list_container.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.search_toolbar_final);

        searchView = (SearchView)findViewById(R.id.search_final);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                list_container.setAdapter(adapter);
                list_container.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
