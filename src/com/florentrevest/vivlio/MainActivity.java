package com.florentrevest.vivlio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity implements
        ActionBar.TabListener
{

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
                {
                    @Override
                    public void onPageSelected(int position)
                    {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        for(int i = 0; i < mSectionsPagerAdapter.getCount(); i++)
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu)
    {
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item)
    {
        switch(item.getItemId())
        {
        case android.R.id.home:
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        case R.id.menu_add:
            final CharSequence[] addOptions = {getString(R.string.title_add1),
                    getString(R.string.title_add2),
                    getString(R.string.title_add3)};

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.title_add_menu))
                    .setItems(addOptions, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int item)
                        {
                            switch(item)
                            {
                            case 0:
                                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                                intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                                startActivityForResult(intent, 0);
                                break;
                            case 1:
                                final EditText input = new EditText(
                                        MainActivity.this);
                                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle(
                                                getString(R.string.title_isbn_dialog))
                                        .setView(input)
                                        .setPositiveButton(
                                                getString(android.R.string.ok),
                                                new DialogInterface.OnClickListener()
                                                {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int whichButton)
                                                    {
                                                        String value = input
                                                                .getText()
                                                                .toString();
                                                        
                                                        if(value.length() == 13 || value.length() == 10)
                                                        {
                                                            Intent addbookintent = new Intent(
                                                                    MainActivity.this,
                                                                    AddBookActivity.class);
                                                            addbookintent.putExtra("ISBN", value);
                                                            startActivity(addbookintent);
                                                        }
                                                        else
                                                            Toast.makeText(MainActivity.this, getString(R.string.isbn_not_long_enough),
                                                                    Toast.LENGTH_LONG).show();
                                                    }
                                                })
                                        .setNegativeButton(
                                                getString(android.R.string.cancel),
                                                new DialogInterface.OnClickListener()
                                                {
                                                    public void onClick(DialogInterface dialog, int whichButton)
                                                    {
                                                    }
                                                }).show();
                                break;
                            case 2:
                                Intent addbookintent = new Intent(
                                        MainActivity.this,
                                        AddBookActivity.class);
                                addbookintent.putExtra("ISBN", "");
                                startActivity(addbookintent);
                                break;
                            }
                        }
                    }).show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode == 0 && resultCode == RESULT_OK)
        {
            String value = intent.getStringExtra("SCAN_RESULT");
            Intent addbookintent = new Intent(MainActivity.this, AddBookActivity.class);
            addbookintent.putExtra("ISBN", value);
            startActivity(addbookintent);
        }
        else
            Toast.makeText(this, getString(R.string.barcode_not_found),
                    Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction)
    {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction)
    {
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction)
    {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        private BooksFragment m_everyBooksF, m_toReadBooksF, m_readBooksF;
        private AuthorsFragment m_authorsF;
        
        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
            
            m_everyBooksF = new BooksFragment();
            
            m_toReadBooksF = new BooksFragment();
            Bundle b = new Bundle();
            b.putInt("toReadFilter", 1);
            m_toReadBooksF.setArguments(b);
            
            m_readBooksF = new BooksFragment();
            b = new Bundle();
            b.putInt("toReadFilter", 0);
            m_readBooksF.setArguments(b);
            
            m_authorsF = new AuthorsFragment();
        }

        @Override
        public Fragment getItem(int i)
        {
            switch(i)
            {
            case 0:
                return m_everyBooksF;
            case 1:
                return m_toReadBooksF;
            case 2:
                return m_readBooksF;
            case 3:
                return m_authorsF;
            }
            return new Fragment();
        }

        @Override
        public int getCount()
        {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch(position)
            {
            case 0:
                return getString(R.string.title_section1).toUpperCase();
            case 1:
                return getString(R.string.title_section2).toUpperCase();
            case 2:
                return getString(R.string.title_section3).toUpperCase();
            case 3:
                return getString(R.string.title_section4).toUpperCase();
            }
            return null;
        }
    }
}
