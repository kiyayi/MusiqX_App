package com.skilledhacker.developer.musiqx.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skilledhacker.developer.musiqx.R;

/**
 * Created by apostolus on 09/07/17.
 */

public class ArtistSearchFragment extends Fragment {

    public ArtistSearchFragment(){

    }

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(R.layout.fragment_search_artist,container,false);
    }

}
