package com.skilledhacker.developer.musiqx;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private final int LENGTH_CODE_MIN = 6;
    private final int LENGTH_CODE_MAX = 50;


    protected Button sign_up;
    protected EditText f_name;
    protected EditText l_name;
    protected AutoCompleteTextView country;
    protected EditText e_mail;
    protected EditText code;
    protected EditText code_c;

    protected MyEditText error_f_name;
    protected MyEditText error_l_name;
    protected MyEditText error_country;
    protected MyEditText error_mail;
    protected MyEditText error_password;
    protected MyEditText error_c_password;

    protected String code_recup;
    protected Drawable errorIcon;

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,COUNTRIES);
        country = (AutoCompleteTextView) findViewById(R.id.Auto_complete_country);
        country.setAdapter(adapter);

        error_f_name = (MyEditText)findViewById(R.id.error_f_name);
        error_l_name = (MyEditText)findViewById(R.id.error_l_name);
        error_country = (MyEditText)findViewById(R.id.error_country);
        error_password = (MyEditText)findViewById(R.id.error_password);
        error_c_password = (MyEditText)findViewById(R.id.error_c_password);
        error_mail = (MyEditText)findViewById(R.id.error_mail);


        errorIcon = getResources().getDrawable(R.drawable.ic_error_outline_black_24dp);
        errorIcon.setBounds(new Rect(0,0,errorIcon.getIntrinsicWidth(),errorIcon.getIntrinsicHeight()));



        error_f_name.setError(null,errorIcon);
        error_l_name.setError(null,errorIcon);
        error_country.setError(null,errorIcon);
        error_mail.setError(null,errorIcon);
        error_password.setError(null,errorIcon);
        error_c_password.setError(null,errorIcon);



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
                error_f_name.setError("",errorIcon);
            }
            else {
                error_f_name.setError("",null);
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

            boolean vraie = Verification_code(s.toString(),LENGTH_CODE_MIN,LENGTH_CODE_MAX);
            if(vraie){
                error_password.setError("",null);
                code_recup = s.toString();
                code_c.setEnabled(true);
            }
            else{
                error_password.setError("",errorIcon);
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
                error_l_name.setError("",errorIcon);
            }
            else {
                error_l_name.setError("",null);
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
            if(inArrayList(COUNTRIES,s.toString())){
                error_country.setError("",null);
                valid_country = true;
            }
            else{
                error_country.setError("",errorIcon);
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
                error_mail.setError("",null);
                valid_mail = true;
            }
            else{
                error_mail.setError("",errorIcon);
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
                error_c_password.setError("", null);
                valid_code = true;
            }
            else{
                error_c_password.setError("", errorIcon);
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
