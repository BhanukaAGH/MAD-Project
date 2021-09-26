package com.example.madprojectnew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CommentAdapter extends ArrayAdapter <Comment>{

    private Context context;
    private int resource;
    List<Comment> comments;

    CommentAdapter(Context context, int resource, List<Comment> comments){
        super(context, resource, comments);
        this.context = context;
        this.resource = resource;
        this.comments = comments;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(resource, parent, false);

        /*ImageView imageView =  row.findViewById(R.id.imageView);
        TextView nameView =  row.findViewById(R.id.name);*/
        TextView commentView =  row.findViewById(R.id.comment);

        Comment comment = comments.get(position);
        commentView.setText(comment.getComment());

        return row;
    }
}
