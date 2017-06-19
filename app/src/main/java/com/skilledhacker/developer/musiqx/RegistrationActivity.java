package com.skilledhacker.developer.musiqx;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Utilities.NetworkChecker;
import com.skilledhacker.developer.musiqx.Utilities.Utilities;
import com.skilledhacker.developer.musiqx.Utilities.Verification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;


public class RegistrationActivity extends AppCompatActivity {
    private Button sign_up;
    private EditText f_name;
    private EditText l_name;
    private EditText username;
    private EditText e_mail;
    private EditText code;
    private EditText code_c;
    private CheckBox ConditionsCheckBox;
    private RelativeLayout activity;
    private ProgressDialog progressDialog;
    private String RegistrationUrl;

    private boolean valid_mail = false;
    private boolean valid_code = false;
    private boolean code_match = false;
    private boolean valid_name1 = false;
    private boolean valid_name2 = false;
    private boolean valid_username = false;
    private boolean registration_ready=false;

    public static final String urlBroadcast="com.skilledhacker.developer.musiqx.regurl";
    public static final String registrationBroadcast="com.skilledhacker.developer.musiqx.registration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        RegistrationUrl=getString(R.string.registration_url);
        l_name = (EditText)findViewById(R.id.l_name);
        e_mail = (EditText)findViewById(R.id.mail);
        code = (EditText)findViewById(R.id.password);
        code_c = (EditText) findViewById(R.id.c_password);
        sign_up = (Button) findViewById(R.id.sign_2);
        f_name = (EditText)findViewById(R.id.f_name);
        ConditionsCheckBox=(CheckBox)findViewById(R.id.agree);
        activity=(RelativeLayout)findViewById(R.id.activity_registration);
        username = (EditText) findViewById(R.id.username);
        progressDialog=new ProgressDialog(RegistrationActivity.this,R.style.MyMaterialTheme);
        BroadcastReceiver emailCheckReceiver;
        BroadcastReceiver usernameCheckReceiver;
        BroadcastReceiver registrationReceiver;
        BroadcastReceiver urlReceiver;

        IntentFilter registrationFilter = new IntentFilter();
        registrationFilter.addAction(registrationBroadcast);
        registrationReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Snackbar.make(activity,R.string.registration_error,Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registerUser();
                    }
                }).show();
            }
        };

        IntentFilter urlFilter = new IntentFilter();
        urlFilter.addAction(urlBroadcast);
        urlReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(RegistrationActivity.this,R.string.server_fail,Toast.LENGTH_LONG).show();
            }
        };

        IntentFilter emailFilter = new IntentFilter();
        emailFilter.addAction(Verification.emailCheckBroadcast);
        emailCheckReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (Verification.isEmailFree==0){
                    e_mail.setError(getString(R.string.email_registered));
                    valid_mail=false;
                }else if (Verification.isEmailFree==-1){
                    Snackbar.make(activity,R.string.email_check_error, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    valid_mail=false;
                }else {
                    e_mail.setError(null);
                    valid_mail=true;
                    if (registration_ready && valid_username && valid_mail && valid_code && valid_name1 && valid_name2){
                        registerUser();
                    }
                }
            }
        };

        IntentFilter usernameFilter = new IntentFilter();
        usernameFilter.addAction(Verification.usernameCheckBroadcast);
        usernameCheckReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (progressDialog.isShowing()) progressDialog.dismiss();
                if (Verification.isUsernameFree==0){
                    username.setError(getString(R.string.username_registered));
                    valid_username=false;
                }else if (Verification.isUsernameFree==-1){
                    Snackbar.make(activity,R.string.username_check_error, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    valid_username=false;
                }else {
                    username.setError(null);
                    valid_username=true;
                    if (registration_ready && valid_username && valid_mail && valid_code && valid_name1 && valid_name2){
                        registerUser();
                    }
                }
            }
        };

        registerReceiver(emailCheckReceiver,emailFilter);
        registerReceiver(usernameCheckReceiver,usernameFilter);
        registerReceiver(registrationReceiver,registrationFilter);
        registerReceiver(urlReceiver,urlFilter);

        l_name.addTextChangedListener(l_name_textWatcher);
        e_mail.addTextChangedListener(mail_textWatcher);
        code.addTextChangedListener(codeTextWatcher);
        code_c.addTextChangedListener(c_code_textWatcher);
        sign_up.setOnClickListener(sign_onClickListener);
        f_name.addTextChangedListener(f_name_textWatcher);
        username.addTextChangedListener(username_textWatcher);
    }

    protected View.OnClickListener sign_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String condition_result=Verification.condition_check(ConditionsCheckBox.isChecked(),RegistrationActivity.this);
            if (!valid_name1) f_name.requestFocus();
            else if (!valid_name2) l_name.requestFocus();
            else if (!valid_username) username.requestFocus();
            else if (!valid_mail) e_mail.requestFocus();
            else if (!valid_code) code.requestFocus();
            else if (!code_match) code_c.requestFocus();
            else if (condition_result.equals("")){
                Snackbar.make(activity,condition_result, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else{
                if (NetworkChecker.isConnected(RegistrationActivity.this)){
                    registration_ready=true;
                    registerUser();

                }else {
                    Toast.makeText(RegistrationActivity.this,R.string.internet_fail, Toast.LENGTH_LONG).show();
                }
            }
        }

    };
    protected TextWatcher f_name_textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            f_name.setError(null);
            String result= Verification.fname_check(f_name.getText().toString(),RegistrationActivity.this);
            if (result.equals("")){
                valid_name1=true;
            }else {
                f_name.setError(result);
                valid_name1=false;
            }

        }
    };
    protected TextWatcher codeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            code.setError(null);
            String result= Verification.password_check(code.getText().toString(),RegistrationActivity.this);
            if (result.equals("")){
                valid_code=true;
            }else {
                code.setError(result);
                valid_code=false;
            }


        }
    };
    protected TextWatcher l_name_textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            l_name.setError(null);
            String result= Verification.lname_check(l_name.getText().toString(),RegistrationActivity.this);
            if (result.equals("")){
                valid_name2=true;
            }else {
                l_name.setError(result);
                valid_name2=false;
            }

        }
    };
    protected TextWatcher username_textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            username.setError(null);
            String result= Verification.username_check(username.getText().toString(),RegistrationActivity.this);
            if (result.equals("")){
                valid_username=true;
            }else {
                username.setError(result);
                valid_username=false;
            }

        }
    };
    protected TextWatcher mail_textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            e_mail.setError(null);
            String result= Verification.email_check(e_mail.getText().toString(),RegistrationActivity.this);
            if (result.equals("")){
                valid_mail=true;
            }else {
                e_mail.setError(result);
                valid_mail=false;
            }

        }
    };
    protected TextWatcher c_code_textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            code_c.setError(null);
            String result= Verification.cPassword_check(code.getText().toString(),code_c.getText().toString(),
                    RegistrationActivity.this);
            if (result.equals("")){
                code_match=true;
            }else {
                code_c.setError(result);
                code_match=false;
            }
        }


    };

    private void registerUser(){
        progressDialog.setMessage(getString(R.string.authenticate));
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        if (Verification.isEmailFree==-1){
            Verification.isEmailTaken(e_mail.getText().toString(),RegistrationActivity.this);
        }else if (Verification.isUsernameFree==-1){
            Verification.isUsernameTaken(username.getText().toString(),RegistrationActivity.this);
        }else{
            String fname=f_name.getText().toString();
            String lname=l_name.getText().toString();
            String email=e_mail.getText().toString();
            String password=code.getText().toString();
            String user_name=username.getText().toString();

            l_name.setError(null);
            e_mail.setError(null);
            code.setError(null);
            code_c.setError(null);
            f_name.setError(null);
            username.setError(null);

            JSONObject user=new JSONObject();
            try{
                user.put("username" , user_name);
                user.put("password", password);
                user.put("email", email);
                user.put("first_name", fname);
                user.put("last_name", lname);
            }catch (JSONException e){
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(RegistrationActivity.this,R.string.random_error,Toast.LENGTH_LONG).show();
            }
            if (user.length()>0){
                ServerRequest serverRequest=new ServerRequest();
                serverRequest.execute(String.valueOf(user));

            }

        }
    }

    public class ServerRequest extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String response= Utilities.POSTRequest(RegistrationUrl,params[0]);
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
                intent.setAction(registrationBroadcast);
                sendBroadcast(intent);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.dismiss();
            if(response==null || response.isEmpty()){
                Intent intent=new Intent();
                intent.setAction(registrationBroadcast);
                sendBroadcast(intent);
            }else{
                try {
                    JSONObject user=new JSONObject(response);
                    String user_name=user.getString("username");
                    String email=user.getString("email");
                    Log.d("AA",user_name+"-----"+email);
                    if (user_name==username.getText().toString() && email==e_mail.getText().toString()){
                        Intent i=new Intent(RegistrationActivity.this,LoginActivity.class);
                        Toast.makeText(RegistrationActivity.this,R.string.registration_success,Toast.LENGTH_LONG).show();
                        startActivity(i);
                        finish();
                    }else {
                        Intent intent=new Intent();
                        intent.setAction(registrationBroadcast);
                        sendBroadcast(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Intent intent=new Intent();
                    intent.setAction(registrationBroadcast);
                    sendBroadcast(intent);
                }
            }
        }
    }
}
