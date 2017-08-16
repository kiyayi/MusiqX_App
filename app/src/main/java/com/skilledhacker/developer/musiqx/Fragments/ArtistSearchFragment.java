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

import com.skilledhacker.developer.musiqx.Adapters.SearchMusicAdapter;
import com.skilledhacker.developer.musiqx.R;


/**
 * Created by apostolus on 10/08/17.
 */

@SuppressLint("ValidFragment")
public class ArtistSearchFragment extends Fragment {

    private RecyclerView list_song;
    private ImageButton button_song;
    private SearchMusicAdapter adapter;

    public ArtistSearchFragment(SearchMusicAdapter searchMusicAdapter) {
       this.adapter = searchMusicAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view_song = inflater.inflate(R.layout.search_artist_fragment, container, false);
        list_song = (RecyclerView) view_song.findViewById(R.id.list_artist_fragment);
        button_song = (ImageButton) view_song.findViewById(R.id.button_artist_fragment);
        createList();
        return view_song;
    }

    public void createList() {
        list_song.setAdapter(adapter);
        list_song.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}