package com.skilledhacker.developer.musiqx;


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
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    protected Button sign_up;
    protected EditText f_name;
    protected EditText l_name;
    protected AutoCompleteTextView country;
    protected EditText e_mail;
    protected EditText code;
    protected EditText code_c;
    protected CheckBox licence;

    protected String code_recup;


    protected boolean valid_mail = false;
    protected boolean valid_code = false;
    protected boolean valid_name1 = false;
    protected boolean valid_name2 = false;
    protected boolean valid_country = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        l_name = (EditText)findViewById(R.id.l_name);
        e_mail = (EditText)findViewById(R.id.mail);
        code = (EditText)findViewById(R.id.password);
        code_c = (EditText) findViewById(R.id.c_password);
        sign_up = (Button) findViewById(R.id.sign_2);
        f_name = (EditText)findViewById(R.id.f_name);
        licence = (CheckBox)findViewById(R.id.agree);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,COUNTRIES);
        country = (AutoCompleteTextView) findViewById(R.id.Auto_complete_country);
        country.setAdapter(adapter);

        l_name.addTextChangedListener(l_name_textWatcher);
        e_mail.addTextChangedListener(mail_textWatcher);
        code.addTextChangedListener(codeTextWatcher);
        code_c.addTextChangedListener(c_code_textWatcher);
        sign_up.setOnClickListener(sign_onClickListener);
        f_name.addTextChangedListener(f_name_textWatcher);
        country.addTextChangedListener(country_textWatcher);
    }

    protected boolean Verification_code(String string, int length_code_min, int length_code_max) {

        boolean majuscule = false;
        boolean miniscule = false;
        boolean carac_special = false;
        boolean number = false;

        char tab[] = string.toCharArray();

        if(string.length()<length_code_min){
            return false;
        }
        else if(string.length()>length_code_max){
            return false;
        }
        else {
            for (int i = 0; i < string.length(); i++) {

                if (tab[i] <= '!' || tab[i] >= '~') {
                    return false;
                }
                if (!majuscule) {
                    if (tab[i] <= 'Z' && tab[i] >= 'A') {
                        majuscule = true;
                    }
                }
                if (!miniscule) {
                    if (tab[i] <= 'z' && tab[i] >= 'a') {
                        miniscule = true;
                    }
                }

                if (!number) {
                    if (Character.isDigit(tab[i])) {
                        number = true;
                    }
                }

                if (!carac_special) {
                    if (!Character.isDigit(tab[i])) {
                        if (!(tab[i] <= 'z' && tab[i] >= 'a')) {
                            if (!(tab[i] <= 'Z' && tab[i] >= 'A')) {
                                carac_special = true;
                            }
                        }
                    }
                }

            }
            if(!miniscule || !majuscule || !number || !carac_special){
                return false;
            }
        }
        return true;
    }
    protected boolean Verification_mail(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    protected View.OnClickListener sign_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            boolean clickable = true;

            if((e_mail.getText().toString().isEmpty()) || (f_name.getText().toString().isEmpty()) || (l_name.getText().toString().isEmpty()) || (country.getText().toString().isEmpty()) || (code_c.getText().toString().isEmpty()) || (code.getText().toString().isEmpty())){
                Toast.makeText(RegistrationActivity.this,"Please complete all case",Toast.LENGTH_LONG).show();
                clickable = false;
            }
            if(!valid_mail){
                e_mail.setError("Your mail is not valid");
            }
            if(!inArrayList(COUNTRIES,country.toString())){
                country.setError("Your country name is not valid");
            }
            if(!valid_code){
                code_c.setError("The password confirmation is not valid");
            }
            if(!licence.isChecked() && clickable){
                Toast.makeText(RegistrationActivity.this,"Please read and confirm the licence",Toast.LENGTH_LONG).show();
            }
            if(valid_mail && valid_country && valid_code && valid_name1 && valid_name2 && licence.isChecked()){
                Toast.makeText(RegistrationActivity.this,"Please wait Apostolus is recording your personal data",Toast.LENGTH_LONG).show();
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
            if(s.toString().length()<=1){
                f_name.setError("Your first name is too short");
            }
            else if(s.toString().length()>50){
                f_name.setError("Your first name is too long");
            }
            else {
                valid_name1 = true;
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
            int LENGTH_CODE_MIN = 6;
            int LENGTH_CODE_MAX = 50;

            boolean vraie = Verification_code(s.toString(),LENGTH_CODE_MIN,LENGTH_CODE_MAX);
            if(vraie){
                code_recup = s.toString();
                code_c.setEnabled(true);
            }
            else{
                code.setError("The password must have at least one uppercase letter, lowercase letter, a special character and a number");
                code_c.setEnabled(false);
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
            if(s.toString().isEmpty()|| s.toString().length()<=1){
                l_name.setError("Your last name is too short");
            }
            else if(s.toString().length()>50){
                l_name.setError("Your last name is too long");
            }
            else {
                valid_name2 = true;
            }

        }
    };
    protected TextWatcher country_textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(inArrayList(COUNTRIES,country.toString())){
                valid_country = true;
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

            boolean true_mail = Verification_mail(s.toString());
            if(true_mail){
                valid_mail = true;
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

            if (s.toString().equals(code_recup)) {
                valid_code = true;
            }
        }
    };
    protected boolean inArrayList(String tab[],String string){

        for(int i=0;i<tab.length;i++){
            if(tab[i].equals(string)){
                return true;
            }
        }

        return false;
    }

    private static final String[] COUNTRIES = new String[] {"Burundi","Rwanda","Congo"};
}
