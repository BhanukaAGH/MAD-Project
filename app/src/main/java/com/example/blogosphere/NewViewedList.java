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

public class NewViewedList extends AppCompatActivity {

    ListView rowViewedList ;

    String storyTopic1[] = {"Apple says its iCloud scanning will rely on multiple child safety groups to address privacy fearsApple says its iCloud scanning will rely on multiple child safety groups to address privacy fears","Federal agencies use facial recognition from private companies, but almost nobody is keeping track","Facebook develops new method to reverse-engineer deepfakes and track their source","Deepfake dubs could help translate film and TV without losing an actor’s original performance","Behind the IPO Spectacle: Five Communications Lessons Learned the Hard Way"};
    String AuthorName1[] = {"Nick Tune","Jesse Weaver","Neel V. Patel","Katie Jacobs","Dan Hansen"};
    String publishDate1[] = {"2021/02/11", "2021/05/16", "2021/02/11", "2021/02/11", "2021/02/11"};

    ImageButton imgbtnclose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_viewed_list);

        BookmarkListAdapter adapterViewedList = new BookmarkListAdapter(this, storyTopic1, AuthorName1, publishDate1);
        rowViewedList = findViewById(R.id.listview_viewedlist);
        rowViewedList.setAdapter(adapterViewedList);

        imgbtnclose = findViewById(R.id.imgBtnClose);

    }
}

class BookmarkListAdapter extends ArrayAdapter<String> {
    Context context;
    String[] storyTopic1;
    String[] AuthorName1;
    String[] publishDate1;


    BookmarkListAdapter(Context context, String[] storyTopic1, String[] AuthorName1, String[] publishDate1) {
        super(context, R.layout.rowviewedlist, R.id.listview_viewedlist, storyTopic1);
        this.context = context;
        this.storyTopic1 = storyTopic1;
        this.AuthorName1 = AuthorName1;
        this.publishDate1 = publishDate1;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.rowviewedlist, parent, false);
        TextView storyTopic1View = row.findViewById(R.id.tv_storytopic1);
        TextView AuthorName1View = row.findViewById(R.id.tv_author1);

        TextView publishDate1View = row.findViewById(R.id.tv_date2);


        storyTopic1View.setText(storyTopic1[position]);
        AuthorName1View.setText(AuthorName1[position]);
        publishDate1View.setText(publishDate1[position]);

        return row;
    }
}