package com.skilledhacker.developer.musiqx.Utilities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.skilledhacker.developer.musiqx.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Guy on 6/2/2017.
 */

public class Verification {
    private static String emailCheckUrl="PUT HERE URL TO SERVER";
    private static String usernameCheckUrl="PUT HERE URL TO SERVER";
    public static final String emailCheckBroadcast="com.skilledhacker.developer.musiqx.email";
    public static final String usernameCheckBroadcast="com.skilledhacker.developer.musiqx.username";
    private static Context ctx;
    public static short isEmailFree=-1;
    public static short isUsernameFree=-1;

    public static String email_check(String email, Context ctx){
        String result="";
        email=email.trim();
        if (email==null || email.isEmpty()){
            result= ctx.getString(R.string.email_empty);
        }else if (!isEmailValid(email)){
            result= ctx.getString(R.string.email_invalid);
        }else {
            isEmailTaken(email,ctx);
        }

        return result;
    }

    public static  void isEmailTaken(String email,Context context){
        ctx=context;
        emailCheckUrl+="/ADD EMAIL";
        EmailCheck emailCheck=new EmailCheck();
        emailCheck.execute();
    }

    public static String fname_check(String fname,Context ctx){
        String result="";
        fname=fname.trim();
        if (fname==null || fname.isEmpty()){
            result= ctx.getString(R.string.fname_empty);
        }else if (fname.length()<2){
            result= ctx.getString(R.string.fname_short);
        }else if (fname.length()>50){
            result= ctx.getString(R.string.fname_long);
        }

        return result;
    }

    public static String lname_check(String lname,Context ctx){
        String result="";
        lname=lname.trim();
        if (lname==null || lname.isEmpty()){
            result= ctx.getString(R.string.lname_empty);
        }else if (lname.length()<2){
            result= ctx.getString(R.string.lname_short);
        }else if (lname.length()>50){
            result= ctx.getString(R.string.lname_long);
        }

        return result;
    }

    public static String password_check(String password,Context ctx){
        String result="";
        if (password==null || password.isEmpty()){
            result= ctx.getString(R.string.password_empty);
        }else if (password.length()>50){
            result= ctx.getString(R.string.password_long);
        }else if (password.length()<6){
            result= ctx.getString(R.string.password_short);
        }else if (!password_char_check(password)){
            result= ctx.getString(R.string.password_condition);
        }

        return result;
    }

    public static String cPassword_check(String password,String cpassword,Context ctx){
        String result="";
        if (!password.equals(cpassword)) result= ctx.getString(R.string.password_match);

        return result;
    }

    public static String username_check(String username,Context ctx){
        String result="";
        username=username.trim();
        if (username==null || username.isEmpty()){
            result= ctx.getString(R.string.uname_empty);
        }else if (username.length()<2){
            result= ctx.getString(R.string.uname_short);
        }else if (username.length()>50){
            result= ctx.getString(R.string.uname_long);
        }else {
            isUsernameTaken(username,ctx);
        }

        return result;
    }

    public static  void isUsernameTaken(String username,Context context){
        ctx=context;
        usernameCheckUrl+="/ADD USERNAME";
        UsernameCheck usernameCheck=new UsernameCheck();
        usernameCheck.execute();
    }

    public static String condition_check(boolean condition,Context ctx){
        String result="";
        if (!condition) result= ctx.getString(R.string.condition_check);

        return result;
    }

    //PRIVATE FUNCTIONS

    private static boolean inArrayList(String tab[],String string){

        for(int i=0;i<tab.length;i++){
            if(tab[i].equals(string)){
                return true;
            }
        }

        return false;
    }

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static class EmailCheck extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            URL url= null;
            try {
                url = new URL(emailCheckUrl);
                String response= Utilities.getResponseFromHttpUrl(url);
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(ctx,R.string.server_fail,Toast.LENGTH_LONG).show();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(ctx,R.string.server_fail, Toast.LENGTH_LONG).show();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response) {
            if (response=="yes") isEmailFree=0;
            else if (response=="no") isEmailFree=1;

            Intent intent=new Intent();
            intent.setAction(emailCheckBroadcast);
            ctx.sendBroadcast(intent);

        }
    }

    private static class UsernameCheck extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            URL url= null;
            try {
                url = new URL(usernameCheckUrl);
                String response= Utilities.getResponseFromHttpUrl(url);
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(ctx,R.string.server_fail,Toast.LENGTH_LONG).show();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(ctx,R.string.server_fail, Toast.LENGTH_LONG).show();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response) {
            if (response=="yes") isUsernameFree=0;
            else if (response=="no") isUsernameFree=1;

            Intent intent=new Intent();
            intent.setAction(usernameCheckBroadcast);
            ctx.sendBroadcast(intent);

        }
    }

    private static boolean password_char_check(String password){
        boolean majuscule = false;
        boolean miniscule = false;
        boolean carac_special = false;
        boolean number = false;
        char tab[] = password.toCharArray();

        for (int i = 0; i < password.length(); i++) {

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

        return true;
    }
}
