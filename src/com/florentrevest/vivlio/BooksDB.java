package com.florentrevest.vivlio;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BooksDB
{
    private List<OnUpdateListener> onUpdateListeners = null;

    private static final int DB_VESION = 1;
    private static final String DB_NAME = "vivlio_books.db";

    private static final String BOOKS_TABLE = "books_table";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_ISBN = "ISBN";
    private static final int NUM_COL_ISBN = 1;
    private static final String COL_TITLE = "Title";
    private static final int NUM_COL_TITLE = 2;
    private static final String COL_AUTHORS = "Authors";
    private static final int NUM_COL_AUTHORS = 3;
    private static final String COL_PUBLISHER = "Publisher";
    private static final int NUM_COL_PUBLISHER = 4;
    private static final String COL_PAGESNUMBER = "PagesNumber";
    private static final int NUM_COL_PAGESNUMBER = 5;
    private static final String COL_COMMENTS = "Comments";
    private static final int NUM_COL_COMMENTS = 6;
    private static final String COL_DATE = "Date";
    private static final int NUM_COL_DATE = 7;
    private static final String COL_RATING = "Rating";
    private static final int NUM_COL_RATING = 8;
    private static final String COL_TOREAD = "ToRead";
    private static final int NUM_COL_TOREAD = 9;
    private static final String COL_THUMBPATH = "ThumbPath";
    private static final int NUM_COL_THUMBPATH = 10;
    
    private SQLiteDatabase db;
    private SQLiteBase m_SQLiteBase;

    private static BooksDB instance;

    private BooksDB(Context context)
    {
        m_SQLiteBase = new SQLiteBase(context, DB_NAME, null, DB_VESION);
    }
    
    public static BooksDB getInstance(Context context) {
        if(null == instance)
            instance = new BooksDB(context);
        return instance;
    }

    public void open()
    {
        db = m_SQLiteBase.getWritableDatabase();
    }

    public void close()
    {
        db.close();
    }

    public SQLiteDatabase getBDD()
    {
        return db;
    }

    public long insertBook(Book book)
    {
        ContentValues values = new ContentValues();

        values.put(COL_ISBN, book.getIsbn());
        values.put(COL_TITLE, book.getTitle());
        values.put(COL_AUTHORS, book.getAuthors());
        values.put(COL_PUBLISHER, book.getPublisher());
        values.put(COL_PAGESNUMBER, book.getPagesNumber());
        values.put(COL_COMMENTS, book.getComments());
        values.put(COL_DATE, book.getDate());
        values.put(COL_RATING, book.getRating());
        values.put(COL_TOREAD, book.getToRead());
        values.put(COL_THUMBPATH, book.getThumbPath());

        long v = db.insert(BOOKS_TABLE, null, values);

        if(onUpdateListeners != null)
            for(OnUpdateListener oul : onUpdateListeners)
                oul.onUpdate(getBooks(), getAuthors());
        return v;
    }

    public int updateBook(int id, Book book)
    {
        ContentValues values = new ContentValues();

        values.put(COL_ISBN, book.getIsbn());
        values.put(COL_TITLE, book.getTitle());
        values.put(COL_AUTHORS, book.getAuthors());
        values.put(COL_PUBLISHER, book.getPublisher());
        values.put(COL_PAGESNUMBER, book.getPagesNumber());
        values.put(COL_COMMENTS, book.getComments());
        values.put(COL_DATE, book.getDate());
        values.put(COL_RATING, book.getRating());
        values.put(COL_TOREAD, book.getToRead());
        values.put(COL_THUMBPATH, book.getThumbPath());
        
        int v = db.update(BOOKS_TABLE, values, COL_ID + " = " + id, null);
        if(onUpdateListeners != null)
            for(OnUpdateListener oul : onUpdateListeners)
                oul.onUpdate(getBooks(), getAuthors());
        return v;
    }

    public int removeBookWithID(int id)
    {
        open();
        int v = db.delete(BOOKS_TABLE, COL_ID + " = " + id, null);
        if(onUpdateListeners != null)
            for(OnUpdateListener oul : onUpdateListeners)
                oul.onUpdate(getBooks(), getAuthors());
        close();
        return v;
    }

    public List<String> getAuthors()
    {
        open();
        Cursor c = db.query(BOOKS_TABLE, null, null, null, null, null, null);
        ArrayList<String> authors = new ArrayList<String>();

        c.moveToFirst();
        for(; c.isAfterLast() == false; c.moveToNext())
            if(!authors.contains(c.getString(NUM_COL_AUTHORS)))
                authors.add(c.getString(NUM_COL_AUTHORS));
        c.close();
        close();
        return authors;
    }
    
    public List<Book> getBooksFromAuthor(String author)
    {
        open();
        Cursor c = db.query(BOOKS_TABLE, new String[] {COL_ID, COL_ISBN, COL_TITLE, COL_AUTHORS, COL_PUBLISHER, COL_PAGESNUMBER, COL_COMMENTS, COL_DATE, COL_RATING, COL_TOREAD, COL_THUMBPATH}, COL_AUTHORS + " LIKE \"" + author + "\"", null, null,
                null, null);

        List<Book> books = new ArrayList<Book>();
        
        c.moveToFirst();
        for(; c.isAfterLast() == false; c.moveToNext())
            books.add(new Book(c.getString(NUM_COL_ISBN)
                    , c.getString(NUM_COL_TITLE), c.getString(NUM_COL_AUTHORS)
                    , c.getString(NUM_COL_PUBLISHER), c.getInt(NUM_COL_PAGESNUMBER)
                    , c.getString(NUM_COL_COMMENTS), c.getString(NUM_COL_DATE)
                    , c.getInt(NUM_COL_RATING), c.getInt(NUM_COL_TOREAD), c.getString(NUM_COL_THUMBPATH), c.getInt(NUM_COL_ID)));
        c.close();
        close();

        return books;
    }
    
    public Book getBookWithId(int id)
    {
        open();
        Cursor c = db.query(BOOKS_TABLE, new String[] {COL_ID, COL_ISBN, COL_TITLE, COL_AUTHORS, COL_PUBLISHER, COL_PAGESNUMBER, COL_COMMENTS, COL_DATE, COL_RATING, COL_TOREAD, COL_THUMBPATH}, COL_ID + " LIKE \"" + id + "\"", null, null, null, null);

        Book b = null;        
        
        c.moveToFirst();
        if(c.isAfterLast() == false)
            b = new Book(c.getString(NUM_COL_ISBN)
                    , c.getString(NUM_COL_TITLE), c.getString(NUM_COL_AUTHORS)
                    , c.getString(NUM_COL_PUBLISHER), c.getInt(NUM_COL_PAGESNUMBER)
                    , c.getString(NUM_COL_COMMENTS), c.getString(NUM_COL_DATE)
                    , c.getInt(NUM_COL_RATING), c.getInt(NUM_COL_TOREAD), c.getString(NUM_COL_THUMBPATH), c.getInt(NUM_COL_ID));
        c.close();
        close();

        return b;
    }

    public List<Book> getBooks()
    {
        open();
        Cursor c = db.query(BOOKS_TABLE, null, null, null, null, null, null);
        List<Book> books = new ArrayList<Book>();

        c.moveToFirst();
        for(; c.isAfterLast() == false; c.moveToNext())
            books.add(new Book(c.getString(NUM_COL_ISBN)
                    , c.getString(NUM_COL_TITLE), c.getString(NUM_COL_AUTHORS)
                    , c.getString(NUM_COL_PUBLISHER), c.getInt(NUM_COL_PAGESNUMBER)
                    , c.getString(NUM_COL_COMMENTS), c.getString(NUM_COL_DATE)
                    , c.getInt(NUM_COL_RATING), c.getInt(NUM_COL_TOREAD), c.getString(NUM_COL_THUMBPATH), c.getInt(NUM_COL_ID)));
        c.close();
        
        close();
        return books;
    }
    
    public interface OnUpdateListener
    {
        public abstract void onUpdate(List<Book> booksList, List<String> newAuthorsList);
    }

    public void removeOnUpdateListener(OnUpdateListener listener)
    {
        if(onUpdateListeners == null)
            onUpdateListeners = new ArrayList<OnUpdateListener>();
        
        onUpdateListeners.remove(listener);
    }
    
    public void addOnUpdateListener(OnUpdateListener listener)
    {
        if(onUpdateListeners == null)
            onUpdateListeners = new ArrayList<OnUpdateListener>();
        
        onUpdateListeners.add(listener);
    }
}
