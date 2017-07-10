package com.skilledhacker.developer.musiqx.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.skilledhacker.developer.musiqx.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.skilledhacker.developer.musiqx.R.layout.fragment_search_album;

/**
 * Created by apostolus on 09/07/17.
 */

public class AlbumSearchFragment extends Fragment {



    public AlbumSearchFragment(){


    }

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        return inflater.inflate(fragment_search_album,container,false);
    }
}
