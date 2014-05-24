package com.florentrevest.vivlio;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BooksAdapter extends BaseAdapter
{
    private Context m_context;
    private List<Book> m_booksList;

    public BooksAdapter(Context context, List<Book> booksList)
    {
        m_context = context;
        m_booksList = booksList;
    }
    
    public void setBooksList(List<Book> booksList)
    {
        m_booksList = booksList;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)m_context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            convertView = inflater.inflate(R.layout.view_book_item, null);
        }
        TextView textView = (TextView)convertView
                .findViewById(R.id.itemTitleView);
        textView.setText(m_booksList.get(position).getTitle());

        ImageView imageView = (ImageView)convertView
                .findViewById(R.id.itemCoverView);
        if(m_booksList.get(position).getThumbPath() == null)
            imageView.setImageResource(R.drawable.default_cover);
        else
            imageView.setImageDrawable(Drawable.createFromPath(m_booksList.get(position).getThumbPath()));

        return convertView;
    }

    public Book getBook(int position)
    {
        return m_booksList.get(position);
    }

    @Override
    public int getCount()
    {
        return m_booksList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

}
