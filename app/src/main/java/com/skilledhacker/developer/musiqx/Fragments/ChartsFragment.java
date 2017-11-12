package com.skilledhacker.developer.musiqx.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

import com.skilledhacker.developer.musiqx.Adapters.ChartListAdapter;
import com.skilledhacker.developer.musiqx.Adapters.SongListAdapter;
import com.skilledhacker.developer.musiqx.Database.DatabaseUpdater;
import com.skilledhacker.developer.musiqx.DiscoverActivity;
import com.skilledhacker.developer.musiqx.Models.Audio;
import com.skilledhacker.developer.musiqx.Models.Chart;
import com.skilledhacker.developer.musiqx.R;
import com.skilledhacker.developer.musiqx.Utilities.NetworkChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Guy on 8/25/2017.
 */

public class ChartsFragment extends Fragment {
    private ArrayList<Audio> audioList;
    private ArrayList<Chart> chartList;
    private SongListAdapter songAdapter;
    private ChartListAdapter chartAdapter;
    private RecyclerView songView;
    private RecyclerView chartView;
    private LinearLayout ListContainer;
    private LinearLayout NoList;
    private LinearLayout NoSong;
    private ProgressBar LoadingBar;
    private Button Sync;
    private BroadcastReceiver ChartReceiver;

    public ChartsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_charts, container, false);

        ListContainer=(LinearLayout)view.findViewById(R.id.ListContainer);
        NoList=(LinearLayout)view.findViewById(R.id.NoList);
        NoSong=(LinearLayout)view.findViewById(R.id.NoSong);
        LoadingBar=(ProgressBar)view.findViewById(R.id.LoadingBar);
        Sync=(Button)view.findViewById(R.id.SyncSong);

        try {
            LoadCharts(view);
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

        IntentFilter chart_filter = new IntentFilter();
        chart_filter.addAction(DatabaseUpdater.CHART_BROADCAST);
        ChartReceiver =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NoSong.setVisibility(View.VISIBLE);
                LoadingBar.setVisibility(View.GONE);
                try {
                    LoadCharts(view);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        getActivity().registerReceiver(ChartReceiver,chart_filter);

        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (ChartReceiver != null) {
            getActivity().unregisterReceiver(ChartReceiver);
            ChartReceiver = null;
        }
    }

    private void LoadCharts(View view) throws JSONException {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String content = preferences.getString(DatabaseUpdater.CHART_PREFERENCE, "");

        Log.d("TTTT",content);

        if (content==null || content.isEmpty()) {
            ListContainer.setVisibility(View.GONE);
            NoList.setVisibility(View.VISIBLE);
        }else {
            JSONArray array=new JSONArray(content);
            chartList=new ArrayList<>();
            long size=array.length();
            for(int i=0;i<size;i++) {
                JSONObject row=array.getJSONObject(i);
                chartList.add(new Chart(row.getInt(Chart.KEY_ID),row.getString(Chart.KEY_TITLE),
                        row.getString(Chart.KEY_KEYWORD),row.getString(Chart.KEY_CREATED_AT),row.getString(Chart.KEY_UPDATED_AT)));
            }


            NoList.setVisibility(View.GONE);
            ListContainer.setVisibility(View.VISIBLE);
            chartView = (RecyclerView) view.findViewById(R.id.chartList);
            int numberOfColumns = 2;
            chartView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
            chartAdapter = new ChartListAdapter(chartList, getActivity().getApplication());
            chartView.setAdapter(chartAdapter);
            //chartView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }
}
