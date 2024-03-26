////package com.example.newapp;
////
////import android.annotation.SuppressLint;
////import android.content.Context;
////import android.util.Pair;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.ArrayAdapter;
////import android.widget.ImageView;
////import android.widget.TextView;
////
////import androidx.annotation.NonNull;
////import androidx.annotation.Nullable;
////
////import com.bumptech.glide.Glide;
////import com.cloudinary.android.MediaManager;
////
////import java.util.ArrayList;
////import java.util.HashMap;
////import java.util.List;
////import java.util.Map;
////
////public class DesAdapter2 extends ArrayAdapter {
////    private Context ctx;
////    private int mResources;
////    private static Integer temp = 0;
////
////
////    public static ArrayList<Object> mergeLists(ArrayList<Destination> first, ArrayList<Tour> second) {
////        ArrayList<Object> result = new ArrayList<>();
////        result.addAll(first);
////        result.addAll(second);
////        return result;
////    }
////
////    public DesAdapter2(@NonNull Context context, int resource, @NonNull Pair<ArrayList<Destination>, ArrayList<Tour>> objects) {
////        super(context, resource, mergeLists(objects.first, objects.second));
////        this.ctx = context;
////        this.mResources = resource;
////    }
////
////
////
////
////    @SuppressLint({"DefaultLocale", "ViewHolder"})
////    @NonNull
////    @Override
////    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
////        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
////        convertView = layoutInflater.inflate(mResources,parent,false);
////        TextView des = convertView.findViewById(R.id.destination);
////        TextView desc = convertView.findViewById(R.id.description);
////        TextView price = convertView.findViewById(R.id.price);
////        ImageView img = convertView.findViewById(R.id.imageView4);
////
////        //Ket noi den Cloud
////        if (temp == 0){
//////            initConfig();
////        }
////
////
////        Tour des1 = (Tour) getItem(position);
////        Destination des2 = (Destination) getItem(position);
////        des.setText(String.format("%s", des1.getTour_name()));
////        desc.setText(String.format("Description =  %s", des2.getDescriptions()));
////        price.setText(String.format("price = %.2f", des2.getPrice()));
////        Glide.with(ctx)
////                .load(MediaManager.get().url().generate(des2.getPic1()))
////                .into(img);
////        return convertView;
////
////    }
////
////
//////    private void initConfig() {
//////        Map config = new HashMap();
//////        config.put("cloud_name", "drhgfqbzv");
//////        config.put("api_key", "158819295119423");
//////        config.put("api_secret", "AoaAmRiOV3QXlVFVDuFA4UI2yfs");
//////        MediaManager.init(ctx, config);
//////        temp = temp +1;
//////    }
////}
//
//package com.example.newapp;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.util.Pair;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.bumptech.glide.Glide;
//import com.cloudinary.android.MediaManager;
//
//import java.util.ArrayList;
//
//public class DesAdapter2 extends ArrayAdapter<Object> {
//    private Context ctx;
//    private int mResources;
//    private static Integer temp = 0;
//
//    public DesAdapter2(@NonNull Context context, int resource, @NonNull ArrayList<Destination> objects) {
//        super(context, resource, mergeLists(objects.first, objects.second));
//        this.ctx = context;
//        this.mResources = resource;
//        initConfig();
//    }
//
//    private static ArrayList<Object> mergeLists(ArrayList<Destination> first, ArrayList<Tour> second) {
//        ArrayList<Object> result = new ArrayList<>();
//        result.addAll(first);
//        result.addAll(second);
//        return result;
//    }
//
//    @SuppressLint("DefaultLocale")
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        if (convertView == null) {
//            LayoutInflater layoutInflater = LayoutInflater.from(ctx);
//            convertView = layoutInflater.inflate(mResources, parent, false);
//        }
//
//        TextView des = convertView.findViewById(R.id.destination);
//        TextView desc = convertView.findViewById(R.id.description);
//        TextView price = convertView.findViewById(R.id.price);
//        ImageView img = convertView.findViewById(R.id.imageView4);
//
//        Object item = getItem(position);
//        if (item instanceof Tour) {
//            Tour tour = (Tour) item;
//            des.setText(String.format("Tour = %s", tour.getTour_name()));
//        }
//        if (item instanceof Destination) {
//            Destination destination = (Destination) item;
//            desc.setText(String.format("Description =  %s", destination.getDescriptions()));
//            price.setText(String.format("price = %.2f", destination.getPrice()));
//            Glide.with(ctx)
//                    .load(MediaManager.get().url().generate(destination.getPic1()))
//                    .into(img);
//        }
//
//        return convertView;
//    }
//
//
//
//    private void initConfig() {
//        // Initialize MediaManager configuration
////        MediaManager.init(ctx);
//    }
//}
//
