package com.skilledhacker.developer.musiqx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skilledhacker.developer.musiqx.Adapters.SearchMusicAdapter;
import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.Models.Audio;

import java.util.ArrayList;

public class ViewAllSearchActivity extends AppCompatActivity {

    private TextView title;
    private RecyclerView recyclerView;
    private ImageButton back_button;
    private SearchMusicAdapter adapter;
    private DatabaseHandler databaseHandler;
    private ArrayList<Audio> audios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_search);

        title = (TextView)findViewById(R.id.title_see_all);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_see_all);
        back_button = (ImageButton)findViewById(R.id.back_see_all);
        databaseHandler = new DatabaseHandler(this);
        audios = databaseHandler.retrieve_library();

        Intent intent = getIntent();
        String textSearch = intent.getStringExtra("newText");
        String TextNameType = intent.getStringExtra("StringTitle");
        int description = intent.getIntExtra("id",0);

        title.setText("All "+TextNameType);
        adapter = new SearchMusicAdapter(description,audios);
        adapter.getFilter().filter(textSearch);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewAllSearchActivity.this,SearchActivity.class));
            }
        });


    }
}
