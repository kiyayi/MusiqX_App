package com.skilledhacker.developer.musiqx;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Utilities.NetworkChecker;
import com.skilledhacker.developer.musiqx.Utilities.Utilities;
import com.skilledhacker.developer.musiqx.Utilities.Verification;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Guy on 6/2/2017.
 */

public class PRecoveryActivity extends AppCompatActivity {
    private EditText EmailInput;
    private Button RecoveryButon;
    private LinearLayout activity;
    private String RecoveryUrl;
    private int recovery_error=0;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precovery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_recovery);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecoveryUrl=getString(R.string.recovery_url);
        EmailInput=(EditText)findViewById(R.id.email_recovery);
        RecoveryButon=(Button)findViewById(R.id.recovery_button);
        activity=(LinearLayout)findViewById(R.id.activity_recovery);
        progressDialog=new ProgressDialog(PRecoveryActivity.this,R.style.MyMaterialTheme);

        RecoveryButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email=EmailInput.getText().toString();
                EmailInput.setError(null);

                String result= Verification.email_check(Email,PRecoveryActivity.this,false);
                if (result.equals("")){
                    if (NetworkChecker.isConnected(PRecoveryActivity.this)){
                        progressDialog.setMessage(getString(R.string.authenticate));
                        progressDialog.getWindow().setGravity(Gravity.CENTER);
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();
                        RecoveryUrl+="?email="+Email;

                        ServerRequest serverRequest=new ServerRequest();
                        serverRequest.execute();
                    }else {
                        Toast.makeText(PRecoveryActivity.this,R.string.internet_fail,Toast.LENGTH_LONG).show();
                    }
                }else {
                    EmailInput.setError(result);
                    EmailInput.requestFocus();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ServerRequest extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            URL url= null;
            recovery_error=0;
            try {
                url = new URL(RecoveryUrl);
                String response= Utilities.getResponseFromHttpUrl(url);
                return response;
            } catch (MalformedURLException e) {
                //e.printStackTrace();
                progressDialog.dismiss();
                recovery_error=1;
                return "";
            } catch (IOException e) {
                //e.printStackTrace();
                progressDialog.dismiss();
                recovery_error=2;
                return "";
            }

        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.dismiss();
            if (response.equals("true")){
                Toast.makeText(PRecoveryActivity.this,R.string.email_recovery,Toast.LENGTH_LONG).show();
                PRecoveryActivity.super.onBackPressed();
            } else if (response.equals("false")){
                Toast.makeText(PRecoveryActivity.this,R.string.email_free,Toast.LENGTH_LONG).show();
            }

            if (recovery_error!=0){
                Toast.makeText(PRecoveryActivity.this,R.string.server_fail,Toast.LENGTH_LONG).show();
            }
        }
    }

}
