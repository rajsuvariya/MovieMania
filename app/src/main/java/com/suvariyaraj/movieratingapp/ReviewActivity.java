package com.suvariyaraj.movieratingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.suvariyaraj.movieratingapp.CustomListView.CustomAdapter;
import com.suvariyaraj.movieratingapp.CustomListView.Helper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    private String FEED_URL, FEED_URL2;
    private String API_KEY = "69961736ee88b39584ba695c3da7a759";
    ListView list;
    ArrayList<String> reviewText;
    ArrayList<String> reviewTitle;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        int id = i.getIntExtra("ID", -1);
        FEED_URL2 = "http://api.themoviedb.org/3/movie/"+id+"/reviews?api_key="+API_KEY;

        Log.d("reviewActivity","Got");

        reviewText = new ArrayList<String>();
        reviewTitle = new ArrayList<String>();
        new AsyncHttpTask().execute(FEED_URL2 + "");


        list=(ListView)findViewById(R.id.review_list);

    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
//                Log.d("URL", params[0]);
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult2(response);
                    result=2;
//                  Log.d("abcd", params[0]);
                } else {
                    result=0;
//                  Log.d("abcd", params[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result==2) {
                CustomAdapter adapter = new
                        CustomAdapter(ReviewActivity.this, reviewText, reviewTitle);
                list.setAdapter(adapter);
                Toast.makeText(ReviewActivity.this, "fetched reviews!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ReviewActivity.this, "Failed to fetch trailer! Please try again.", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }


    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }

    private void parseResult2(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");
            Log.d("reviewActivity", "lenth orig : "+posts.length());
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String temp = post.optString("author");
                reviewTitle.add(temp);
                String temp2 = post.optString("content");
                reviewText.add(temp2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
