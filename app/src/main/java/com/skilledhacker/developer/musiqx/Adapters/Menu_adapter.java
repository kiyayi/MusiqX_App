package com.skilledhacker.developer.musiqx.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skilledhacker.developer.musiqx.PlayerActivity;
import com.skilledhacker.developer.musiqx.R;

/**
 * Created by apostolus on 20/06/17.
 */

public class Menu_adapter extends ArrayAdapter {

    private Integer[] tab_images = {
            R.drawable.playlist,
            R.drawable.artist,
            R.drawable.album,
            R.drawable.song
    };

    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_view_menu,parent,false);
        TextView textView = (TextView)rowView.findViewById(R.id.item_menu);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.image_menu);
        textView.setText((CharSequence) getItem(position));

        if(convertView == null){
            imageView.setImageResource(tab_images[position]);
        }
        else {
            rowView = (View)convertView;
        }
        return rowView;
    }

    public Menu_adapter(PlayerActivity context, String[] values) {
        super(context,R.layout.list_view_menu,values);
    }

    public void onClick(View v){
        
    }
}
