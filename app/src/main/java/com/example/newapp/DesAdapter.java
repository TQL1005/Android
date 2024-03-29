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

public class DesAdapter extends ArrayAdapter {
    private Context ctx;
    private int mResources;
    static Integer temp = 0;
    ArrayList<Destination> desList;
    public DesAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Destination> objects) {
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
        TextView des = convertView.findViewById(R.id.destination);
        TextView desc = convertView.findViewById(R.id.description);
        TextView price = convertView.findViewById(R.id.price);
        ImageView img = convertView.findViewById(R.id.imageView4);

        //Ket noi den Cloud
        if (temp == 0){
            initConfig();
        }


        Destination des1 = (Destination) getItem(position);
        des.setText(String.format("%s", des1.getDestinations()));
        desc.setText(String.format("Description =  %s", des1.getDescriptions()));
        price.setText(String.format("price = %.2f", des1.getPrice()));
        Glide.with(ctx)
                .load(MediaManager.get().url().generate(des1.getPic1()))
                .into(img);
        return convertView;
    }


    private void initConfig() {
        Map config = new HashMap();
        config.put("cloud_name", "drhgfqbzv");
        config.put("api_key", "158819295119423");
        config.put("api_secret", "AoaAmRiOV3QXlVFVDuFA4UI2yfs");
        MediaManager.init(ctx, config);
        temp = temp +1;
    }
}
