package com.skilledhacker.developer.musiqx;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;

import java.text.ParseException;

/**
 * Created by Guy on 6/3/2017.
 */

public class SplashScreenActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        database=new DatabaseHandler(SplashScreenActivity.this);
        try {
            if (database.is_login()){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = new Intent(SplashScreenActivity.this, MusicActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, SPLASH_TIME_OUT);
            }else{
                login();
            }
        } catch (ParseException e) {
            //e.printStackTrace();
            login();
        }
    }

    private void login(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
