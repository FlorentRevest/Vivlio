package com.florentrevest.vivlio;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class BooksFragment extends Fragment implements BooksDB.OnUpdateListener
{
    private List<Book> m_booksList;
    private BooksAdapter adapt = null;
    private int m_toReadFilter = -1;
    private String m_authorFilter = null;

    public BooksFragment()
    {
    }

    
    @Override
    public void onSaveInstanceState(Bundle b)
    {
        b.putInt("toReadFilter", m_toReadFilter);
        b.putString("authorFilter", m_authorFilter);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        GridView gridView = (GridView)inflater.inflate(R.layout.fragment_books, container, false);

        BooksDB db = BooksDB.getInstance(getActivity());
        db.addOnUpdateListener(this);
        
        if(getArguments() != null)
        {
            if(getArguments().getInt("toReadFilter", 42) != 42)
                m_toReadFilter = getArguments().getInt("toReadFilter");
            if(getArguments().getString("authorFilter") != null)
                m_authorFilter = getArguments().getString("authorFilter");
        }
        if(savedInstanceState != null)
        {
            if(savedInstanceState.getInt("toReadFilter", 42) != 42)
                m_toReadFilter = savedInstanceState.getInt("toReadFilter");
            if(savedInstanceState.getString("authorFilter") != null)
                m_authorFilter = savedInstanceState.getString("authorFilter");
        }
        
        if(m_authorFilter != null)
            m_booksList = db.getBooksFromAuthor(m_authorFilter);
        else
        {
            m_booksList = db.getBooks();
            if(m_toReadFilter != -1)
            {
                List<Book> filteredBooks = new ArrayList<Book>();
                for(Book book : m_booksList)
                    if(book.getToRead() == m_toReadFilter)
                        filteredBooks.add(book);
                m_booksList = filteredBooks;
            }
        }
        
        adapt = new BooksAdapter(getActivity(), m_booksList);
        gridView.setAdapter(adapt);
        gridView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent bookinfointent = new Intent(getActivity(),
                        BookInfoActivity.class);
                bookinfointent.putExtra("book", adapt.getBook(position));
                startActivity(bookinfointent);
            }
        });
        return gridView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        BooksDB db = BooksDB.getInstance(getActivity());
        db.removeOnUpdateListener(this);
    }

    @Override
    public void onUpdate(List<Book> newBooksList, List<String> newAuthorsList)
    {
        if(m_authorFilter == null)
            m_booksList = newBooksList;
        else
            m_booksList = BooksDB.getInstance(getActivity()).getBooksFromAuthor(m_authorFilter);
            
        if(m_toReadFilter != -1)
        {
            List<Book> filteredBooks = new ArrayList<Book>();
            for(Book book : m_booksList)
                if(book.getToRead() == m_toReadFilter)
                    filteredBooks.add(book);
            m_booksList = filteredBooks;
        }

        adapt.setBooksList(m_booksList);
        adapt.notifyDataSetChanged();
    }

}