package com.example.blogosphere;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FollowingListRecViewAdapter extends  RecyclerView.Adapter<FollowingListRecViewAdapter.ViewHolder> {

    private ArrayList<Followers> followers = new ArrayList<>();

    public FollowingListRecViewAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_list_item, parent, false);
       ViewHolder holder = new ViewHolder(view);
       return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(followers.get(position).getName());
        holder.followerImg.setImageResource(followers.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    public void setFollowers(ArrayList<Followers> followers) {
        this.followers = followers;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtName;
        private ImageView followerImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            followerImg = itemView.findViewById(R.id.followerImg);
        }
    }
}
