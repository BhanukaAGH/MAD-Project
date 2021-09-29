package com.example.blogosphere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blogosphere.database.ArticleModal;
import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.UserModel;

import java.util.ArrayList;

public class OtherProfileView extends AppCompatActivity {

    private ListView view;
    private ImageView ProfileImage;
    private TextView UserName;
    private TextView UserBio;
    private TextView Followers;
    private Button Follow;
    private Context context;
    private DBHelper myDB;
    private UserModel userModel;
    int userID;
    int authorID;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(OtherProfileView.this,Home.class);
        i.putExtra("UserID", userID);
        startActivity(i);
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile_view);

        userID = getIntent().getIntExtra("UserID",0);
        authorID = getIntent().getIntExtra("authorID",0);

        view= (ListView)findViewById(R.id.postlistviewpost);
        ArrayList<ArticleModal> arrayList = new ArrayList<>();
//        arrayList.add(new UserModel(R.drawable.profileimage,"Aaron Caleb","There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. "));
//        arrayList.add(new UserModel(R.drawable.profileimage,"Aaron Caleb","Contrary to popular belief, Lorem Ipsum is not simply random text."));
//        arrayList.add(new UserModel(R.drawable.profileimage,"Aaron Caleb","There are many variations of passages of Lorem Ipsum available"));

        OtherprofileArticleAdapter otherArticle =  new OtherprofileArticleAdapter(this,R.layout.post_structure,arrayList);
        view.setAdapter(otherArticle);
    }
}

class OtherprofileArticleAdapter extends ArrayAdapter<ArticleModal> {
    private Context mcontext;
    private  int mresource;

    public OtherprofileArticleAdapter(Context context, int resource, ArrayList<ArticleModal> objects) {
        super(context, resource, objects);
        this.mcontext=context;
        this.mresource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        convertView = layoutInflater.inflate(mresource, parent, false);

        ImageView imageView = convertView.findViewById(R.id.imguser);
        TextView textname = convertView.findViewById(R.id.username);
        TextView post = convertView.findViewById(R.id.post);
//        imageView.setImageResource(getItem(position).getImage());
//        textname.setText(getItem(position).getName());
//        post.setText(getItem(position).getDes());

        return convertView;
    }
}