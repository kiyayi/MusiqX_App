package com.skilledhacker.developer.musiqx;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.R.animator;
import java.util.logging.Handler;
import android.widget.ListView;

/**
 * Created by 41EM on 28/05/2017.
 */

public class CustomToast
{

    protected String error;
    protected View view;
    protected Context context;


    public static CustomToast {
        // Custom Toast Method
        public void showToast (Context context, View view, String error)
    {

        // Layout Inflater for inflating custom view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate the layout over view
        View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup) layout.findViewById(R.id.toast_root));

        // Get TextView id and set error
        TextView text;
        text = (TextView) layout.findViewById(R.id.toast_error);
        text.setText(error);

        Toast toast = new Toast(context);// Get Toast Context
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
        // Toast
        // gravity
        // and
        // Fill
        // Horizoontal

        toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
        toast.setView(layout); // Set Custom View over toast

        toast.show();// Finally show toast
    }
    }
}
