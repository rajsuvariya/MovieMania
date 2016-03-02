package com.suvariyaraj.movieratingapp;

/**
 * Created by GOODBOY-PC on 17/02/16.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GridViewActivity extends ActionBarActivity {
    private static final String TAG = GridViewActivity.class.getSimpleName();
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private String API_KEY = YOUR_API_KEY;
    private int page_number =1;
    private String FEED_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+API_KEY+"&page=";

    private Button next, prev;

    public int getPage_number() {
        return page_number;
    }

    public void setPage_number(int page_number) {
        this.page_number = page_number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gridview);


        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        //Start download
        new AsyncHttpTask().execute(FEED_URL+""+page_number);
        mProgressBar.setVisibility(View.VISIBLE);


        Button more = (Button) findViewById(R.id.more_movies);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncHttpTask().execute(FEED_URL+""+page_number);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                //Pass the image title and url to DetailsActivity
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);

                intent.putExtra("Movie", (Parcelable)item);
                //Start details activity
                startActivity(intent);
            }
        });
    }

    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
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
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
                page_number++;
            } else {
                Toast.makeText(GridViewActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
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

    /**
     * Parsing the feed results and get the list
     * @param result
     */
    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");
            GridItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("original_title");
                item = new GridItem();
                item.setTitle(title);
                String image = post.optString("poster_path");
                image = "http://image.tmdb.org/t/p/w500/"+image.substring(1);
                item.setImage(image);
                String backdrop = post.optString("backdrop_path");
                backdrop = "http://image.tmdb.org/t/p/w500/"+backdrop.substring(1);
                item.setBackdrop(backdrop);
                int id = post.optInt("id");
                item.setId(id);
                String release_date = post.optString("release_date");
                item.setRelease_date(release_date);
                String adult = post.optString("adult");
                item.setAdult(adult);
                String overview = post.optString("overview");
                item.setOverview(overview);
                String original_language = post.optString("original_language");
                item.setOriginal_language(original_language);
                int vote_count = post.optInt("vote_count");
                item.setVote_count(vote_count);
                double popularity = post.optDouble("popularity");
                item.setPopularity(popularity);
                double vote_average = post.optDouble("vote_average");
                item.setVote_average(vote_average);
                mGridData.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.sort_by_popolarity:
                FEED_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+API_KEY+"&page=";
                mGridData.clear();
                page_number = 1;
                new AsyncHttpTask().execute(FEED_URL + "" + page_number);
                break;

            case R.id.sort_by_count:
                FEED_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_count.desc&api_key="+API_KEY+"&page=";
                mGridData.clear();
                page_number = 1;
                new AsyncHttpTask().execute(FEED_URL+""+page_number);
                break;

            case R.id.sort_by_revenue:
                FEED_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=revenue.desc&api_key="+API_KEY+"&page=";
                mGridData.clear();
                page_number = 1;
                new AsyncHttpTask().execute(FEED_URL+""+page_number);
                break;

        }
        return true;

    }

    
}