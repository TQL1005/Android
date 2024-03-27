package com.example.newapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DesAdapter2 extends ArrayAdapter {
    private Context ctx;
    private int mResources;
    private static Integer temp = 0;
    ArrayList<Destination> desList;
    public DesAdapter2(@NonNull Context context, int resource, @NonNull ArrayList<Destination> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.mResources = resource;
    }


    @SuppressLint({"DefaultLocale", "ViewHolder"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        convertView = layoutInflater.inflate(mResources,parent,false);
        TextView des = convertView.findViewById(R.id.destinationz);
        TextView desc = convertView.findViewById(R.id.descriptionz);
        TextView price = convertView.findViewById(R.id.pricez);
        ImageView img = convertView.findViewById(R.id.imageView4z);
        TextView amount = convertView.findViewById(R.id.amountz);
        TextView date_start = convertView.findViewById(R.id.start_datez);
        TextView date_end = convertView.findViewById(R.id.end_datez);



        Destination des1 = (Destination) getItem(position);
        des.setText(String.format("%s", des1.getTour_name()));
        desc.setText(String.format("%s", des1.getDescriptions()));
        price.setText(String.format("price: %.2f$", des1.getPrice()));
        amount.setText(String.format("available: %d", des1.getAmount()));
        date_start.setText(String.format("start: %s", des1.getStart_date()));
        date_end.setText(String.format("end: %s", des1.getEnd_date()));
        Glide.with(ctx)
                .load(MediaManager.get().url().generate(des1.getPic1()))
                .into(img);
        return convertView;
    }



}
