package com.skilledhacker.developer.musiqx.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skilledhacker.developer.musiqx.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apostolus on 11/07/17.
 */

public class SearchMusicAdapter extends RecyclerView.Adapter<SearchMusicAdapter.ViewHolder> {

    private List items;
    private List<String> savedList;
    private ValueFilter valueFilter;



    public SearchMusicAdapter(List<String> items) {
        this.items = items;
        this.savedList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String item = (String) items.get(position);
        holder.primaryText.setText(item);
        holder.secondaryText.setText(item);
        holder.imageSearch.setImageResource(R.drawable.avatar);
        holder.itemView.setTag(item);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView primaryText;
        public TextView secondaryText;
        public ImageView imageSearch;

        public ViewHolder(View itemView) {
            super(itemView);
            secondaryText = (TextView)itemView.findViewById(R.id.songArtist_text_search);
            primaryText = (TextView) itemView.findViewById(R.id.songName_text_search);
            imageSearch = (ImageView)itemView.findViewById(R.id.imageArtist_search);

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

                for(int i = 0;i<savedList.size();i++){
                    if(savedList.get(i).toUpperCase().contains(constraint.toString().toUpperCase())){
                        filterList.add(savedList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }
            else {
                results.count = savedList.size();
                results.values = savedList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            items = (List)results.values;
            notifyDataSetChanged();

        }
    }
}
