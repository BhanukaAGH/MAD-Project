package com.example.blogosphere;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blogosphere.database.ArticleModal;
import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.UserModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import jp.wasabeef.richeditor.RichEditor;

public class Edit_Article extends AppCompatActivity {

    private RichEditor mEditor;
    private ImageView bold;
    private ImageView italic;
    private ImageView underline;
    private ImageView left_align;
    private ImageView center_align;
    private ImageView right_align;
    private ImageView insert_image;
    private ImageView addImage;

    boolean isBold = false;
    boolean isItalic = false;
    boolean isUnderline = false;

    private TextView txtSave;

    String storyContent;

    public static final int PICK_IMAGE_FOR_ARTICLE = 100;
    public static final int PICK_IMAGE = 101;

    UserModel user;
    ArticleModal article;

    DBHelper myDB;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        myDB = new DBHelper(this);

        // Get login user object
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        user = (UserModel) extras.getSerializable("UserObject");
        String articleID = extras.getString("StoryID");

        article = myDB.getSingleArticle(articleID);

        storyContent = article.getContent();

        System.out.println(storyContent);

        txtSave = findViewById(R.id.txtSave);
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storyContent.length()-4 == 0) {
                    Toasty.normal(Edit_Article.this, R.string.check_story_empty).show();
                } else {
                    showDialogAddNameIamge();
                }
            }
        });

        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(20);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");
        mEditor.setHtml(storyContent);


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
                checkIsClicked(bold, isBold);
                mEditor.setBold();
            }
        });

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isItalic = !isItalic;
                checkIsClicked(italic, isItalic);
                mEditor.setItalic();
            }
        });

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUnderline = !isUnderline;
                checkIsClicked(underline, isUnderline);
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
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE_FOR_ARTICLE);
            }
        });
    }

    private void checkIsClicked(ImageView v, boolean isClicked) {
        if (isClicked) {
            v.setColorFilter(Color.BLACK);
            isClicked = false;
        } else {
            v.setColorFilter(null);
            isClicked = true;
        }
    }

    private void showDialogAddNameIamge() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.add_name_image_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText articleName = (EditText) promptsView
                .findViewById(R.id.edtName);
        addImage = (ImageView) promptsView
                .findViewById(R.id.addImg);

        articleName.setText(article.getTitle());
        addImage.setImageBitmap(article.getImage());

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String articleTile = articleName.getText().toString();
                                article.setTitle(articleTile);
                                article.setContent(storyContent);
                                date = new Date();
                                article.setDate(String.format(" %tb %<te",date));
                                int state = myDB.updateArticle(article);
                                System.out.println(state);
                                dialog.cancel();
                                if(state > 0){
                                    Toasty.success(Edit_Article.this, "Post Edited.", Toast.LENGTH_SHORT, false).show();
                                } else {
                                    Toasty.error(Edit_Article.this, "Post Edited Failed.", Toast.LENGTH_SHORT, false).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void backProfile(View view) {
        Intent editpostIntent = new Intent(getApplicationContext(), Profile.class);
        editpostIntent.putExtra("UserObject", user);
        startActivity(editpostIntent);
        overridePendingTransition(0, 0);
    }

    public void getArticleImage(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_FOR_ARTICLE) {
            Uri articleuri = data.getData();
            mEditor.insertImage(articleuri.toString(), "dachshund", 300);
        } else if (requestCode == PICK_IMAGE) {
            Uri articleImageuri = data.getData();
            addImage.setImageURI(articleImageuri);
//            new Line
            try {
                Bitmap imgtostore = MediaStore.Images.Media.getBitmap(getContentResolver(), articleImageuri);
                article.setImage(imgtostore);
            } catch (IOException e) {
                Toasty.normal(this, e.getMessage()).show();
            }

        }
    }

}