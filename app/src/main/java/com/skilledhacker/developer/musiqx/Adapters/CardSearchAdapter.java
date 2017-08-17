package com.skilledhacker.developer.musiqx.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.R;
import com.skilledhacker.developer.musiqx.Utilities.ItemClickSupport;

import java.util.ArrayList;


/**
 * Created by apostolus on 15/08/17 ; 20:15
 */

public class CardSearchAdapter extends RecyclerView.Adapter<CardSearchAdapter.CardCordinator> {

    private String[]list_item = {"Songs","Artist","Album"};
    private Context context_adapter;
    private ArrayList<SearchMusicAdapter>list_adapter;

    public CardSearchAdapter(Context context,ArrayList<SearchMusicAdapter>adapter){
        this.context_adapter = context;
        this.list_adapter = adapter;
    }

    @Override
    public CardCordinator onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_no_fragment,parent,false);
        return new CardCordinator(view);
    }

    @Override
    public void onBindViewHolder(CardCordinator holder, int position) {
        holder.title_card.setText(list_item[position]);
        holder.recycle_card.setAdapter(list_adapter.get(position));
        holder.recycle_card.setLayoutManager(new LinearLayoutManager(context_adapter));
    }

    @Override
    public int getItemCount() {
        return list_item.length;
    }

    static class CardCordinator extends RecyclerView.ViewHolder{

        TextView title_card;
        RecyclerView recycle_card;
        Button button_card;

        CardCordinator(View itemView) {

            super(itemView);
            title_card = (TextView)itemView.findViewById(R.id.title_card);
            recycle_card = (RecyclerView)itemView.findViewById(R.id.recycler_card);
            button_card = (Button)itemView.findViewById(R.id.imageButton_card);

            ItemClickSupport.addTo(recycle_card).setOnItemClickListener(new ItemClickSupport.OnItemClickListener(){
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Toast.makeText(recyclerView.getContext(),"hello "+position,Toast.LENGTH_SHORT).show();
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
