package com.skilledhacker.developer.musiqx.Utilities;

import android.content.Context;
import android.media.AudioManager;
import android.widget.SeekBar;

/**
 * Created by apostolus on 23/06/17.
 */

public class Volume_Controller {

    private static AudioManager audioManager;



    public static void volume_Controller(SeekBar seekBar, Context ctx) {

        try {

            audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
            seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

