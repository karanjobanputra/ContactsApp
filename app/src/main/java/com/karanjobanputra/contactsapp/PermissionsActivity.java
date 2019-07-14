package com.karanjobanputra.contactsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class PermissionsActivity extends AppCompatActivity {
    String permissions[] = {Manifest.permission.READ_CONTACTS};
    final int REQUEST_PERMISSION_READ_CONTACTS = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        if( ! isPermissionGranted() ){
            requestPermissions(permissions, REQUEST_PERMISSION_READ_CONTACTS);
        } else {
            startMainActivity();
        }
    }

    private boolean isPermissionGranted() {
        return checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch ( requestCode ){
            case REQUEST_PERMISSION_READ_CONTACTS :
                if ( checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED ){
                    startMainActivity();
                } else {
                    Toast.makeText(this,R.string.please_grant_permission,Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
