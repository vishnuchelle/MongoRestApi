package com.example.vishnuchelle.mongorestapi;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

        //Http POST url
        String url = "https://api.mongolab.com/api/1/databases/" +
                database +
                "/collections/" +
                collection +
                "?apiKey=" + key;

        HttpPost p = new HttpPost(url);

        //sample JSON object
        JSONObject object = new JSONObject();
        try {
            object.put("email", "v@v.com.com");
            object.put("old_passw", "456");
            object.put("use_id", "test1");
            object.put("new_passw", "789");
            object.put("phone","456123");
        } catch (Exception e) {
            e.printStackTrace();
        }

//      JSONArray arrayJson = new JSONArray();
//      for(int i =0;i<10;i++){
//        JSONObject object = new JSONObject();
//        try {
//            object.put("email", "a@b.com");
//            object.put("old_passw", "456");
//            object.put("use_id", "456");
//            object.put("new_passw", "789");
//            arrayJson.put(object);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//      }

        try {
            //convert json object to string
            message = object.toString();

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

    //http get request to retrive data from the server.
    public void httpGetRetrieve(){

        String key = "hmmOXhHsA3Kp1f8HdZApSdh98JVvPLfP";
//        String url = "https://api.mongolab.com/api/1/databases/diarytest/collections/testcollection?q={%22use_id%22:%22chelle%22}&f={%22email%22:1}&apiKey=hmmOXhHsA3Kp1f8HdZApSdh98JVvPLfP";
//        https://api.mongolab.com/api/1/databases/diarytest/collections/testcollection?q={%22use_id%22:%22chelle%22}&f={%22email%22:1}&apiKey=hmmOXhHsA3Kp1f8HdZApSdh98JVvPLfP

        String url = "https://api.mongolab.com/api/1/databases/diarytest/collections/testcollection";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //prameters to be added to the url
        params.add(new BasicNameValuePair("q", "{\"use_id\":\"test1\"}"));
        params.add(new BasicNameValuePair("f","{\"phone\":1}"));
        params.add(new BasicNameValuePair("apiKey","hmmOXhHsA3Kp1f8HdZApSdh98JVvPLfP"));
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
