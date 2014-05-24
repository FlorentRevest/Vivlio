package com.florentrevest.vivlio;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable
{
    private int m_id;
    private String m_isbn;
    private String m_title;
    private String m_authors;
    private String m_publisher;
    private int m_pagesNumber;
    private String m_comments;
    private String m_date;
    private int m_rating;
    private int m_toRead;
    private String m_thumbPath;

    public Book()
    {
    }

    public Book(String isbn, String title, String authors, String publisher, int pagesNumber, String comments, String date, int rating, int toRead, String thumbPath, int id)
    {
        m_isbn = isbn;
        m_title = title;
        m_authors = authors;
        m_publisher = publisher;
        m_pagesNumber = pagesNumber;
        m_comments = comments;
        m_date = date;
        m_rating = rating;
        m_toRead = toRead;
        m_thumbPath = thumbPath;
        m_id = id;
    }

    public int getId()
    {
        return m_id;
    }

    public void setId(int id)
    {
        m_id = id;
    }
    public String getIsbn()
    {
        return m_isbn;
    }

    public void setIsbn(String isbn)
    {
        m_isbn = isbn;
    }

    public String getTitle()
    {
        return m_title;
    }

    public void setTitle(String title)
    {
        m_title = title;
    }
    
    public String getAuthors()
    {
        return m_authors;
    }

    public void setAuthors(String authors)
    {
        m_authors = authors;
    }
    
    public String getPublisher()
    {
        return m_publisher;
    }

    public void setPublisher(String publisher)
    {
        m_publisher = publisher;
    }
    
    public String getComments()
    {
        return m_comments;
    }

    public void setComments(String comments)
    {
        m_comments = comments;
    }
        
    public String getDate()
    {
        return m_date;
    }

    public void setDate(String date)
    {
        m_date = date;
    }
    
    public int getPagesNumber()
    {
        return m_pagesNumber;
    }

    public void setPagesNumber(int pagesNumber)
    {
        m_pagesNumber = pagesNumber;
    }
    
    public int getRating()
    {
        return m_rating;
    }

    public void setRating(int rating)
    {
        m_rating = rating;
    }
    
    
    public int getToRead()
    {
        return m_toRead;
    }

    public void setToRead(int toRead)
    {
        m_toRead = toRead;
    }
    
    public String getThumbPath()
    {
        return m_thumbPath;
    }

    public void setThumbPath(String thumbPath)
    {
        m_thumbPath = thumbPath;
    }
    
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeInt(m_id);
        out.writeString(m_isbn);
        out.writeString(m_title);
        out.writeString(m_authors);
        out.writeString(m_publisher);
        out.writeInt(m_pagesNumber);
        out.writeString(m_comments);
        out.writeString(m_date);
        out.writeInt(m_rating);
        out.writeInt(m_toRead);
        out.writeString(m_thumbPath);
    }

    public void readFromParcel(Parcel in)
    {
        m_id = in.readInt();
        m_isbn = in.readString();
        m_title = in.readString();
        m_authors = in.readString();
        m_publisher = in.readString();
        m_pagesNumber = in.readInt();
        m_comments = in.readString();
        m_date = in.readString();
        m_rating = in.readInt();
        m_toRead = in.readInt();
        m_thumbPath = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>()
    {
        public Book createFromParcel(final Parcel in)
        {
            Book b = new Book();
            b.readFromParcel(in);
            return b;
        }
        
        public Book[] newArray(final int size)
        {
            return new Book[size];
        }
    };
}
