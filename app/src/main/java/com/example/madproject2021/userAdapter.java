package com.example.madproject2021;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class userAdapter extends ArrayAdapter<user> {

    private Context mcontext;
    private  int mresource;


    public userAdapter(@NonNull Context context, int resource, @NonNull ArrayList<user> objects) {
        super(context, resource, objects);
        this.mcontext=context;
        this.mresource=resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);

        convertView = layoutInflater.inflate(mresource, parent, false);


        ImageView imageView = convertView.findViewById(R.id.imguser);

        TextView textname = convertView.findViewById(R.id.username);

        TextView post = convertView.findViewById(R.id.post);

        imageView.setImageResource(getItem(position).getImage());

        textname.setText(getItem(position).getName());

        post.setText(getItem(position).getDes());


        return convertView;

    }
}
