package com.skilledhacker.developer.musiqx.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skilledhacker.developer.musiqx.R;

/**
 * Created by Guy on 4/23/2017.
 */

public class AlbumsLibraryFragment extends Fragment {

    public AlbumsLibraryFragment() {
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
        return inflater.inflate(R.layout.fragment_library_albums, container, false);
    }

}