package com.skilledhacker.developer.musiqx;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.Utilities.NetworkChecker;
import com.skilledhacker.developer.musiqx.Utilities.Verification;


/**
 * Created by 41EM on 02/06/2017.
 */

public class LoginActivity extends AppCompatActivity{
    private EditText EmailInput;
    private EditText PasswordInput;
    private Button SignInButon;
    private TextView RecoveryButton;
    private final String LoginUrl="";
    private LinearLayout activity;

    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailInput=(EditText)findViewById(R.id.email);
        PasswordInput=(EditText)findViewById(R.id.password);
        SignInButon=(Button)findViewById(R.id.sign_in_button);
        RecoveryButton=(TextView)findViewById(R.id.password_recovery);
        activity=(LinearLayout) findViewById(R.id.activity_login);

        SignInButon.setOnClickListener(login_onClickListener);
        RecoveryButton.setOnClickListener(recovery_onClickListener);


    }

    private View.OnClickListener login_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (NetworkChecker.isConnected(LoginActivity.this)) {
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                        R.style.MyMaterialTheme);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                progressDialog.dismiss();

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
}
