package com.example.truongthanhhoa_2123110457;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> titles;
    private ArrayList<String> imageUrls;
    private ArrayList<String> prices;
    private ArrayList<String> categories;
    private ArrayList<String> ratings; // dạng "rate (count)"

    public CustomAdapter(Context context, ArrayList<String> titles, ArrayList<String> imageUrls,
                         ArrayList<String> prices, ArrayList<String> categories, ArrayList<String> ratings) {
        this.context = context;
        this.titles = titles;
        this.imageUrls = imageUrls;
        this.prices = prices;
        this.categories = categories;
        this.ratings = ratings;
    }

    @Override
    public int getCount() { return titles.size(); }

    @Override
    public Object getItem(int position) { return titles.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.item_image);
        TextView txtTitle = convertView.findViewById(R.id.item_text);
        TextView txtPrice = convertView.findViewById(R.id.item_price);
        TextView txtCategory = convertView.findViewById(R.id.item_category);
        TextView txtRating = convertView.findViewById(R.id.item_rating);

        txtTitle.setText(titles.get(position));
        txtPrice.setText(prices.get(position));
        txtCategory.setText(categories.get(position));
        txtRating.setText(ratings.get(position));

        Glide.with(context)
                .load(imageUrls.get(position))
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView);

        return convertView;
    }
}
