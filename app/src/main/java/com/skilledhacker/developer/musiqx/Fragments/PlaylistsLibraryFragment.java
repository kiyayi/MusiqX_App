package com.skilledhacker.developer.musiqx.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.skilledhacker.developer.musiqx.Adapters.SearchMusicAdapter;
import com.skilledhacker.developer.musiqx.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guy on 4/23/2017.
 */

public class PlaylistsLibraryFragment extends Fragment {

    RecyclerView list_container;

    public PlaylistsLibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        List<String> items = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            items.add("test " + i);
        }

        final View view = inflater.inflate(R.layout.fragment_library_playlists,container,false);
        list_container = (RecyclerView)view.findViewById(R.id.list_playlist_container);
        SearchMusicAdapter adapter = new SearchMusicAdapter(items);
        list_container.setAdapter(adapter);
        list_container.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }


}
