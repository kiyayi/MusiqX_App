package com.skilledhacker.developer.musiqx.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.R;
import com.skilledhacker.developer.musiqx.SearchActivity;
import com.skilledhacker.developer.musiqx.Utilities.ItemClickSupport;
import com.skilledhacker.developer.musiqx.ViewAllSearchActivity;

import java.util.ArrayList;


/**
 * Created by apostolus on 15/08/17 ; 20:15
 */

public class CardSearchAdapter extends RecyclerView.Adapter<CardSearchAdapter.CardCordinator> {

    private String[]list_item = {"Songs","Artists","Albums"};
    private Context context_adapter;
    private ArrayList<SearchMusicAdapter>list_adapter;

    public CardSearchAdapter(Context context,ArrayList<SearchMusicAdapter>adapter){
        this.context_adapter = context;
        this.list_adapter = adapter;
    }

    @Override
    public CardCordinator onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_no_fragment, parent, false);
        return new CardCordinator(view, viewType);
    }

    @Override
    public void onBindViewHolder(CardCordinator holder, int position) {

        holder.title_card.setText(list_item[position]);
        holder.recycle_card.setAdapter(list_adapter.get(position));
        holder.recycle_card.setLayoutManager(new LinearLayoutManager(context_adapter));
        holder.button_card.setText("Show all "+holder.title_card.getText());
    }

    @Override
    public int getItemCount() {
        return list_item.length;
    }


    static class CardCordinator extends RecyclerView.ViewHolder{

        TextView title_card;
        RecyclerView recycle_card;
        Button button_card;
        int id;

        CardCordinator(final View itemView, int description) {
            super(itemView);
            this.id = description;
            title_card = (TextView)itemView.findViewById(R.id.title_card);
            recycle_card = (RecyclerView)itemView.findViewById(R.id.recycler_card);
            button_card = (Button)itemView.findViewById(R.id.imageButton_card);

            ItemClickSupport.addTo(recycle_card).setOnItemClickListener(new ItemClickSupport.OnItemClickListener(){
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Toast.makeText(recyclerView.getContext(),"position "+position,Toast.LENGTH_SHORT).show();
                }
            });

            button_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),ViewAllSearchActivity.class);
                    intent.putExtra("newText",SearchActivity.transferExtrat());
                    intent.putExtra("StringTitle",title_card.getText());
                    intent.putExtra("id",getPosition());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
