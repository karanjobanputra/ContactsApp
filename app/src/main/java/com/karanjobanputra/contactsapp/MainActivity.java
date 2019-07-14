package com.karanjobanputra.contactsapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView contactsRecyclerView;
    private SimpleCursorRecyclerAdapter contactsRecyclerAdapter;
    private static String[] projectionFields = new String[] { ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_URI };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        getLoaderManager().initLoader(0,null,this);
    }

    private void initViews(){
        contactsRecyclerView = findViewById(R.id.contactRecyclerView);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsRecyclerView.setHasFixedSize(false);
        contactsRecyclerAdapter = new SimpleCursorRecyclerAdapter(this,
                R.layout.contacts_list_item,
                null,
                contactsRecyclerView
        );
        contactsRecyclerView.setAdapter(contactsRecyclerAdapter);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(this,
                ContactsContract.Contacts.CONTENT_URI, // URI
                projectionFields,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+" ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        contactsRecyclerAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        contactsRecyclerAdapter.swapCursor(null);
    }
}
