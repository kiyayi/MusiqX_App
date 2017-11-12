package com.skilledhacker.developer.musiqx.Utilities;

import android.util.Log;

import com.skilledhacker.developer.musiqx.Models.Audio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Guy on 4/24/2017.
 */

public class Utilities {
    public static final String POST_REQUEST="POST";
    public static final String PUT_REQUEST="PUT";
    public static final String GET_REQUEST="GET";
    public static final String DELETE_REQUEST="DELETE";

    public static int RandomSong(ArrayList<Audio> AudioList) {
        Random rand = new Random();
        int randomInt = AudioList.get(rand.nextInt(AudioList.size())).getSong();
        return randomInt;
    }

    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public static int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }

    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = totalDuration / 1000;
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public  static String POSTRequest(String requestURL,String JSONData) throws IOException{
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String JsonResponse = "";

        try {
            URL url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            writer.write(JSONData);
            writer.flush();
            writer.close();
            InputStream inputStream = conn.getInputStream();

            /*int statusCode = conn.getResponseCode();
            Log.d("ERROR", "FFFF"+statusCode);*/

            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            JsonResponse = buffer.toString();
            return JsonResponse;
        }finally {
            conn.disconnect();
        }
    }

    public  static String put_post_request(String requestURL,String JSONData,String request,String token) throws IOException{
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String JsonResponse = "";

        try {
            URL url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);

            conn.setRequestMethod(request);
            conn.setRequestProperty("Authorization", "Token "+token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            writer.write(JSONData);
            writer.flush();
            writer.close();
            InputStream inputStream = conn.getInputStream();

            /*int statusCode = conn.getResponseCode();
            Log.d("ERROR", "FFFF"+statusCode);*/

            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            JsonResponse = buffer.toString();
            return JsonResponse;
        }finally {
            conn.disconnect();
        }
    }

    public  static String get_delete_request(String requestURL,String request,String token) throws IOException{
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String JsonResponse = "";

        try {
            URL url = new URL(requestURL);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(request);
            conn.setRequestProperty("Authorization", "Token "+token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            InputStream inputStream = conn.getInputStream();

            int statusCode = conn.getResponseCode();
            Log.d("ERROR", "FFFF"+statusCode);

            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            JsonResponse = buffer.toString();
            return JsonResponse;
        }finally {
            conn.disconnect();
        }
    }
}
