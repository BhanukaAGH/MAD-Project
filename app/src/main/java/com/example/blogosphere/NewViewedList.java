package com.example.blogosphere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.blogosphere.database.ArticleModal;
import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.ListModal;
import com.example.blogosphere.database.UserModel;

import java.util.ArrayList;
import java.util.List;

public class NewViewedList extends AppCompatActivity {

    ListView rowViewedList ;

    String storyTopic1[] = {"Apple says its iCloud scanning will rely on multiple child safety groups to address privacy fearsApple says its iCloud scanning will rely on multiple child safety groups to address privacy fears","Federal agencies use facial recognition from private companies, but almost nobody is keeping track","Facebook develops new method to reverse-engineer deepfakes and track their source","Deepfake dubs could help translate film and TV without losing an actor’s original performance","Behind the IPO Spectacle: Five Communications Lessons Learned the Hard Way"};
    String AuthorName1[] = {"Nick Tune","Jesse Weaver","Neel V. Patel","Katie Jacobs","Dan Hansen"};
    String publishDate1[] = {"2021/02/11", "2021/05/16", "2021/02/11", "2021/02/11", "2021/02/11"};

    TextView listTopic;
    TextView txtStories;
    TextView txtDate;
    ImageButton imgbtnclose;
    private DBHelper myDB;
    UserModel user;
    ListModal listItem;
    List<Integer> postsIDs;
    ArrayList<ArticleModal> articles;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_viewed_list);

        myDB = new DBHelper(this);

        userID = getIntent().getIntExtra("UserID",0);
        user = myDB.getUserbyID(userID);

        listItem = (ListModal) getIntent().getSerializableExtra("listItems");

        postsIDs = myDB.getCurrentListPost(userID, listItem.getList_ID());

        articles = new ArrayList<>();

        for(int i : postsIDs){
            articles.add(myDB.getSingleArticle(Integer.toString(i)));
        }

        BookmarkListAdapter adapterViewedList = new BookmarkListAdapter(this, R.layout.rowviewedlist, articles,myDB);
        rowViewedList = findViewById(R.id.listview_viewedlist);
        rowViewedList.setAdapter(adapterViewedList);

        imgbtnclose = findViewById(R.id.imgBtnClose);
        listTopic = findViewById(R.id.tv_top6);
        txtStories = findViewById(R.id.tv_topstorycount);
        txtDate = findViewById(R.id.tv_topdate1);

        listTopic.setText(listItem.getList_Topic());
        if(articles.size() > 0) {
            txtStories.setText(Integer.toString(articles.size()) + " Stories");
        }
        txtDate.setText(listItem.getCreated_date());
    }
}

class BookmarkListAdapter extends ArrayAdapter<ArticleModal> {
    Context context;
    ArrayList<ArticleModal> articles;
    private int resource;
    private DBHelper myDB;

    BookmarkListAdapter(Context context, int resource, ArrayList<ArticleModal> articles, DBHelper myDB) {
        super(context, resource, articles);
        this.context = context;
        this.resource = resource;
        this.articles = articles;
        this.myDB = myDB;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(resource, parent, false);

        TextView storyTopic1View = row.findViewById(R.id.tv_storytopic1);
        TextView AuthorName1View = row.findViewById(R.id.tv_author1);
        TextView publishDate1View = row.findViewById(R.id.tv_date2);

        ArticleModal article = articles.get(position);

        storyTopic1View.setText(article.getTitle());
        AuthorName1View.setText(myDB.getUserNameById(article.getWriter_id()));
        publishDate1View.setText(article.getDate());

        return row;
    }
}