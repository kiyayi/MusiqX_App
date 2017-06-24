package com.skilledhacker.developer.musiqx.Fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.IdentificationActivty;
import com.skilledhacker.developer.musiqx.R;
import com.skilledhacker.developer.musiqx.Utilities.NetworkChecker;
import com.skilledhacker.developer.musiqx.Utilities.Utilities;
import com.skilledhacker.developer.musiqx.Utilities.Verification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Guy on 6/23/2017.
 */

public class RegistratonFragment extends Fragment {
    private Button sign_up;
    private EditText f_name;
    private EditText l_name;
    private EditText username;
    private EditText e_mail;
    private EditText code;
    private EditText code_c;
    private CheckBox ConditionsCheckBox;
    private RelativeLayout fragment;
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

    public RegistratonFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_registration, container, false);

        RegistrationUrl=getString(R.string.registration_url);
        l_name = (EditText)view.findViewById(R.id.l_name);
        e_mail = (EditText)view.findViewById(R.id.mail);
        code = (EditText)view.findViewById(R.id.password);
        code_c = (EditText) view.findViewById(R.id.c_password);
        sign_up = (Button) view.findViewById(R.id.sign_2);
        f_name = (EditText)view.findViewById(R.id.f_name);
        ConditionsCheckBox=(CheckBox)view.findViewById(R.id.agree);
        fragment=(RelativeLayout)view.findViewById(R.id.fragment_registration);
        username = (EditText) view.findViewById(R.id.username);
        progressDialog=new ProgressDialog(getActivity(),R.style.MyMaterialTheme);
        BroadcastReceiver emailCheckReceiver;
        BroadcastReceiver usernameCheckReceiver;
        BroadcastReceiver registrationReceiver;
        BroadcastReceiver urlReceiver;

        IntentFilter registrationFilter = new IntentFilter();
        registrationFilter.addAction(registrationBroadcast);
        registrationReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Snackbar.make(fragment,R.string.registration_error,Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
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
                Toast.makeText(getActivity(),R.string.server_fail,Toast.LENGTH_LONG).show();
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
                    e_mail.setError(getString(R.string.email_check_error));
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
                    username.setError(getString(R.string.username_check_error));
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

        getActivity().registerReceiver(emailCheckReceiver,emailFilter);
        getActivity().registerReceiver(usernameCheckReceiver,usernameFilter);
        getActivity().registerReceiver(registrationReceiver,registrationFilter);
        getActivity().registerReceiver(urlReceiver,urlFilter);

        f_name.setOnFocusChangeListener(f_name_watcher);
        l_name.setOnFocusChangeListener(l_name_watcher);
        username.setOnFocusChangeListener(username_watcher);
        e_mail.setOnFocusChangeListener(email_watcher);
        code.setOnFocusChangeListener(code_watcher);
        code_c.setOnFocusChangeListener(code_c_watcher);
        sign_up.setOnClickListener(sign_onClickListener);

        return view;
    }

    protected View.OnClickListener sign_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            f_name.clearFocus();
            l_name.clearFocus();
            username.clearFocus();
            e_mail.clearFocus();
            code.clearFocus();
            code_c.clearFocus();
            String condition_result=Verification.condition_check(ConditionsCheckBox.isChecked(),getActivity());
            if (!valid_name1) f_name.requestFocus();
            else if (!valid_name2) l_name.requestFocus();
            else if (!valid_username) username.requestFocus();
            else if (!valid_mail) e_mail.requestFocus();
            else if (!valid_code) code.requestFocus();
            else if (!code_match) code_c.requestFocus();
            else if (!condition_result.equals("")){
                Snackbar.make(fragment,condition_result, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else{
                if (NetworkChecker.isConnected(getActivity())){
                    registration_ready=true;
                    registerUser();

                }else {
                    Toast.makeText(getActivity(),R.string.internet_fail, Toast.LENGTH_LONG).show();
                }
            }
        }

    };

    private View.OnFocusChangeListener f_name_watcher=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                f_name.setError(null);
                String result= Verification.fname_check(f_name.getText().toString(),getActivity());
                if (result.equals("")){
                    valid_name1=true;
                }else {
                    f_name.setError(result);
                    valid_name1=false;
                }
            }
        }
    };
    private View.OnFocusChangeListener l_name_watcher=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                l_name.setError(null);
                String result= Verification.lname_check(l_name.getText().toString(),getActivity());
                if (result.equals("")){
                    valid_name2=true;
                }else {
                    l_name.setError(result);
                    valid_name2=false;
                }
            }
        }
    };
    private View.OnFocusChangeListener username_watcher=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                username.setError(null);
                String result= Verification.username_check(username.getText().toString(),getActivity());
                if (result.equals("")){
                    valid_username=true;
                }else {
                    username.setError(result);
                    valid_username=false;
                }
            }
        }
    };
    private View.OnFocusChangeListener email_watcher=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                e_mail.setError(null);
                String result= Verification.email_check(e_mail.getText().toString(),getActivity(),true);
                if (result.equals("")){
                    valid_mail=true;
                }else {
                    e_mail.setError(result);
                    valid_mail=false;
                }
            }
        }
    };
    private View.OnFocusChangeListener code_watcher=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                code.setError(null);
                String result= Verification.password_check(code.getText().toString(),getActivity());
                if (result.equals("")){
                    valid_code=true;
                }else {
                    code.setError(result);
                    valid_code=false;
                }
            }
        }
    };
    private View.OnFocusChangeListener code_c_watcher=new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                code_c.setError(null);
                String result= Verification.cPassword_check(code.getText().toString(),code_c.getText().toString(),
                        getActivity());
                if (result.equals("")){
                    code_match=true;
                }else {
                    code_c.setError(result);
                    code_match=false;
                }
            }
        }
    };

    private void registerUser(){
        progressDialog.setMessage(getString(R.string.authenticate));
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        if (Verification.isEmailFree==-1){
            Verification.isEmailTaken(e_mail.getText().toString(),getActivity());
        }else if (Verification.isUsernameFree==-1){
            Verification.isUsernameTaken(username.getText().toString(),getActivity());
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
                Toast.makeText(getActivity(),R.string.random_error,Toast.LENGTH_LONG).show();
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
                getActivity().sendBroadcast(intent);
                return null;
            } catch (IOException e) {
                //e.printStackTrace();
                progressDialog.dismiss();
                Intent intent=new Intent();
                intent.setAction(registrationBroadcast);
                getActivity().sendBroadcast(intent);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.dismiss();
            if(response==null || response.isEmpty()){
                Intent intent=new Intent();
                intent.setAction(registrationBroadcast);
                getActivity().sendBroadcast(intent);
            }else{
                try {
                    JSONObject user=new JSONObject(response);
                    String user_name=user.getString("username");
                    String email=user.getString("email");
                    if (user_name.equals(username.getText().toString()) && email.equals(e_mail.getText().toString())){
                        Toast.makeText(getActivity(),R.string.registration_success,Toast.LENGTH_LONG).show();
                        f_name.setText(null);
                        l_name.setText(null);
                        username.setText(null);
                        code.setText(null);
                        code_c.setText(null);
                        e_mail.setText(null);

                        l_name.setError(null);
                        e_mail.setError(null);
                        code.setError(null);
                        code_c.setError(null);
                        f_name.setError(null);
                        username.setError(null);
                        ConditionsCheckBox.setChecked(false);

                        valid_mail = false;
                        valid_code = false;
                        code_match = false;
                        valid_name1 = false;
                        valid_name2 = false;
                        valid_username = false;
                        registration_ready=false;
                        ((IdentificationActivty)getActivity()).selectFragment(0);
                    }else {
                        Intent intent=new Intent();
                        intent.setAction(registrationBroadcast);
                        getActivity().sendBroadcast(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Intent intent=new Intent();
                    intent.setAction(registrationBroadcast);
                    getActivity().sendBroadcast(intent);
                }
            }
        }
    }
}
