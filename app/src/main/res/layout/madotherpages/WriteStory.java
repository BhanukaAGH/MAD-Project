package com.example.madotherpages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import jp.wasabeef.richeditor.RichEditor;


public class WriteStory extends AppCompatActivity {

    private RichEditor mEditor;
    private ImageView bold;
    private ImageView italic;
    private ImageView underline;
    private ImageView left_align;
    private ImageView center_align;
    private ImageView right_align;
    private ImageView insert_image;

    boolean isBold = false;
    boolean isItalic = false;
    boolean isUnderline = false;

    private TextView txtPublish;
    private EditText addTagsToStory;
    private ListView tagsList;
    ArrayList<String> tags = new ArrayList<>();
    ArrayAdapter adapter;

    String storyContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_story);

        txtPublish = findViewById(R.id.txtPublish);
        txtPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storyContent.isEmpty()){
                    Toasty.normal(WriteStory.this, R.string.check_story_empty).show();
                }else{
                    showBottomSheetDialog();
                }
            }
        });

        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(20);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");


        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                storyContent = text;
            }
        });

        bold = findViewById(R.id.action_bold);
        italic = findViewById(R.id.action_italic);
        underline = findViewById(R.id.action_underline);
        left_align = findViewById(R.id.action_align_left);
        center_align = findViewById(R.id.action_align_center);
        right_align = findViewById(R.id.action_align_right);
        insert_image = findViewById(R.id.action_insert_image);


        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBold = !isBold;
                checkIsClicked(bold,isBold);
                mEditor.setBold();
            }
        });

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isItalic = !isItalic;
                checkIsClicked(italic,isItalic);
                mEditor.setItalic();
            }
        });

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUnderline = !isUnderline;
                checkIsClicked(underline,isUnderline);
                mEditor.setUnderline();
            }
        });

        left_align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        center_align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        right_align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        insert_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertImage("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg",
                        "dachshund", 320);
            }
        });

    }

    private void checkIsClicked(ImageView v, boolean isClicked){
        if(isClicked)
        {
            v.setColorFilter(Color.BLACK);
            isClicked = false;
        }
        else
        {
            v.setColorFilter(null);
            isClicked = true;
        }
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.add_tags_bottom_sheet);

        Button btnPublish = bottomSheetDialog.findViewById(R.id.btnPublish);
        TextView addTagtxt = bottomSheetDialog.findViewById(R.id.txtAddTag);

        // Tags List View
        adapter = new ArrayAdapter<String>(this,R.layout.single_tag,R.id.oneTag,tags);

        tagsList = bottomSheetDialog.findViewById(R.id.tagsListView);


        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Publish Button is Clicked ", Toast.LENGTH_LONG).show();
                bottomSheetDialog.dismiss();
            }
        });

        addTagtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddTags();
            }
        });

        bottomSheetDialog.show();
    }


    // Dialog Box
    private void showDialogAddTags(){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.add_tags_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.edtTag);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                String tagName = userInput.getText().toString();
                                tags.add(tagName);
                                tagsList.setAdapter(adapter);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}