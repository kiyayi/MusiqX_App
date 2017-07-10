package com.skilledhacker.developer.musiqx.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.skilledhacker.developer.musiqx.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apostolus on 09/07/17.
 */

public class SearchAdapter extends ArrayAdapter implements Filterable{

    private List<String> listFiltered;
    private List<String> listNonFiltered;
    private ValueFilter valueFilter;


    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.search_list_view,parent,false);
        TextView item_name = (TextView)rowView.findViewById(R.id.search_string_item);

        if(convertView == null){
            item_name.setText((CharSequence)getItem(position));
        }
        else {
            rowView = convertView;
        }

        return rowView;
    }

    public class ValueFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if(constraint!=null && constraint.length()>0){
                List listFilter = new ArrayList();

                for(int i=0;i<listFiltered.size();i++){
                    if(listFiltered.get(i).toUpperCase().contains(constraint.toString().toUpperCase())){
                        listFilter.add(listFiltered.get(i));
                    }
                }
                results.count = listFilter.size();
                results.values = listFilter;
            }

            else {
                results.values = listFiltered;
                results.count = listFiltered.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listNonFiltered = (List)results.values;
            notifyDataSetChanged();
        }
    }

    public Filter getFilter(){
        if(valueFilter == null){
            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }


    public SearchAdapter(Context context, String[]values) {
        super(context, R.layout.search_list_view,values);

        /*for(int i = 0;i<values.length;i++){
            listFiltered.add(values[i]);
            listNonFiltered.add(values[i]);
        }*/
    }
}
