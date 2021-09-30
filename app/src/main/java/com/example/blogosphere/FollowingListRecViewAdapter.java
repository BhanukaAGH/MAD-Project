package com.example.blogosphere;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.FollowModel;

import java.util.ArrayList;
import java.util.List;

public class FollowingListRecViewAdapter extends  RecyclerView.Adapter<FollowingListRecViewAdapter.ViewHolder> {

    private List<FollowModel> followers = new ArrayList<>();
    private DBHelper myDB;

    public FollowingListRecViewAdapter(DBHelper myDB) {
        this.myDB = myDB;
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
        holder.txtName.setText(myDB.getUserNameById(followers.get(position).getUserid()));
        holder.followerImg.setImageBitmap(myDB.getUserImageById(followers.get(position).getUserid()));
    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    public void setFollowers(List<FollowModel> followers) {
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
