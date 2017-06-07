package com.skilledhacker.developer.musiqx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Utilities.NetworkChecker;
import com.skilledhacker.developer.musiqx.Utilities.Verification;


public class RegistrationActivity extends AppCompatActivity {
    protected Button sign_up;
    protected EditText f_name;
    protected EditText l_name;
    protected EditText username;
    protected EditText e_mail;
    protected EditText code;
    protected EditText code_c;
    private CheckBox ConditionsCheckBox;
    private RelativeLayout activity;

    protected boolean valid_mail = false;
    protected boolean valid_code = false;
    protected boolean code_match = false;
    protected boolean valid_name1 = false;
    protected boolean valid_name2 = false;
    protected boolean valid_username = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        BroadcastReceiver emailCheckReceiver;
        BroadcastReceiver usernameCheckReceiver;
        l_name = (EditText)findViewById(R.id.l_name);
        e_mail = (EditText)findViewById(R.id.mail);
        code = (EditText)findViewById(R.id.password);
        code_c = (EditText) findViewById(R.id.c_password);
        sign_up = (Button) findViewById(R.id.sign_2);
        f_name = (EditText)findViewById(R.id.f_name);
        ConditionsCheckBox=(CheckBox)findViewById(R.id.agree);
        activity=(RelativeLayout)findViewById(R.id.activity_registration);
        username = (EditText) findViewById(R.id.username);

        IntentFilter emailFilter = new IntentFilter();
        emailFilter.addAction(Verification.emailCheckBroadcast);
        emailCheckReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
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
                }
            }
        };

        IntentFilter usernameFilter = new IntentFilter();
        usernameFilter.addAction(Verification.usernameCheckBroadcast);
        usernameCheckReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
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
                }
            }
        };

        registerReceiver(emailCheckReceiver,emailFilter);
        registerReceiver(usernameCheckReceiver,usernameFilter);

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
                    if (Verification.isEmailFree==-1){

                    }else if (Verification.isUsernameFree==-1){

                    }else{

                    }

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
}