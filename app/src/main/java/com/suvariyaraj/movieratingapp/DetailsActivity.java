package com.suvariyaraj.movieratingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.suvariyaraj.movieratingapp.DBManager.DBAdapter;

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

/**
 * Created by GOODBOY-PC on 20/02/16.
 */
public class DetailsActivity extends ActionBarActivity {

    Button review;
    TextView title, rating, overview, release, trailer_text, review_text, favourite_text;
    ImageView image, backdrop, favourite, trailer_icon, review_icon, favourite_icon;
    private ProgressBar mProgressBar;
    private String FEED_URL, FEED_URL2;
    private String API_KEY = "69961736ee88b39584ba695c3da7a759";
    private String TRAILER_URL ="https://www.youtube.com/watch?v=";

    DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbAdapter = new DBAdapter(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
        overview = (TextView) findViewById(R.id.details_overview);
        title = (TextView) findViewById(R.id.details_movie_name);
        rating = (TextView) findViewById(R.id.details_rating);
        image = (ImageView) findViewById(R.id.details_image);
        backdrop = (ImageView) findViewById(R.id.details_backdrop);
        release = (TextView) findViewById(R.id.details_release_date);
        trailer_icon = (ImageView) findViewById(R.id.icon_youtube);
        trailer_text = (TextView) findViewById(R.id.details_movie_trailer);
        review_icon = (ImageView) findViewById(R.id.icon_review);
        review_text = (TextView) findViewById(R.id.details_movie_review);
        favourite_icon = (ImageView)findViewById(R.id.icon_favourite);
        favourite_text = (TextView)findViewById(R.id.details_movie_favourite);


        Intent i =getIntent();
        final GridItem item = (GridItem) i.getParcelableExtra("Movie");

        FEED_URL = "http://api.themoviedb.org/3/movie/"+item.getId()+"/videos?api_key="+API_KEY;
        new AsyncHttpTask().execute(FEED_URL + "");

        if (dbAdapter.isFav(item.getId())) {
            favourite_text.setText("Favourite movie");
            favourite_icon.setImageDrawable(null);
            favourite_icon.setImageResource(R.drawable.favourite_selected);
        }

        title.setText(item.getTitle());
        overview.setText(item.getOverview());
        Picasso.with(getApplicationContext()).load(item.getImage()).into(image);
        Picasso.with(getApplicationContext()).load(item.getBackdrop()).into(backdrop);
        String temp_rating = "Rating :\n"+item.getVote_average();
        rating.setText(temp_rating);
        String temp_release = "Release Date : \n"+item.getRelease_date();
        release.setText(temp_release);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ImageFullScreen.class);
                i.putExtra("image", item.getImage());
                startActivity(i);
            }
        });

        trailer_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(TRAILER_URL)));
            }
        });
        trailer_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(TRAILER_URL)));
            }
        });

        review_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ReviewActivity.class);
                i.putExtra("ID", item.getId());
                startActivity(i);
            }
        });
        review_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ReviewActivity.class);
                i.putExtra("ID", item.getId());
                startActivity(i);
            }
        });

        favourite_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favourite_text.getText().toString().equals("Add to favourite")){
                    favourite_text.setText("Favourite movie");
                    favourite_icon.setImageDrawable(null);
                    favourite_icon.setImageResource(R.drawable.favourite_selected);
                    dbAdapter.insert(item.getId());
                }
                else {
                    favourite_text.setText("Add to favourite");
                    favourite_icon.setImageDrawable(null);
                    favourite_icon.setImageResource(R.drawable.favourite_unselected);
                    dbAdapter.remove(item.getId());
                }
            }
        });
        favourite_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favourite_text.getText().toString().equals("Add to favourite")){
                    favourite_text.setText("Favourite movie");
                    favourite_icon.setImageDrawable(null);
                    favourite_icon.setImageResource(R.drawable.favourite_selected);
                    dbAdapter.insert(item.getId());
                }
                else {
                    favourite_text.setText("Add to favourite");
                    favourite_icon.setImageDrawable(null);
                    favourite_icon.setImageResource(R.drawable.favourite_unselected);
                    dbAdapter.remove(item.getId());
                }
            }
        });
//        reviewText = new ArrayList<String>();
//        reviewTitle = new ArrayList<String>();

        new AsyncHttpTask().execute(FEED_URL2+"");


//        list=(ListView)findViewById(R.id.review_list);
//
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Toast.makeText(DetailsActivity.this, "You Clicked at " + reviewText.get(position), Toast.LENGTH_SHORT).show();
//            }
//        });
//


    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

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
                    if(params[0].contains("videos")) {
                        parseResult(response);
                        result = 1;
//                        Log.d("abcd", params[0]);
                    }
                    else if(params[0].contains("reviews")){
                        parseResult2(response);
//                        Log.d("abcd", params[0]);
                        result = 2; // Successful
                    }
                } else {
//                    Log.d("abcd", params[0]);
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
                Toast.makeText(DetailsActivity.this, "fetched trailer!", Toast.LENGTH_SHORT).show();
            }
            else if (result==2) {
//                for (int i=0;i<reviewTitle.size();i++){
//                    tv.setText(tv.getText()+"\n"+"Title : "+reviewTitle.get(i));
//                }
//                CustomAdapter adapter = new
//                        CustomAdapter(DetailsActivity.this, reviewText, reviewTitle);
//                list.setAdapter(adapter);
//                Helper.getListViewSize(list);
                Toast.makeText(DetailsActivity.this, "fetched reviews!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(DetailsActivity.this, "Failed to fetch trailer! Please try again.", Toast.LENGTH_SHORT).show();
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
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String trailer = post.optString("key");
                TRAILER_URL = TRAILER_URL +trailer;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseResult2(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("results");
//            Log.d("resultlength", ""+posts.length());
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String temp = post.optString("author");
//                reviewTitle.add(temp);
                String temp2 = post.optString("content");
//                reviewText.add(temp2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
