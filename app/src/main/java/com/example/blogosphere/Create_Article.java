package com.example.blogosphere;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.blogosphere.database.ArticleModal;
import com.example.blogosphere.database.DBHelper;
import com.example.blogosphere.database.UserModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import jp.wasabeef.richeditor.RichEditor;

public class Create_Article extends AppCompatActivity {

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

    private TextView txtPublish;
    private EditText addTagsToStory;
    private ListView tagsList;
    ArrayList<String> tags = new ArrayList<>();
    ArrayAdapter adapter;

    String storyContent = "";

    public static final int PICK_IMAGE_FOR_ARTICLE = 1;
    public static final int PICK_IMAGE = 2;

    ArticleModal article = new ArticleModal();
    UserModel user;

    DBHelper myDB;
    Date date;

    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);

        myDB = new DBHelper(this);

//        Get login user object
//        Intent i = getIntent();
//        user = (UserModel) i.getSerializableExtra("UserObject");

        userID = getIntent().getIntExtra("UserID",0);
        user = myDB.getUserbyID(userID);

        txtPublish = findViewById(R.id.txtPublish);
        txtPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storyContent.isEmpty()) {
                    Toasty.normal(Create_Article.this, R.string.check_story_empty).show();
                } else {
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

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.add_tags_bottom_sheet);

        Button btnPublish = bottomSheetDialog.findViewById(R.id.btnPublish);
        TextView addTagtxt = bottomSheetDialog.findViewById(R.id.txtAddTag);

        // Tags List View
        adapter = new ArrayAdapter<String>(this, R.layout.single_tag, R.id.oneTag, tags);

        tagsList = bottomSheetDialog.findViewById(R.id.tagsListView);


        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.isEmpty()) {
                    Toasty.normal(Create_Article.this, "You need to add tags to Article.").show();
                } else {
                    article.setTags(tags);
                    bottomSheetDialog.dismiss();
                    showDialogAddNameIamge();
                }
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
    private void showDialogAddTags() {
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
                            public void onClick(DialogInterface dialog, int id) {
                                String tagName = userInput.getText().toString();
                                tags.add(tagName);
                                tagsList.setAdapter(adapter);
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

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (article.getImage() != null && !articleName.getText().toString().isEmpty()) {
                                    String articleTile = articleName.getText().toString();
                                    article.setTitle(articleTile);
                                    article.setContent(storyContent);
                                    article.setWriter_id(userID);
                                    date = new Date();
                                    article.setDate(String.format(" %tb %<te", date));
                                    boolean publishPost = myDB.publishArticle(article);
                                    dialog.cancel();
                                    // Insert Tags new Lines
                                    myDB.getArticleID(article);
                                    for (String tagname : tags) {
                                        myDB.insertArticleTags(article.getId(), tagname);
                                    }
                                    // new Lines End
                                    if (publishPost == true) {
                                        Toasty.success(Create_Article.this, "Post Published.", Toast.LENGTH_SHORT, false).show();
                                        Intent publishedIntent = new Intent(Create_Article.this, Home.class);
                                        publishedIntent.putExtra("UserID", userID);
                                        startActivity(publishedIntent);
                                        overridePendingTransition(0, 0);

                                    } else {
                                        Toasty.error(Create_Article.this, "Post Published Failed.", Toast.LENGTH_SHORT, false).show();
                                    }
                                }else{
                                    Toasty.warning(Create_Article.this, "You must include an image and a title to publish the article.", Toast.LENGTH_SHORT, false).show();
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

    public void backWriteStory(View view) {
        Intent writepostIntent = new Intent(getApplicationContext(), Write_Story.class);
        writepostIntent.putExtra("UserID", userID);
        startActivity(writepostIntent);
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
            if(data != null){
                Uri articleuri = data.getData();
                mEditor.insertImage(articleuri.toString(), "dachshund", 300);
            }
        } else if (requestCode == PICK_IMAGE) {
            if (data != null) {
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

}