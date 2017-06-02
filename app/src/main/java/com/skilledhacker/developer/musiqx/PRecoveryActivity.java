package com.skilledhacker.developer.musiqx;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Utilities.NetworkChecker;
import com.skilledhacker.developer.musiqx.Utilities.Utilitties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Guy on 6/2/2017.
 */

public class PRecoveryActivity extends AppCompatActivity {
    private EditText EmailInput;
    private Button RecoveryButon;

    private ProgressBar progressBar;
    private ScrollView scrollView;

    private String RecoveryUrl="PUT HERE URL TO SERVER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precovery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_recovery);
        setSupportActionBar(toolbar);

        EmailInput=(EditText)findViewById(R.id.email_recovery);
        RecoveryButon=(Button)findViewById(R.id.recovery_button);

        progressBar=(ProgressBar)findViewById(R.id.recovery_progress);
        scrollView=(ScrollView)findViewById(R.id.recovery_form);

        RecoveryButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email=EmailInput.getText().toString();
                EmailInput.setError(null);

                RecoveryUrl+="/ADD EMAIL";

                if (NetworkChecker.isConnected(PRecoveryActivity.this)){
                    ServerRequest serverRequest=new ServerRequest();
                    serverRequest.execute();
                    scrollView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(PRecoveryActivity.this,R.string.internet_fail,Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public class ServerRequest extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            URL url= null;
            try {
                url = new URL(RecoveryUrl);
                String response= Utilitties.getResponseFromHttpUrl(url);
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(PRecoveryActivity.this,R.string.server_fail,Toast.LENGTH_LONG).show();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(PRecoveryActivity.this,R.string.server_fail, Toast.LENGTH_LONG).show();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response) {
            if (response=="success"){
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                Snackbar.make(scrollView,R.string.email_recovery, Snackbar.LENGTH_LONG)
                        .setAction(R.string.go_back, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PRecoveryActivity.super.onBackPressed();
                            }
                        }).show();

            } else{
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                Snackbar.make(scrollView,R.string.email_free, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

}
