package com.florentrevest.vivlio;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class AuthorActivity extends SherlockFragmentActivity implements BooksDB.OnUpdateListener
{
    String m_authorName;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        
        m_authorName = getIntent().getExtras().getString("Author");
        getSupportActionBar().setTitle(m_authorName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        
        if(fm.findFragmentById(R.id.fragment_container) == null)
        {
            BooksFragment booksFragment = new BooksFragment();
            Bundle b = new Bundle();
            b.putString("authorFilter", m_authorName);
            booksFragment.setArguments(b);

            fm.beginTransaction().add(R.id.fragment_container, booksFragment).commit();
        }
        
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
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_author, menu);
        return true;
    }

    @Override
    public void onUpdate(List<Book> booksList, List<String> newAuthorsList)
    {
        if(!newAuthorsList.contains(m_authorName))
            finish();
    }
}
