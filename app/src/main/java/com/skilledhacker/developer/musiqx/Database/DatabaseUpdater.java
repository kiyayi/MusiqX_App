package com.skilledhacker.developer.musiqx.Database;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.skilledhacker.developer.musiqx.Player.Audio;
import com.skilledhacker.developer.musiqx.Utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import static com.skilledhacker.developer.musiqx.R.string.email;

/**
 * Created by Guy on 7/16/2017.
 */

public class DatabaseUpdater {
    private DatabaseHandler database;
    private Context ctx;
    private int update_error=0;

    private String library_sync_url="";

    public static final String SYNC_LIBRARY_BROADCAST="com.skilledhacker.developer.musiqx.database.sync.library";

    public DatabaseUpdater(Context context) {
        database = new DatabaseHandler(context);
        library_sync_url="";
        ctx=context;
    }

    public void SyncLibrary(){
        ArrayList<Audio> library_list=database.retrieve_added_deleted_songs();
        String token=database.retrieve_account_token();
        JSONArray jsonArray=new JSONArray();

        try{
            jsonArray=library_list_to_json(library_list);
        }catch (JSONException e){
            e.printStackTrace();
            update_error=1;
        }

        if (jsonArray.length()>0){
            ServerRequest serverRequest=new ServerRequest();
            serverRequest.execute(String.valueOf(jsonArray),"POST",token);

        }
    }

    public JSONArray library_list_to_json(ArrayList<Audio> list) throws JSONException{
        JSONArray array=new JSONArray();
        int i=0;
        int size=list.size();

        for (i=0;i<size;i++){
            JSONObject object=new JSONObject();
            object.put("song",list.get(i).getSong());
            object.put("updated_at",list.get(i).getUpdated_at());
            object.put("status",list.get(i).getStatus());

            array.put(object);
        }

        return array;
    }

    public class ServerRequest extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String response= Utilities.put_post_request(library_sync_url,params[0],params[1],params[2]);
                return response;
            } catch (MalformedURLException e) {
                //e.printStackTrace();
                update_error=2;
                return null;
            } catch (IOException e) {
                //e.printStackTrace();
                update_error=3;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if(response==null || response.isEmpty()){
                Intent intent=new Intent();
                intent.setAction(registrationBroadcast);
                sendBroadcast(intent);
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
                        Verification.isUsernameFree=-1;
                        Verification.isEmailFree=-1;
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
