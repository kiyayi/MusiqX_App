package com.skilledhacker.developer.musiqx.Adapters;

import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skilledhacker.developer.musiqx.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by apostolus on 11/07/17.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {

    private List<Pair<String,String>> maList = Arrays.asList(
            Pair.create("hello","bonjour"),Pair.create("Gute","nibon?")
    );



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_adapter_search,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Pair<String,String>pair = maList.get(position);
        holder.display(pair);
    }


    @Override
    public int getItemCount() {
        return maList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView name = null;
        private TextView description = null;
        private Pair<String,String> currentPair;


        public MyViewHolder(final View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.text1111);
            description = (TextView)itemView.findViewById(R.id.text2222);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(currentPair.first)
                            .setMessage(currentPair.second)
                            .show();
                }
            });


        }

        public void display(Pair<String,String>pair){
            currentPair = pair;
            name.setText(pair.first);
            description.setText(pair.second);
        }
    }


}
