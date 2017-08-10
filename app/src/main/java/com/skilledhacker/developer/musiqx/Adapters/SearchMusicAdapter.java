package com.skilledhacker.developer.musiqx.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skilledhacker.developer.musiqx.Player.Audio;
import com.skilledhacker.developer.musiqx.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by apostolus on 11/07/17.
 */

public class SearchMusicAdapter extends RecyclerView.Adapter<SearchMusicAdapter.ViewHolder> {

    private ValueFilter valueFilter;
    private int description;
    private final int SEARCH_SONG = 0;
    private final int SEARCH_ARTIST = 1;
    private final int SEARCH_aLBUM = 2;
    private List<Audio>audioList;
    private List<Audio>saved_audio;

    public SearchMusicAdapter(int id_description, List<Audio>audios) {
        this.description = id_description;
        this.audioList = audios;
        this.saved_audio = audios;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_view, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        switch (description){
            case SEARCH_SONG :
                holder.primaryText.setText(audioList.get(position).getTitle());
                holder.secondaryText.setText(audioList.get(position).getArtist()+" - "+audioList.get(position).getAlbum());
                break;

            case SEARCH_ARTIST:
                holder.primaryText.setText(audioList.get(position).getArtist());
                holder.secondaryText.setText(audioList.get(position).getTitle()+" - "+audioList.get(position).getAlbum());
                break;

            case SEARCH_aLBUM:
                holder.primaryText.setText(audioList.get(position).getAlbum());
                holder.secondaryText.setText(audioList.get(position).getTitle()+" - "+audioList.get(position).getArtist());
                break;

        }


        holder.imageSearch.setImageResource(R.drawable.search_bg);
        holder.itemView.setTag(audioList.get(position));

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView primaryText;
        public TextView secondaryText;
        public CircleImageView imageSearch;

        public ViewHolder(View itemView) {
            super(itemView);
            secondaryText = (TextView)itemView.findViewById(R.id.songArtist_text_search);
            primaryText = (TextView) itemView.findViewById(R.id.songName_text_search);
            imageSearch = (CircleImageView) itemView.findViewById(R.id.imageArtist_search);
        }
    }

    public Filter getFilter(){
        if(valueFilter==null){
            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }

    private class ValueFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if(constraint!=null && constraint.length()>0){
                List filterList = new ArrayList();

                for(int i = 0;i<saved_audio.size();i++){
                    if(saved_audio.get(i).getTitle().toUpperCase().contains(constraint.toString().toUpperCase())){
                        filterList.add(saved_audio.get(i).getTitle());
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }
            else {
                results.count = saved_audio.size();
                results.values = saved_audio;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            audioList = (List)results.values;
            notifyDataSetChanged();

        }
    }
}
