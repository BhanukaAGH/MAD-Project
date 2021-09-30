package com.example.blogosphere;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.ListModal;
import com.example.blogosphere.database.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Bookmarks extends AppCompatActivity {

    private Button start_now;
    private ListView rowreadinglistView;
    private List<ListModal> listmodel;
    Context context;
    private DBHelper myDB;
    UserModel user;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        myDB = new DBHelper(this);

        userID = getIntent().getIntExtra("UserID", 0);
        user = myDB.getUserbyID(userID);

//        Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

//        Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.bookmark);

//        Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent homeIntent = new Intent(getApplicationContext(), Home.class);
                        homeIntent.putExtra("UserID", userID);
                        startActivity(homeIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.bookmark:
                        return true;
                    case R.id.writepost:
                        Intent writepostIntent = new Intent(getApplicationContext(), Write_Story.class);
                        writepostIntent.putExtra("UserID", userID);
                        startActivity(writepostIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
                        profileIntent.putExtra("UserID", userID);
                        startActivity(profileIntent);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


        // Gihan Line
        context = this;

        rowreadinglistView = findViewById(R.id.listview_home);
        start_now = findViewById(R.id.btn_startnow);
        listmodel = new ArrayList<>();
        listmodel = myDB.getAllLists();

        CustomAdapter_List_HomePage adapter = new CustomAdapter_List_HomePage(context, R.layout.rowreadinglists, listmodel, myDB);
        rowreadinglistView.setAdapter(adapter);

        start_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CreateNewList.class);
                i.putExtra("UserID", userID);
                Toast.makeText(getApplicationContext(), "create new list has pressed", Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });

        rowreadinglistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListModal listmodels = listmodel.get(i);
                Intent newI = new Intent(context, NewViewedList.class);
                newI.putExtra("UserID", userID);
                newI.putExtra("listItems", listmodels);
                startActivity(newI);
            }
        });

        rowreadinglistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                ListModal listmodelsLongClick = listmodel.get(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(listmodelsLongClick.getList_Topic());
                builder.setMessage(listmodelsLongClick.getList_Description());

                builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDB.deleteList(listmodelsLongClick.getList_ID());
                        Intent bookmarkI = new Intent(context, Bookmarks.class);
                        bookmarkI.putExtra("UserID", userID);
                        startActivity(bookmarkI);
                    }
                });

                builder.setNegativeButton("update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, NewEditList.class);
                        intent.putExtra("UserID", userID);
                        intent.putExtra("LIST_ID", String.valueOf(listmodelsLongClick.getList_ID()));
                        startActivity(intent);
                    }
                });
                builder.show();

                return false;
            }
        });

    }
}


class CustomAdapter_List_HomePage extends ArrayAdapter<ListModal> {

    private Context context;
    private int resource;
    List<ListModal> listmodel;
    private DBHelper myDB;

    public CustomAdapter_List_HomePage(@NonNull Context context, int resource, List<ListModal> listmodel, DBHelper myDB) {
        super(context, resource, listmodel);
        this.context = context;
        this.resource = resource;
        this.listmodel = listmodel;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(resource, parent, false);

        TextView listTopicView = row.findViewById(R.id.tv_listtitle1);
        TextView listDiscriptionView = row.findViewById(R.id.tv_listdiscription);
        TextView storyCountView = row.findViewById(R.id.tv_countstories1);
        TextView publishDateView = row.findViewById(R.id.tv_date1);

        //arraylist01 [ob1,ob2,ob3]
        ListModal Lmodels = listmodel.get(position);
        listTopicView.setText(Lmodels.getList_Topic());
        listDiscriptionView.setText(Lmodels.getList_Description());
        storyCountView.setText(Integer.toString(myDB.CountListPosts(Lmodels.getList_ID())) + " Stories");
        publishDateView.setText(Lmodels.getCreated_date());

        return row;
    }
}