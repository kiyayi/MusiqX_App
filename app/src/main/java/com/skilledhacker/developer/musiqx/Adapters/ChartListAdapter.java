package com.skilledhacker.developer.musiqx.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Models.Chart;
import com.skilledhacker.developer.musiqx.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Guy on 11/12/2017.
 */

public class ChartListAdapter extends RecyclerView.Adapter<ChartListAdapter.ChartViewHolder> {
    List<Chart> list = Collections.emptyList();
    Context context;

    public ChartListAdapter(List<Chart> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ChartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart, parent, false);
        ChartViewHolder holder = new ChartViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChartViewHolder holder, int position) {
        holder.title.setText(list.get(position).getChart_title());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ChartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;

        ChartViewHolder(View itemView) {
            super(itemView);
            title=(TextView) itemView.findViewById(R.id.chart_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            Toast.makeText(context,"Hello, you clicked #"+clickedPosition,Toast.LENGTH_LONG).show();
        }
    }
}
