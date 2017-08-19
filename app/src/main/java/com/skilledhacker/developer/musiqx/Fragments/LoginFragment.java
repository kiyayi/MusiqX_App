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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Database.DatabaseHandler;
import com.skilledhacker.developer.musiqx.MusicActivity;
import com.skilledhacker.developer.musiqx.R;
import com.skilledhacker.developer.musiqx.Utilities.NetworkChecker;
import com.skilledhacker.developer.musiqx.Utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Guy on 6/23/2017.
 */

public class LoginFragment  extends Fragment {
    private EditText UsernameInput;
    private EditText PasswordInput;
    private Button SignInButon;
    private String LoginUrl;
    private LinearLayout fragment;
    private ProgressDialog progressDialog;
    public static final String loginBroadcast="com.skilledhacker.developer.musiqx.login";
    public static final String urlBroadcast="com.skilledhacker.developer.musiqx.url";
    public static final String registration_success_broadcast="com.skilledhacker.developer.musiqx.registration.success";

    private DatabaseHandler database;
    private BroadcastReceiver registration_receiver;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_login, container, false);

        LoginUrl=getString(R.string.login_url);
        UsernameInput =(EditText)view.findViewById(R.id.login_username);
        PasswordInput=(EditText)view.findViewById(R.id.login_password);
        SignInButon=(Button)view.findViewById(R.id.sign_in_button);
        fragment=(LinearLayout) view.findViewById(R.id.fragment_login);
        progressDialog=new ProgressDialog(getActivity(),R.style.MyMaterialTheme);
        database=new DatabaseHandler(getActivity());

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
                Toast.makeText(getActivity(),R.string.server_fail,Toast.LENGTH_LONG).show();
            }
        };

        IntentFilter registrationFilter = new IntentFilter();
        registrationFilter.addAction(registration_success_broadcast);
        registration_receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Snackbar.make(fragment,R.string.email_sent_registration,Snackbar.LENGTH_INDEFINITE).show();
            }
        };

        getActivity().registerReceiver(loginReceiver,loginFilter);
        getActivity().registerReceiver(urlReceiver,urlFilter);
        getActivity().registerReceiver(registration_receiver,registrationFilter);

        SignInButon.setOnClickListener(login_onClickListener);

        return view;
    }

    private View.OnClickListener login_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (NetworkChecker.isConnected(getActivity())) {
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
                        Toast.makeText(getActivity(),R.string.random_error,Toast.LENGTH_LONG).show();
                    }
                    if (user.length()>0){
                        ServerRequest serverRequest=new ServerRequest();
                        serverRequest.execute(String.valueOf(user));

                    }
                }
            } else {
                Toast.makeText(getActivity(), R.string.internet_fail, Toast.LENGTH_LONG).show();
            }
        }
    };

    public class ServerRequest extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String response= Utilities.POSTRequest(LoginUrl,params[0]);
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
                intent.setAction(loginBroadcast);
                getActivity().sendBroadcast(intent);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.dismiss();
            if(response==null || response.isEmpty()){
                Intent intent=new Intent();
                intent.setAction(loginBroadcast);
                getActivity().sendBroadcast(intent);
            }else{
                try {
                    JSONObject user=new JSONObject(response);
                    String token=user.getString("token");
                    database.insert_account(1,token);
                    Intent i=new Intent(getActivity(),MusicActivity.class);
                    startActivity(i);
                    getActivity().finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Intent intent=new Intent();
                    intent.setAction(loginBroadcast);
                    getActivity().sendBroadcast(intent);
                }
            }
        }
    }
}
