package com.suvariyaraj.movieratingapp.CustomListView;

/**
 * Created by GOODBOY-PC on 07/04/16.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.suvariyaraj.movieratingapp.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String>{

    private final Activity activity;
    private final ArrayList<String> review_text;
    private final ArrayList<String> review_title;
    public CustomAdapter(Activity context,
                      ArrayList<String> text, ArrayList<String> title) {
        super(context, R.layout.list_single, text);
        this.activity= context;
        this.review_text = text;
        this.review_title = title;

    }
    private LayoutInflater inflater;
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view= inflater.inflate(R.layout.list_single, null);

        TextView reviewText = (TextView) view.findViewById(R.id.review_text);
        TextView reviewTitle = (TextView) view.findViewById(R.id.review_heading);

        reviewText.setText(review_text.get(position));
        reviewTitle.setText(review_title.get(position));
        if (position%2==0){
            view.setBackgroundColor(Color.parseColor("#c4c4c4"));
        }
        else{
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        return view;
    }
}