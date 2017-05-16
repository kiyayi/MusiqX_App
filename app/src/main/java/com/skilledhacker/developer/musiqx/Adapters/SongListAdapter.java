package com.skilledhacker.developer.musiqx.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skilledhacker.developer.musiqx.Player.Audio;
import com.skilledhacker.developer.musiqx.PlayerActivity;
import com.skilledhacker.developer.musiqx.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Guy on 4/17/2017.
 */

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder> {
    List<Audio> list = Collections.emptyList();
    Context context;

    public SongListAdapter(List<Audio> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_layout, parent, false);
        SongViewHolder holder = new SongViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.artist.setText(list.get(position).getArtist());
        holder.album.setText(list.get(position).getAlbum());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageButton more;
        TextView title;
        TextView artist;
        TextView album;

        SongViewHolder(View itemView) {
            super(itemView);
            more=(ImageButton)itemView.findViewById(R.id.moreItemSong);
            title=(TextView) itemView.findViewById(R.id.songTitle);
            artist=(TextView) itemView.findViewById(R.id.songArtist);
            album=(TextView) itemView.findViewById(R.id.songAlbum);

            itemView.setOnClickListener(this);
            more.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            if (v.getId() == more.getId()){

            }else {
                Intent intent=new Intent(v.getContext(), PlayerActivity.class);
                Bundle b=new Bundle();
                int id=list.get(clickedPosition).getData();
                b.putInt("data",id);
                intent.putExtras(b);
                v.getContext().startActivity(intent);
            }
        }
    }

}
