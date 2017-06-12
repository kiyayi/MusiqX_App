package com.skilledhacker.developer.musiqx;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.Utilities.NetworkChecker;
import com.skilledhacker.developer.musiqx.Utilities.Utilitties;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;


/**
 * Created by 41EM on 02/06/2017.
 */

public class LoginActivity extends AppCompatActivity{
    private EditText UsernameInput;
    private EditText PasswordInput;
    private Button SignInButon;
    private TextView RecoveryButton;
    private String LoginUrl;
    private LinearLayout activity;
    private ProgressDialog progressDialog;
    public static final String loginBroadcast="com.skilledhacker.developer.musiqx.login";
    public static final String urlBroadcast="com.skilledhacker.developer.musiqx.url";

    private DatabaseHandler database;

    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginUrl=getString(R.string.login_url);
        UsernameInput =(EditText)findViewById(R.id.login_username);
        PasswordInput=(EditText)findViewById(R.id.login_password);
        SignInButon=(Button)findViewById(R.id.sign_in_button);
        RecoveryButton=(TextView)findViewById(R.id.password_recovery);
        activity=(LinearLayout) findViewById(R.id.activity_login);
        progressDialog=new ProgressDialog(LoginActivity.this,R.style.MyMaterialTheme);
        database=new DatabaseHandler(LoginActivity.this);

        BroadcastReceiver loginReceiver;
        IntentFilter loginFilter = new IntentFilter();
        loginFilter.addAction(loginBroadcast);
        loginReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                UsernameInput.setError(getString(R.string.login_error));
                PasswordInput.setError(getString(R.string.login_error));
                UsernameInput.requestFocus();
            }
        };

        BroadcastReceiver urlReceiver;
        IntentFilter urlFilter = new IntentFilter();
        urlFilter.addAction(urlBroadcast);
        urlReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(LoginActivity.this,R.string.server_fail,Toast.LENGTH_LONG).show();
            }
        };

        registerReceiver(loginReceiver,loginFilter);
        registerReceiver(urlReceiver,urlFilter);

        SignInButon.setOnClickListener(login_onClickListener);
        RecoveryButton.setOnClickListener(recovery_onClickListener);


    }

    private View.OnClickListener login_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (NetworkChecker.isConnected(LoginActivity.this)) {
                String username=UsernameInput.getText().toString();
                String password=PasswordInput.getText().toString();

                if (username==null||username.isEmpty() ){
                    UsernameInput.setError(getString(R.string.uname_empty));
                    UsernameInput.requestFocus();
                }else if (password==null||password.isEmpty()){
                    PasswordInput.setError(getString(R.string.password_empty));
                    PasswordInput.requestFocus();
                }else{
                    progressDialog.setMessage(getString(R.string.authenticate));
                    progressDialog.getWindow().setGravity(Gravity.CENTER);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();

                    UsernameInput.setError(null);
                    PasswordInput.setError(null);

                    JSONObject user=new JSONObject();
                    try{
                        user.put("username" , username);
                        user.put("password", password);
                    }catch (JSONException e){
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,R.string.random_error,Toast.LENGTH_LONG).show();
                    }
                    if (user.length()>0){
                        ServerRequest serverRequest=new ServerRequest();
                        serverRequest.execute(String.valueOf(user));

                    }
                }
            } else {
                Toast.makeText(LoginActivity.this, R.string.internet_fail, Toast.LENGTH_LONG).show();
            }
        }
    };

    private View.OnClickListener recovery_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i=new Intent(LoginActivity.this,PRecoveryActivity.class);
            startActivity(i);
        }
    };

    public class ServerRequest extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String response= Utilitties.POSTRequest(LoginUrl,params[0]);
                return response;
            } catch (MalformedURLException e) {
                //e.printStackTrace();
                progressDialog.dismiss();
                Intent intent=new Intent();
                intent.setAction(urlBroadcast);
                sendBroadcast(intent);
                return null;
            } catch (IOException e) {
                //e.printStackTrace();
                progressDialog.dismiss();
                Intent intent=new Intent();
                intent.setAction(loginBroadcast);
                sendBroadcast(intent);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.dismiss();
            if(response==null || response.isEmpty()){
                Intent intent=new Intent();
                intent.setAction(loginBroadcast);
                sendBroadcast(intent);
            }else{
                try {
                    JSONObject user=new JSONObject(response);
                    String token=user.getString("token");
                    database.insert_account(1,token);
                    Intent i=new Intent(LoginActivity.this,MusicActivity.class);
                    startActivity(i);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this,R.string.random_error,Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
