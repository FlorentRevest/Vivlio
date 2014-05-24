package com.florentrevest.vivlio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class AddBookActivity extends SherlockActivity
{
    private EditText mEditTitle;
    private EditText mEditAuthors;
    private EditText mEditPublisher;
    private EditText mEditPagesNumber;
    private EditText mEditComments;
    private EditText mEditDate;
    private RatingBar mEditRating;
    private Button mOkButton;
    private Spinner mStateSpinner;
    private ImageView mBookPreview;
    private String mISBN;
    private String m_thumbPath;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_book);

        mEditTitle = (EditText)findViewById(R.id.editTitle);
        mEditTitle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        mEditAuthors = (EditText)findViewById(R.id.editAuthors);
        mEditAuthors.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        mEditPublisher = (EditText)findViewById(R.id.editPublisher);
        mEditPublisher.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        mEditPagesNumber = (EditText)findViewById(R.id.editPagesNb);
        mEditComments = (EditText)findViewById(R.id.editComments);
        mEditComments.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mEditDate = (EditText)findViewById(R.id.editDate);
        mEditRating = (RatingBar)findViewById(R.id.editRating);
        mEditRating.setStepSize(1);
        mOkButton = (Button)findViewById(R.id.Okbutton);
        mStateSpinner = (Spinner)findViewById(R.id.stateSpinner);
        mBookPreview = (ImageView)findViewById(R.id.bookPreview);
        mISBN = getIntent().getExtras().getString("ISBN");

        if(mISBN.length() == 13 || mISBN.length() == 10)
        {
            String path = "https://www.googleapis.com/books/v1/volumes?q=isbn:"
                    + mISBN;
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(path, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(JSONObject response)
                {
                    try
                    {
                        if(response.has("items"))
                        {
                            JSONObject item = (JSONObject)response
                                    .getJSONArray("items").get(0);
                            JSONObject volumeInfo = item
                                    .getJSONObject("volumeInfo");

                            mEditTitle.setText(volumeInfo.getString("title"));
                            mEditAuthors.setText(volumeInfo
                                    .getJSONArray("authors").toString()
                                    .replace("[", "").replace("]", "")
                                    .replace(",", ", ").replace("\"", ""));
                            mEditPublisher.setText(volumeInfo
                                    .getString("publisher"));
                            mEditPagesNumber.setText(volumeInfo
                                    .getString("pageCount"));
                            mEditComments.setText(volumeInfo
                                    .getString("description"));
                            mEditDate.setText(volumeInfo
                                    .getString("publishedDate"));
                            
                            final String thumb_url = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                            
                            AsyncHttpClient coverClient = new AsyncHttpClient();
                            coverClient.get(thumb_url, new BinaryHttpResponseHandler() {
                                @Override
                                public void onSuccess(byte[] fileData) {
                                    try
                                    {
                                        String id = Uri.parse(thumb_url).getQueryParameter("id");
                                        
                                        FileOutputStream fos = openFileOutput(id, AddBookActivity.MODE_PRIVATE);
                                        fos.write(fileData);
                                        fos.close();
                                        
                                        mBookPreview.setImageDrawable(Drawable.createFromStream(openFileInput(id), id));
                                        m_thumbPath = getFilesDir().toString() + "/" + id;
                                    }
                                    catch(IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                    catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        else
            mISBN = "";
        
        mBookPreview.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                final CharSequence[] coverOptions = {getString(R.string.title_cover1), getString(R.string.title_cover2), getString(R.string.title_cover3)};

                AlertDialog.Builder b = new AlertDialog.Builder(AddBookActivity.this);
                b.setTitle(getString(R.string.title_cover_menu));
                b.setItems(coverOptions, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int item)
                    {
                        switch(item)
                        {
                        case 0:
                            Intent galleryIntent = new Intent();
                            galleryIntent.setType("image/*");
                            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(galleryIntent, getString(R.string.title_cover_menu)), 0);
                            break;
                        case 1:
                            Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            AddBookActivity.this.startActivityForResult(photoIntent, 1);
                            break;
                        case 2:
                            if(mEditTitle.getText().length() == 0 || mEditAuthors.getText().length() == 0)
                                Toast.makeText(AddBookActivity.this, getString(R.string.no_title_or_authors), Toast.LENGTH_LONG).show();
                            else
                            {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.fr/search?site=imghp&tbm=isch&q=" + mEditTitle.getText() + " " + mEditAuthors.getText()));
                                startActivity(browserIntent);
                            }
                            break;
                        };
                    }
                });
                b.show();
            }
        });

        mOkButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg)
            {
                if(mEditTitle.getText().length() == 0 || mEditAuthors.getText().length() == 0)
                    Toast.makeText(AddBookActivity.this, getString(R.string.no_title_or_authors), Toast.LENGTH_LONG).show();
                else
                {
                    BooksDB db = BooksDB.getInstance(getApplicationContext());
                    db.open();
                    
                    int toRead;
                    if(mStateSpinner.getSelectedItem().toString().equals(AddBookActivity.this.getResources().getStringArray(R.array.state_array)[0]))
                        toRead = 1;
                    else
                        toRead = 0;
                    
                    int pagesNumber;
                    String pagesNumberStr = mEditPagesNumber.getText().toString();
                    if(pagesNumberStr.equals(""))
                        pagesNumber = 0;
                    else
                        pagesNumber = Integer.parseInt(pagesNumberStr);

                    db.insertBook(new Book(mISBN, mEditTitle.getText().toString(), mEditAuthors.getText().toString(), mEditPublisher.getText().toString(), pagesNumber, mEditComments.getText().toString(), mEditDate.getText().toString(), (int)mEditRating.getRating(), toRead, m_thumbPath, -1));
                    db.close();
                    finish();
                }
            }
        });
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == SherlockActivity.RESULT_OK) {
            if (requestCode == 0) {
                Uri selectedImageUri = data.getData();
                m_thumbPath = getPath(selectedImageUri);

                mBookPreview.setImageDrawable(Drawable.createFromPath(m_thumbPath));
            }
            else
            {
                Bundle extras = data.getExtras();
                Bitmap coverBitmap = (Bitmap) extras.get("data");
                if(coverBitmap != null)
                {
                    try
                    {
                        mBookPreview.setImageBitmap(coverBitmap);
                    
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        FileOutputStream out = openFileOutput(timeStamp, AddBookActivity.MODE_PRIVATE);
                        coverBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                        m_thumbPath = getFilesDir().toString() + "/" + timeStamp;
                    }
                    catch(FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor != null)
        {
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) 
    {
        switch(item.getItemId())
        {
        case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
