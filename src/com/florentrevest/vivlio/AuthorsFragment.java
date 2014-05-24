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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AuthorsFragment extends Fragment implements BooksDB.OnUpdateListener
{
    private List<String> m_authorsList;
    private ListView m_lView;
    
    public AuthorsFragment()
    {
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        m_lView = (ListView)inflater.inflate(R.layout.fragment_authors, container, false);
        
        BooksDB db = BooksDB.getInstance(getActivity());
        m_authorsList = (ArrayList<String>)db.getAuthors();
        m_lView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, m_authorsList));
        db.addOnUpdateListener(this);
        
        m_lView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent authorIntent = new Intent(getActivity(), AuthorActivity.class);
                authorIntent.putExtra("Author", ((TextView) view).getText());
                startActivity(authorIntent);
            }
        });
        
        return m_lView;
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
        m_authorsList = newAuthorsList;
        m_lView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, m_authorsList));
    }
}
