package com.florentrevest.vivlio;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class BookInfoActivity extends SherlockActivity implements BooksDB.OnUpdateListener
{  
    private Book m_book;
    private ImageView m_coverView;
    private RatingBar m_ratingBar;
    private TextView m_titleTextView, m_authorsTextView, m_publisherTextView, m_pagesNbTextView, m_dateTextView, m_stateTextView, m_commentsTextView;
    private int m_id;
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_book_info);

        m_book = (Book)getIntent().getExtras().getParcelable("book");
        
        m_id = m_book.getId();
        m_coverView = (ImageView)findViewById(R.id.coverView);
        String thumbPath = m_book.getThumbPath();
        if(thumbPath != null)
            m_coverView.setImageDrawable(Drawable.createFromPath(thumbPath));
        m_ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        m_ratingBar.setStepSize(1);
        m_ratingBar.setRating(m_book.getRating());
        m_titleTextView = (TextView)findViewById(R.id.titleTextViewContent);
        m_titleTextView.setText(m_book.getTitle());
        m_authorsTextView = (TextView)findViewById(R.id.authorsTextViewContent);
        m_authorsTextView.setText(m_book.getAuthors());
        m_publisherTextView = (TextView)findViewById(R.id.publisherTextViewContent);
        m_publisherTextView.setText(m_book.getPublisher());
        m_pagesNbTextView = (TextView)findViewById(R.id.pagesNumberTextViewContent);
        m_pagesNbTextView.setText(Integer.toString(m_book.getPagesNumber()));
        m_dateTextView = (TextView)findViewById(R.id.dateTextViewContent);
        m_dateTextView.setText(m_book.getDate());
        m_stateTextView = (TextView)findViewById(R.id.stateTextViewContent);
        if(m_book.getToRead() == 1)
            m_stateTextView.setText(getResources().getStringArray(R.array.state_array)[0]);
        else
            m_stateTextView.setText(getResources().getStringArray(R.array.state_array)[1]);
        m_commentsTextView = (TextView)findViewById(R.id.commentsTextViewContent);
        m_commentsTextView.setText(m_book.getComments());
        
        getSupportActionBar().setTitle(m_book.getTitle() + " - " + m_book.getAuthors());
        
        BooksDB.getInstance(getApplicationContext()).addOnUpdateListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item)
    {
        switch(item.getItemId())
        {
        case android.R.id.home:
            finish();
            return true;
        case R.id.menu_share:
            final Intent MessIntent = new Intent(Intent.ACTION_SEND);
            MessIntent.setType("text/plain");
            MessIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message) + " " + m_book.getTitle() + " " + getString(R.string.share_message2) + " " + m_book.getAuthors());
            BookInfoActivity.this.startActivity(Intent.createChooser(MessIntent, getString(R.string.share_title)));
            return true;
        case R.id.menu_edit:
            Intent editbookintent = new Intent(BookInfoActivity.this, EditBookActivity.class);
            editbookintent.putExtra("book", m_book);
            startActivity(editbookintent);
            return true;
        case R.id.menu_delete:
            BooksDB db = BooksDB.getInstance(this);
            db.removeBookWithID(m_book.getId());
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu)
    {
        getSupportMenuInflater().inflate(R.menu.activity_book_info, menu);
        return true;
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        BooksDB.getInstance(getApplicationContext()).removeOnUpdateListener(this);
    }

    @Override
    public void onUpdate(List<Book> booksList, List<String> newAuthorsList)
    {
        m_book = BooksDB.getInstance(getApplicationContext()).getBookWithId(m_id);
        
        if(m_book != null)
        {
            String thumbPath = m_book.getThumbPath();
            if(thumbPath != null)
                m_coverView.setImageDrawable(Drawable.createFromPath(thumbPath));
            m_ratingBar.setRating(m_book.getRating());
            m_titleTextView.setText(m_book.getTitle());
            m_authorsTextView.setText(m_book.getAuthors());
            m_publisherTextView.setText(m_book.getPublisher());
            m_pagesNbTextView.setText(Integer.toString(m_book.getPagesNumber()));
            m_dateTextView.setText(m_book.getDate());
            if(m_book.getToRead() == 1)
                m_stateTextView.setText(getResources().getStringArray(R.array.state_array)[0]);
            else
                m_stateTextView.setText(getResources().getStringArray(R.array.state_array)[1]);
            m_commentsTextView.setText(m_book.getComments());
        
            getSupportActionBar().setTitle(m_book.getTitle() + " - " + m_book.getAuthors());
        }
    }
}
