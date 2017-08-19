package com.skilledhacker.developer.musiqx.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Models.Audio;
import com.skilledhacker.developer.musiqx.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by apostolus on 11/07/17 ; 20:19
 */

public class SearchMusicAdapter extends RecyclerView.Adapter<SearchMusicAdapter.ViewHolder> {

    private ValueFilter valueFilter;
    private int description;
    private final int SEARCH_SONG = 0;
    private final int SEARCH_ARTIST = 1;
    private final int SEARCH_aLBUM = 2;
    private ArrayList<Audio>audioList;
    private ArrayList<Audio>saved_audio;
    private ArrayList<Audio>empty;
    

    public SearchMusicAdapter(int id_description, ArrayList<Audio> audios) {
        this.description = id_description;
        this.audioList = audios;
        this.saved_audio = audios;
        this.empty = new ArrayList<>();
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

        if(audioList.size()>0) {
            switch (description) {
                case SEARCH_SONG:
                    holder.primaryText.setText(audioList.get(position).getSong_title());
                    holder.secondaryText.setText(audioList.get(position).getArtist_name() + " - " + audioList.get(position).getAlbum_name());
                    break;

                case SEARCH_ARTIST:
                    holder.primaryText.setText(audioList.get(position).getArtist_name());
                    holder.secondaryText.setText(audioList.get(position).getSong_title() + " - " + audioList.get(position).getAlbum_name());
                    break;

                case SEARCH_aLBUM:
                    holder.primaryText.setText(audioList.get(position).getAlbum_name());
                    holder.secondaryText.setText(audioList.get(position).getSong_title()+ " - " + audioList.get(position).getArtist_name());
                    break;

            }


            holder.imageSearch.setImageResource(R.drawable.search_bg);
            holder.itemView.setTag(audioList.get(position));
            //holder.spinner.setAdapter(menu_adapter);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView primaryText;
        private TextView secondaryText;
        private CircleImageView imageSearch;
        //private Spinner spinner;

        private ViewHolder(View itemView) {
            super(itemView);
            secondaryText = (TextView)itemView.findViewById(R.id.songArtist_text_search);
            primaryText = (TextView) itemView.findViewById(R.id.songName_text_search);
            imageSearch = (CircleImageView) itemView.findViewById(R.id.imageArtist_search);
            //spinner = (Spinner)itemView.findViewById(R.id.like_search_song);
        }

        @Override
        public void onClick(View v) {
            int itemClick = getAdapterPosition();
            Toast.makeText(v.getContext(),audioList.get(itemClick).getSong_title(),Toast.LENGTH_SHORT).show();
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
                ArrayList<Audio> filterList = new ArrayList<>();
                switch (description){

                    case SEARCH_SONG:
                        for(int i = 0;i<saved_audio.size();i++){
                            if(saved_audio.get(i).getSong_title().toUpperCase().contains(constraint.toString().toUpperCase())){
                                filterList.add(saved_audio.get(i));
                            }
                        }
                        break;
                    case SEARCH_ARTIST:
                        for(int i = 0;i<saved_audio.size();i++){
                            if(saved_audio.get(i).getArtist_name().toUpperCase().contains(constraint.toString().toUpperCase())){
                                filterList.add(saved_audio.get(i));
                            }
                        }
                        break;
                    case SEARCH_aLBUM:
                        for(int i = 0;i<saved_audio.size();i++){
                            if(saved_audio.get(i).getAlbum_name().toUpperCase().contains(constraint.toString().toUpperCase())){
                                filterList.add(saved_audio.get(i));
                            }
                        }
                        break;
                }
                results.count = filterList.size();
                results.values = filterList;
            }
            else {
                results.count = empty.size();
                results.values = empty;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            audioList = (ArrayList<Audio>)results.values;
            notifyDataSetChanged();

        }
    }
}
