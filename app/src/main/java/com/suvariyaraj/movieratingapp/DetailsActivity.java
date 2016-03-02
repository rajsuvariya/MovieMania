package com.suvariyaraj.movieratingapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by GOODBOY-PC on 20/02/16.
 */
public class DetailsActivity extends ActionBarActivity {

    TextView title, rating, overview, release;
    ImageView image, backdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        overview = (TextView) findViewById(R.id.details_overview);
        title = (TextView) findViewById(R.id.details_movie_name);
        rating = (TextView) findViewById(R.id.details_rating);
        image = (ImageView) findViewById(R.id.details_image);
        backdrop = (ImageView) findViewById(R.id.details_backdrop);
        release = (TextView) findViewById(R.id.details_release_date);

        Intent i =getIntent();
        final GridItem item = (GridItem) i.getParcelableExtra("Movie");

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
