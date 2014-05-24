package com.florentrevest.vivlio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLiteBase extends SQLiteOpenHelper
{
    private static final String BOOKS_TABLE = "books_table";
    private static final String COL_ID = "ID";
    private static final String COL_ISBN = "ISBN";
    private static final String COL_TITLE = "Title";
    private static final String COL_AUTHORS = "Authors";
    private static final String COL_PUBLISHER = "Publisher";
    private static final String COL_PAGESNUMBER = "PagesNumber";
    private static final String COL_COMMENTS = "Comments";
    private static final String COL_DATE = "Date";
    private static final String COL_RATING = "Rating";
    private static final String COL_TOREAD = "ToRead";
    private static final String COL_THUMBPATH = "ThumbPath";

    private static final String CREATE_BDD = "CREATE TABLE " + BOOKS_TABLE
            + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_ISBN
            + " TEXT, " + COL_TITLE + " TEXT NOT NULL, " + COL_AUTHORS + " TEXT NOT NULL, " + COL_PUBLISHER + " TEXT, " + COL_PAGESNUMBER + " INTEGER, " + COL_COMMENTS
            + " TEXT, " + COL_DATE + " TEXT, " + COL_RATING + " INTEGER, " + COL_TOREAD + " INTEGER, " + COL_THUMBPATH + " TEXT);";

    public SQLiteBase(Context context, String name, CursorFactory factory,
            int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE " + BOOKS_TABLE + ";");
        onCreate(db);
    }

}
