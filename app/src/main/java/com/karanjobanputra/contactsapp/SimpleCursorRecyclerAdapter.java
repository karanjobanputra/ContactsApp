package com.karanjobanputra.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.net.URI;

public class SimpleCursorRecyclerAdapter extends CursorRecyclerAdapter<SimpleViewHolder> {

    private int mLayout;
    private int nameColumn = -1;
    private int photoColumn = -1;
    private Context context;
    RecyclerView parentRecyvlerView;
    public SimpleCursorRecyclerAdapter (Context context, int layout, Cursor c, RecyclerView parentRecyclerView) {
        super(c);
        mLayout = layout;
        findColumns(c);
        this.context = context;
        this.parentRecyvlerView = parentRecyclerView;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(mLayout, parent, false);
        return new SimpleViewHolder(v);
    }

    @Override
    public void onBindViewHolder (SimpleViewHolder holder, final Cursor cursor) {
        if ( nameColumn != -1 && photoColumn != -1) {
            holder.tvFullName.setText(cursor.getString(nameColumn));
            final String photoUri = cursor.getString(photoColumn);
            if(photoUri != null){
                Uri uri = Uri.parse(photoUri);
                holder.ivUserIcon.setImageURI(uri);
            } else {
                holder.ivUserIcon.setImageDrawable(context.getDrawable(R.drawable.ic_user_default));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = parentRecyvlerView.getChildLayoutPosition(view);
                    cursor.moveToPosition(index);
                    Intent intent = new Intent(context,ContactDetailsActivity.class);
                    intent.putExtra("contactID",cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
                    intent.putExtra("fullName",cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    intent.putExtra("photoUri",photoUri);
                    context.startActivity(intent);
                }
            });
        }
    }

    private void findColumns(Cursor c) {
        if (c != null) {
            nameColumn = c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
            photoColumn = c.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI);
        }
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        findColumns(c);
        return super.swapCursor(c);
    }
}

class SimpleViewHolder extends RecyclerView.ViewHolder
{
    TextView tvFullName;
    ImageView ivUserIcon;
    public SimpleViewHolder (View itemView)
    {
        super(itemView);
        tvFullName = itemView.findViewById(R.id.tvFullName);
        ivUserIcon = itemView.findViewById(R.id.ivUserIcon);
    }
}
