package com.skilledhacker.developer.musiqx.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.skilledhacker.developer.musiqx.DiscoverActivity;
import com.skilledhacker.developer.musiqx.Models.Audio;
import com.skilledhacker.developer.musiqx.MusicActivity;
import com.skilledhacker.developer.musiqx.R;
import com.skilledhacker.developer.musiqx.Utilities.NetworkChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Guy on 8/25/2017.
 */

public class TrendingFragment extends Fragment {
    private ArrayList<Audio> audioList;
    private SongListAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout ListContainer;
    private LinearLayout NoList;
    private LinearLayout NoSong;
    private ProgressBar LoadingBar;
    private Button Sync;
    private BroadcastReceiver TrendingSyncReceiver;

    public TrendingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_trending, container, false);

        ListContainer=(LinearLayout)view.findViewById(R.id.ListContainer);
        NoList=(LinearLayout)view.findViewById(R.id.NoList);
        NoSong=(LinearLayout)view.findViewById(R.id.NoSong);
        LoadingBar=(ProgressBar)view.findViewById(R.id.LoadingBar);
        Sync=(Button)view.findViewById(R.id.SyncSong);

        try {
            LoadSongs(view);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkChecker.isConnected(getActivity())) {
                    NoSong.setVisibility(View.GONE);
                    LoadingBar.setVisibility(View.VISIBLE);
                    ((DiscoverActivity)getActivity()).musicSrv.update_database();
                }else{
                    Toast.makeText(getActivity(),R.string.internet_fail,Toast.LENGTH_LONG).show();
                }
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(DatabaseUpdater.SYNC_TRENDING_BROADCAST);
        TrendingSyncReceiver =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NoSong.setVisibility(View.VISIBLE);
                LoadingBar.setVisibility(View.GONE);
                try {
                    LoadSongs(view);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        getActivity().registerReceiver(TrendingSyncReceiver,filter);

        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (TrendingSyncReceiver != null) {
            getActivity().unregisterReceiver(TrendingSyncReceiver);
            TrendingSyncReceiver = null;
        }
    }

    private void LoadSongs(View view) throws JSONException {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String content = preferences.getString(DatabaseUpdater.TRENDING_PREFERENCE, "");

        if (content==null || content.isEmpty()) {
            ListContainer.setVisibility(View.GONE);
            NoList.setVisibility(View.VISIBLE);
        }else {
            JSONArray array=new JSONArray(content);
            audioList=new ArrayList<>();
            long size=array.length();
            for(int i=0;i<size;i++) {
                JSONObject row=array.getJSONObject(i);
                audioList.add(new Audio(row.getInt(DatabaseHandler.KEY_LIBRARY_SONG),row.getString(DatabaseHandler.KEY_LIBRARY_SONG_TITLE),
                        row.getInt(DatabaseHandler.KEY_LIBRARY_ARTIST),row.getString(DatabaseHandler.KEY_LIBRARY_ARTIST_NAME),
                        row.getInt(DatabaseHandler.KEY_LIBRARY_ALBUM),row.getString(DatabaseHandler.KEY_LIBRARY_ALBUM_NAME),
                        row.getInt(DatabaseHandler.KEY_LIBRARY_GENRE), row.getString(DatabaseHandler.KEY_LIBRARY_GENRE_NAME),
                        row.getInt(DatabaseHandler.KEY_LIBRARY_MOOD), row.getString(DatabaseHandler.KEY_LIBRARY_MOOD_NAME),
                        row.getInt(DatabaseHandler.KEY_LIBRARY_YEAR),row.getString(DatabaseHandler.KEY_LIBRARY_LYRICS),
                        row.getInt(DatabaseHandler.KEY_LIBRARY_LICENSE),row.getString(DatabaseHandler.KEY_LIBRARY_CREATED_AT),
                        row.getString(DatabaseHandler.KEY_LIBRARY_UPDATED_AT),row.getDouble(DatabaseHandler.KEY_SCORE),
                        row.getInt(DatabaseHandler.KEY_WEEK_POPULAR),row.getInt(DatabaseHandler.KEY_MONTH_POPULAR),
                        row.getInt(DatabaseHandler.KEY_YEAR_POPULAR),row.getInt(DatabaseHandler.KEY_MONTH_LIKED)));
            }


            NoList.setVisibility(View.GONE);
            ListContainer.setVisibility(View.VISIBLE);
            recyclerView = (RecyclerView) view.findViewById(R.id.SongList);
            adapter = new SongListAdapter(audioList, getActivity().getApplication());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }
}
