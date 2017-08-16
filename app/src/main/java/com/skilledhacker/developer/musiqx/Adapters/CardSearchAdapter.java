package com.skilledhacker.developer.musiqx.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.skilledhacker.developer.musiqx.Models.Audio;
import com.skilledhacker.developer.musiqx.R;
import com.skilledhacker.developer.musiqx.Utilities.ItemClickSupport;

import java.util.ArrayList;


/**
 * Created by apostolus on 15/08/17.
 */

public class CardSearchAdapter extends RecyclerView.Adapter<CardSearchAdapter.CardCordinator> {

    //private List<ItemCard> cardList;
    private String[]list_item = {"Songs","Artits","Album"};
    private ArrayList<Audio>audios;
    private Context context_Adapter;
    private CardSearchAdapter adapter;

    public CardSearchAdapter(Context context,ArrayList<Audio>audio){
        //this.cardList = cardList;
        this.audios = audio;
        this.context_Adapter = context;
    }

    private CardSearchAdapter(CardSearchAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public CardCordinator onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_no_fragment,parent,false);
        return new CardCordinator(view);
    }

    @Override
    public void onBindViewHolder(CardCordinator holder, int position) {
        holder.title_card.setText(list_item[position]);
        SearchMusicAdapter adapter = new SearchMusicAdapter(position,audios,list_item,context_Adapter);
        //adapter.getFilter().filter("t");
        holder.recycle_card.setAdapter(adapter);
        holder.recycle_card.setLayoutManager(new LinearLayoutManager(context_Adapter));
    }

    @Override
    public int getItemCount() {
        return list_item.length;
    }

    public class ItemCard{
        protected TextView title_card;
        protected RecyclerView recycle_card_item;
        protected Button button_card_item;
    }

    public static class CardCordinator extends RecyclerView.ViewHolder{

        protected TextView title_card;
        protected RecyclerView recycle_card;
        protected Button button_card;

        public CardCordinator(View itemView) {

            super(itemView);
            title_card = (TextView)itemView.findViewById(R.id.title_card);
            recycle_card = (RecyclerView)itemView.findViewById(R.id.recycler_card);
            button_card = (Button)itemView.findViewById(R.id.imageButton_card);

            ItemClickSupport.addTo(recycle_card).setOnItemClickListener(new ItemClickSupport.OnItemClickListener(){

                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                }
            });

            button_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
