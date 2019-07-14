package com.karanjobanputra.contactsapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactDetailsActivity extends AppCompatActivity {

    private String[] projectionsPhone = {ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER};
    private String[] projectionsEmail = {ContactsContract.CommonDataKinds.Email._ID,
            ContactsContract.CommonDataKinds.Email.ADDRESS};
    ListView listViewPhone;
    ListView listViewEmail;
    private List<String> listPoneNumbers;
    private List<String> listEmailAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        updateViews();
        updatePhoneData(getIntent().getStringExtra("contactID"));
        updateEmailData(getIntent().getStringExtra("contactID"));
    }

    private void updatePhoneData(String contactID) {
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projectionsPhone,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ? ",
                new String[]{contactID},
                null
        );

        int index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        while ( cursor.moveToNext() ){
            listPoneNumbers.add(cursor.getString(index));
        }
        updatePhoneAndCallButton();

        String bindFrom[] = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        int bindTo[] = {R.id.tvPhoneNumber};
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,R.layout.phone_list_item,
                cursor,
                bindFrom,
                bindTo,
                0
                );
        listViewPhone.setAdapter(simpleCursorAdapter);

    }
    private void updateEmailData(String contactID){
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                projectionsEmail,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID +" = ? ",
                new String[]{contactID},
                null
        );

        int index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
        while ( cursor.moveToNext() ){
            String emailAddress = cursor.getString(index);
            listEmailAddress.add(emailAddress);
        }

        String bindFrom[] = {ContactsContract.CommonDataKinds.Email.ADDRESS};
        int bindTo[] = {R.id.tvEmailAddress};
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,R.layout.email_list_item,
                cursor,
                bindFrom,
                bindTo,
                0
        );
        listViewEmail.setAdapter(simpleCursorAdapter);
    }

    private void updateViews() {
        listPoneNumbers = new ArrayList<>();
        listEmailAddress = new ArrayList<>();
        initPhoneListView();
        initEmailListView();
        initContactName();
        initPhoto();
    }

    private void initContactName(){
        String contactName = getIntent().getStringExtra("fullName");
        TextView tvContactName = findViewById(R.id.tvContactName);
        tvContactName.setText(contactName);
    }

    private void initPhoto(){
        String photoUri = getIntent().getStringExtra("photoUri");
        if( photoUri != null && photoUri.length() > 0 ) {
            ImageView imageView = findViewById(R.id.ivContactImage);
            imageView.setImageURI(Uri.parse(photoUri));
        }
    }

    private void initPhoneListView(){
        listViewPhone = findViewById(R.id.listViewPhone);
        listViewPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String phoneNumber = listPoneNumbers.get(i);
                dialNumber(phoneNumber);
            }
        });
    }

    private void initEmailListView(){
        listViewEmail = findViewById(R.id.listViewEmail);
        listViewEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String emailAddress = listEmailAddress.get(i);
                sendEmail(emailAddress);
            }
        });
    }

    public void onCallButtonClicked(View view){
        String phoneNumber = listPoneNumbers.get(0);
        dialNumber(phoneNumber);
    }

    public void onMessageButtonClicked(View view){
        String phoneNumber = listPoneNumbers.get(0);
        Uri numberUri = Uri.fromParts("sms", phoneNumber, null);
        Intent smsIntent = new Intent(Intent.ACTION_VIEW,numberUri);
        startActivity(smsIntent);
    }

    private void updatePhoneAndCallButton(){
        if( listPoneNumbers.size() == 0 ) {
            ImageView callIcon = findViewById(R.id.ivCallIcon);
            callIcon.setImageDrawable(getDrawable(R.drawable.call_icon_disabled));

            ImageView messageIcon = findViewById(R.id.ivMessageIcon);
            messageIcon.setImageDrawable(getDrawable(R.drawable.message_disabled));

            TextView tvCall = findViewById(R.id.tvCall);
            tvCall.setTextColor(getResources().getColor(R.color.disabled));

            TextView tvMessage = findViewById(R.id.tvMessage);
            tvMessage.setTextColor(getResources().getColor(R.color.disabled));

            findViewById(R.id.llCall).setClickable(false);
            findViewById(R.id.llMessage).setClickable(false);
        }
    }

    private void dialNumber(String phoneNumber){
        Uri numberUri = Uri.parse("tel:"+phoneNumber);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, numberUri);
        startActivity(callIntent);
    }

    private void sendEmail(String emailAddress){
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+emailAddress));
        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent);
        } else {
            Toast.makeText(this,R.string.no_application_found_to_handle,Toast.LENGTH_SHORT).show();
        }
    }
}
