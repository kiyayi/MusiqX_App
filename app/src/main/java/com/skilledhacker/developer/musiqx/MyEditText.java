package com.skilledhacker.developer.musiqx;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by apostolus on 01/06/17.
 */

public class MyEditText extends EditText{

    public MyEditText(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    public void setError(CharSequence error, Drawable icon){
        setCompoundDrawables(null,null,icon,null);
    }
}
