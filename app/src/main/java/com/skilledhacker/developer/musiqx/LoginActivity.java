package com.skilledhacker.developer.musiqx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


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

    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginUrl=getString(R.string.login_url);
        Log.d("ZZZ",LoginUrl);
        UsernameInput =(EditText)findViewById(R.id.login_username);
        PasswordInput=(EditText)findViewById(R.id.login_password);
        SignInButon=(Button)findViewById(R.id.sign_in_button);
        RecoveryButton=(TextView)findViewById(R.id.password_recovery);
        activity=(LinearLayout) findViewById(R.id.activity_login);
        progressDialog=new ProgressDialog(LoginActivity.this,R.style.MyMaterialTheme);

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
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage(getString(R.string.authenticate));
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
                String response= Utilitties.POSTRequest("https://musiqx.herokuapp.com/api/get_auth_token/",params[0]);
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                //Toast.makeText(LoginActivity.this,R.string.server_fail,Toast.LENGTH_LONG).show();
                Log.d("ERROR","OOOPS1");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Log.d("ERROR","OOOPS2");
                //Toast.makeText(LoginActivity.this,R.string.server_fail, Toast.LENGTH_LONG).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            //Log.d("AAA",response);
            Log.d("ERROR","HHHH");
            progressDialog.dismiss();
        }
    }
}
