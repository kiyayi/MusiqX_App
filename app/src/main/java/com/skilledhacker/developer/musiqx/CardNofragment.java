package com.skilledhacker.developer.musiqx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.skilledhacker.developer.musiqx.Adapters.CardSearchAdapter;
import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.Models.Audio;

import java.util.ArrayList;

public class CardNofragment extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHandler databaseHandler;
    private ArrayList<Audio>audios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_no_fragment);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_no_fragment);
        databaseHandler = new DatabaseHandler(this);
        audios = databaseHandler.retrieve_library();

        CardSearchAdapter adapter = new CardSearchAdapter(this,audios);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }
}
