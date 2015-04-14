package com.example.vishnuchelle.mongorestapi;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishnu Chelle on 4/6/2015.
 */
public class MongoAsyncTask extends AsyncTask<Void,Void,Void> {

    //http get request to POST data to the server.
    public void httpPostInsert() {

        boolean result = false;
        HttpClient hc = new DefaultHttpClient();
        String message;
        String key = "hmmOXhHsA3Kp1f8HdZApSdh98JVvPLfP";
        String database = "diarytest";
        String collection = "testcollection";


        String url = "https://api.mongolab.com/api/1/databases/" +
                database +
                "/collections/" +
                collection +
                "?apiKey=" + key;

        //Json array for group members
        JSONArray members = new JSONArray();
        members.put("vishnu");
        members.put("varun");
        members.put("yashwanth");

        //JSON array of objects for status
        JSONArray status = new JSONArray();

        JSONObject status1 = new JSONObject();
        JSONObject status2 = new JSONObject();

        try{
            status1.put("date","04-14-2015");
            status1.put("userName","vishnu");
            status1.put("status","2This is first status by vishnu");

            status2.put("date","04-15-2015");
            status2.put("userName","yashwanth");
            status2.put("status","2This is second status by yahswanth");

        }catch (Exception e){
            e.printStackTrace();
        }

        status.put(status1);
        status.put(status2);


        //sample JSON object
        JSONObject group = new JSONObject();
        try {
            group.put("groupName", "Friends");
            group.put("groupMembers", members);
            group.put("groupStatus", status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Http POST url
        HttpPost p = new HttpPost(url);

        try {
            //convert json object to string
            message = group.toString();
            //pass the string as data to the POST url
            p.setEntity(new StringEntity(message, "UTF8"));
            p.setHeader("Content-type", "application/json");
            HttpResponse resp = hc.execute(p);
            if (resp != null) {
                if (resp.getStatusLine().getStatusCode() == 200)
                    result = true;
            }
            Log.i("Response Code", resp.getStatusLine().getStatusCode() + "");
            Log.i("Result is", result + "");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Failed", "POST Not Successful");

        }
    }


    //HTTP put request to update documents in the collection
    public void httpPutUpdate(){

        String key = "hmmOXhHsA3Kp1f8HdZApSdh98JVvPLfP";
        String database = "diarytest";
        String collection = "testcollection";
        boolean result = false;
        String message = "";

        String url = "https://api.mongolab.com/api/1/databases/" +
                database +
                "/collections/" +
                collection;

        JSONObject status3 = new JSONObject();

        try{
            status3.put("date","04-14-2015");
            status3.put("userName","vishnu");
            status3.put("status","2This is first status by vishnu");

        }catch (Exception e){
            e.printStackTrace();
        }


        HttpClient hc = new DefaultHttpClient();
        //Http POST url
        HttpPut put = new HttpPut(url);

        try {
            //convert json object to string
            message = status3.toString();
            //pass the string as data to the POST url
            put.setEntity(new StringEntity(message, "UTF8"));
            put.setHeader("Content-type", "application/json");
            HttpResponse resp = hc.execute(put);
            if (resp != null) {
                if (resp.getStatusLine().getStatusCode() == 200)
                    result = true;
            }
            Log.i("Response Code", resp.getStatusLine().getStatusCode() + "");
            Log.i("Result is", result + "");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Failed", "POST Not Successful");

        }

    }

    //http get request to retrive data from the server.
    public void httpGetRetrieve(){

        String key = "hmmOXhHsA3Kp1f8HdZApSdh98JVvPLfP";
        String database = "diarytest";
        String collection = "testcollection";
//        String url = "https://api.mongolab.com/api/1/databases/diarytest/collections/testcollection?q={%22use_id%22:%22chelle%22}&f={%22email%22:1}&apiKey=hmmOXhHsA3Kp1f8HdZApSdh98JVvPLfP";
//        https://api.mongolab.com/api/1/databases/diarytest/collections/testcollection?q={%22use_id%22:%22chelle%22}&f={%22email%22:1}&apiKey=hmmOXhHsA3Kp1f8HdZApSdh98JVvPLfP

        String url = "https://api.mongolab.com/api/1/databases/" +
                database +
                "/collections/" +
                collection;

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        //prameters to be added to the url
        params.add(new BasicNameValuePair("q", "{\"groupMembers\":\"vishnu\"}"));
//        params.add(new BasicNameValuePair("f","{\"phone\":1}"));
        params.add(new BasicNameValuePair("apiKey",key));

        //create http client
        HttpClient httpClient = new DefaultHttpClient();
        String paramsString = URLEncodedUtils.format(params, "UTF-8");

        HttpGet httpGet = new HttpGet(url + "?" + paramsString);

        InputStream inputStream = null;
        String result = "";
        try {

            // make GET request to the given URL
            HttpResponse response = httpClient.execute(httpGet);

            // receive response as inputStream
            inputStream = response.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null){
                result = convertInputStreamToString(inputStream);
                Log.i("InputStream:", result);
            }
            else{
                result = "Input Stream is null";
                Log.i("InputStream:", "Input Stream is null");
            }

        } catch (Exception e) {
            Log.i("InputStreamException", e.getLocalizedMessage());
        }
    }
    // convert inputstream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }


    @Override
    protected Void doInBackground(Void... params) {

//        httpPostInsert();
        httpGetRetrieve();
        return null;
    }
}
