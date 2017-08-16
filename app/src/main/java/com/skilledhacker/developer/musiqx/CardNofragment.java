package com.skilledhacker.developer.musiqx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Adapters.CardSearchAdapter;
import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.Models.Audio;
import com.skilledhacker.developer.musiqx.Utilities.ItemClickSupport;

import java.util.ArrayList;

public class CardNofragment extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHandler databaseHandler;
    private ArrayList<Audio>audios;
    private RecyclerView recycler_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_no_fragment);

        recycler_card = (RecyclerView)findViewById(R.id.recycler_card);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_no_fragment);
        databaseHandler = new DatabaseHandler(this);
        audios = databaseHandler.retrieve_library();

        CardSearchAdapter adapter = new CardSearchAdapter(this,audios);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener(){

            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(recyclerView.getContext(),audios.get(position).getSong_title(),Toast.LENGTH_SHORT).show();
            }
        });



    }
}
