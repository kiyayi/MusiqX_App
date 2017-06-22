package com.skilledhacker.developer.musiqx.Utilities;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.skilledhacker.developer.musiqx.R;


/**
 * Created by apostolus on 21/06/17.
 */

public class Gestions_menu_bar {

    public static LayoutAnimationController animationController;

    public static LayoutAnimationController openMoreSongBar (long duration, Context ctx, LinearLayout toHide) {

        animationController = AnimationUtils.loadLayoutAnimation(ctx, R.anim.layout_anim_bar_open);

        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f,0.0f,toHide.getHeight(), 0.0f);
        translateAnimation.setDuration(duration);
        animationController.setAnimation(translateAnimation);

        return  animationController;

    }

    public static  LayoutAnimationController closeMoreSongBar(long duration, Context ctx, LinearLayout toHide){

        animationController = AnimationUtils.loadLayoutAnimation(ctx,R.anim.layout_anim_bar_open);
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f,0.0f,0.0f, toHide.getHeight());
        translateAnimation.setDuration(duration);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setFillAfter(true);
        animationController.setAnimation(translateAnimation);

        return  animationController;

    }

    public static LayoutAnimationController clearPlayertools(long duration, Context cxt){
        animationController = AnimationUtils.loadLayoutAnimation(cxt,R.anim.layout_anim_bar_open);
        AlphaAnimation alpha = new AlphaAnimation((float)1,(float)0);
        alpha.setDuration(duration);
        alpha.setFillAfter(true);
        alpha.setFillEnabled(true);
        animationController.setAnimation(alpha);

        return animationController;
    }

}
