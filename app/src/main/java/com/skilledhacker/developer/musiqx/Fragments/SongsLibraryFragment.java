package com.skilledhacker.developer.musiqx.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Adapters.SongListAdapter;
import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.Database.DatabaseUpdater;
import com.skilledhacker.developer.musiqx.Models.Audio;
import com.skilledhacker.developer.musiqx.MusicActivity;
import com.skilledhacker.developer.musiqx.R;
import com.skilledhacker.developer.musiqx.Utilities.NetworkChecker;

import java.util.ArrayList;

/**
 * Created by Guy on 4/23/2017.
 */

public class SongsLibraryFragment extends Fragment {

    private ArrayList<Audio> audioList;
    private SongListAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseHandler database;
    private LinearLayout ListContainer;
    private LinearLayout NoList;
    private LinearLayout NoSong;
    private ProgressBar LoadingBar;
    private Button Sync;
    private BroadcastReceiver LibrarySyncReceiver;

    public SongsLibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view=inflater.inflate(R.layout.fragment_library_songs, container, false);

        ListContainer=(LinearLayout)view.findViewById(R.id.ListContainer);
        NoList=(LinearLayout)view.findViewById(R.id.NoList);
        NoSong=(LinearLayout)view.findViewById(R.id.NoSong);
        LoadingBar=(ProgressBar)view.findViewById(R.id.LoadingBar);
        Sync=(Button)view.findViewById(R.id.SyncSong);

        database=new DatabaseHandler(getActivity());

        LoadSongs(view);

        Sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoSong.setVisibility(View.GONE);
                LoadingBar.setVisibility(View.VISIBLE);
                if(NetworkChecker.isConnected(getActivity())) {
                    ((MusicActivity)getActivity()).musicSrv.update_database();
                }else{
                    Toast.makeText(getActivity(),R.string.internet_fail,Toast.LENGTH_LONG).show();
                }
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(DatabaseUpdater.SYNC_LIBRARY_BROADCAST);
        LibrarySyncReceiver =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NoSong.setVisibility(View.VISIBLE);
                LoadingBar.setVisibility(View.GONE);
                LoadSongs(view);
            }
        };

        getActivity().registerReceiver(LibrarySyncReceiver,filter);

        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (LibrarySyncReceiver != null) {
            getActivity().unregisterReceiver(LibrarySyncReceiver);
            LibrarySyncReceiver = null;
        }
    }

    private void LoadSongs(View view){
        audioList=database.retrieve_library();
        if (audioList.size() > 0) {
            NoList.setVisibility(View.GONE);
            ListContainer.setVisibility(View.VISIBLE);
            recyclerView = (RecyclerView) view.findViewById(R.id.SongList);
            adapter = new SongListAdapter(audioList, getActivity().getApplication());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }else {
            ListContainer.setVisibility(View.GONE);
            NoList.setVisibility(View.VISIBLE);
        }
    }

}