package com.example.kevinlay.safezone;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kevinlay on 4/12/17.
 */

public class GetCord extends AsyncTask<String,Void,ArrayList<SafeZoneInfo>> {
    public MainActivity activity;
    String s2;
    TextView t2;
    public GetCord(MainActivity activity) {
        this.activity = activity;
    }


    protected ArrayList<SafeZoneInfo> doInBackground(String[] params) {
        URL url;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        JSONObject parentObject = null;
        JSONArray breaker = null;
        JSONArray breaker1 = null;
        ArrayList<SafeZoneInfo> list1 = new ArrayList<SafeZoneInfo>();

        try {
            url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String finalJson = buffer.toString();
            s2 = finalJson;
            parentObject = new JSONObject(finalJson);
            breaker = (JSONArray) parentObject.get("results");
            breaker1 = (JSONArray) parentObject.get("results");
            JSONObject j3 = breaker.getJSONObject(0);

           // j3 = j3.getJSONObject("geometry");
            //j3 = j3.getJSONObject("location");

            //list1.add(j3.toString());

            //list1.add(j3.get("lat").toString());




            //j4 = j4.getJSONObject("icon");
            //String s2 = j4.getString("name");
            //list1.add(s2);

            for(int i =0; i<breaker.length(); i++){

                //list1.add(i+"");
                JSONObject j2 = breaker.getJSONObject(i);
                JSONObject j4 = breaker1.getJSONObject(i);
                j2 = j2.getJSONObject("geometry");
                j2 = j2.getJSONObject("location");
               // JSONObject j3 = breaker.getJSONObject(i);

                String lat = j2.get("lat").toString();
                String lng = j2.get("lng").toString();
                String name = j4.get("name").toString();
                SafeZoneInfo s2 = new SafeZoneInfo(lat,lng,name);
                list1.add(s2);
                //list1.add(lat);
                //list1.add(lng);
                //list1.add(name);
                //list1.add("==================");
                //list1.add(j2.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return list1;
    }

    protected void onPostExecute(ArrayList<SafeZoneInfo> obj) {
        ArrayList<String> a1 = new ArrayList<String>();


        if (obj != null) {
                //t2 = (TextView)activity.findViewById(R.id.textView);
                activity.s2Info = obj;
                //t2.setText(obj.toString());
                //t2.setText(obj.toString());
            }
        }
    }

