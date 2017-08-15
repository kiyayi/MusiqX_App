package com.skilledhacker.developer.musiqx.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Adapters.SearchMusicAdapter;
import com.skilledhacker.developer.musiqx.R;
import com.skilledhacker.developer.musiqx.Utilities.ItemClickSupport;


/**
 * Created by apostolus on 10/08/17.
 */

@SuppressLint("ValidFragment")
public class SongSearchFragment extends Fragment {

    private RecyclerView list_song;
    private ImageButton button_song;
    private SearchMusicAdapter adapter;
    private TextView non_found;

    public SongSearchFragment(SearchMusicAdapter searchMusicAdapter){
        this.adapter = searchMusicAdapter;
    }
    public SongSearchFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view_song = inflater.inflate(R.layout.search_song_fragment,container,false);
        list_song = (RecyclerView)view_song.findViewById(R.id.list_song_fragment);
        button_song = (ImageButton) view_song.findViewById(R.id.button_song_fragment);
        non_found = (TextView)view_song.findViewById(R.id.non_found_song);
        createList();

        ItemClickSupport.addTo(list_song).setOnItemClickListener(new ItemClickSupport.OnItemClickListener(){

            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Toast.makeText(getContext(),adapter.getStringMusic(position),Toast.LENGTH_SHORT).show();
            }
        });

        return view_song;
    }

    public void createList(){
        list_song.setAdapter(adapter);
        list_song.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
