package com.skilledhacker.developer.musiqx;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by 41EM on 02/06/2017.
 */

public class LoginActivity extends Activity implements View.OnClickListener{
    EditText email;
    EditText password;
    Button loginBtn;
    Button close_button;
    private User user;

    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        loginBtn= (Button) findViewById(R.id.loginBtn);
        close_button=(ImageView) findViewById(R.id.close_activity);
        email= (EditText) findViewById(R.id.login_emailId);
        password= (EditText) findViewById(R.id.login_password);

        loginBtn.setOnClickListener(this);
        userLocalStore= new UserLocalStore(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.loginBtn:

                String EmailId = email.getText().toString();
                String Password = password.getText().toString();
                User user = new User(EmailId,Password);
                authenticate(user);

                break;

        }
    }

    private void authenticate(User user) {

        ServerRequests serverrequests = new ServerRequests(this);
        serverrequests.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override

            public void done (User returnedUser){

                if(returnedUser == null) {

                showErrorMessage();

                } else{

                LogUserIn(returnedUser);

                }
            }
        });
    }


    private void showErrorMessage() {

        AlertDialog.Builder dialogbuilder= new AlertDialog.Builder (LoginActivity.this);
        dialogbuilder.setMessage("incorrect Email Id or Password, check and try again");
        dialogbuilder.setPositiveButton("Ok", null);
        dialogbuilder.show();
    }

    private void LogUserIn(User returnedUser) {
        userLocalStore.setUserLoggedIn(true);
        userLocalStore.storeUserData(returnedUser);
        AlertDialog.Builder dialogbuilder1= new AlertDialog.Builder(LoginActivity.this);
        dialogbuilder1.setMessage("hello Customer");
        dialogbuilder1.show();
    }
}
