package com.skilledhacker.developer.musiqx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Adapters.CardSearchAdapter;
import com.skilledhacker.developer.musiqx.Adapters.SearchMusicAdapter;
import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.Models.Audio;
import com.skilledhacker.developer.musiqx.Utilities.ItemClickSupport;

import java.util.ArrayList;

/**
 * Created by apostolus on 16/08/17 ; 20:12
 */

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHandler databaseHandler;
    private ArrayList<Audio> audios;
    private SearchView searchView;
    private SearchMusicAdapter song_adapter;
    private SearchMusicAdapter artist_adapter;
    private SearchMusicAdapter album_adapter;
    public  ArrayList<SearchMusicAdapter> list_adapter;
    private CardSearchAdapter adapter;
    protected final int SEARCH_SONG = 0;
    protected final int SEARCH_ARTIST = 1;
    protected final int SEARCH_ALBUM = 2;
    private ImageButton imageButton;
    private String[]list_item = {"Songs","Artist","Album"};
    private static String newTextChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_no_fragment);

        databaseHandler = new DatabaseHandler(this);
        audios = databaseHandler.retrieve_library();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_no_fragment);
        searchView = (SearchView)findViewById(R.id.search_tool_select);
        imageButton = (ImageButton)findViewById(R.id.imageButton_search);
        song_adapter = new SearchMusicAdapter(SEARCH_SONG,audios);
        artist_adapter = new SearchMusicAdapter(SEARCH_ARTIST,audios);
        album_adapter = new SearchMusicAdapter(SEARCH_ALBUM,audios);

        loadAdapter(null);
        adapter = new CardSearchAdapter(this,list_adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener(){
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(recyclerView.getContext(),list_item[position],Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadAdapter(query);
                Log.i("load adaptertextSubmit",query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                loadAdapter(newText);
                Log.i("load adaptertextchange",newText);
                return false;
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this,MusicActivity.class));
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                loadAdapter("");
                return false;
            }
        });
    }

    private void loadAdapter(String textChanged){
        if(textChanged==null || textChanged.isEmpty()){
           generateListAdapter();
        }
        else {

            album_adapter.getFilter().filter(textChanged);
            artist_adapter.getFilter().filter(textChanged);
            song_adapter.getFilter().filter(textChanged);

            if(album_adapter.getItemCount()!=0)list_adapter.add(album_adapter);
            else list_adapter.remove(album_adapter);
            if(artist_adapter.getItemCount()!=0)list_adapter.add(artist_adapter);
            else list_adapter.remove(artist_adapter);
            if(song_adapter.getItemCount()!=0)list_adapter.add(song_adapter);
            else list_adapter.remove(song_adapter);

            newTextChanged = textChanged;
            //adapter = new CardSearchAdapter(this,list_adapter);
            adapter.notifyDataSetChanged();

            /*recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter.notifyDataSetChanged();*/
        }
    }

    public static String transferExtra(){
        return newTextChanged;
    }

    public void generateListAdapter(){
        list_adapter = new ArrayList<>();
        list_adapter.add(song_adapter);
        list_adapter.add(artist_adapter);
        list_adapter.add(album_adapter);

    }
}
