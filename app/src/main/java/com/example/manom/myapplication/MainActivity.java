package com.example.manom.myapplication;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Map;

import android.database.Cursor;
import android.widget.Toast;


import java.util.Vector;


public class MainActivity extends AppCompatActivity {
    private static final int CONTACT_PICKER_RESULT = 1001;
    private MyDBHandler dbHandler;
    //public SQLiteDatabase db = dbHandler.getWritableDatabase();

    public void doLaunchContactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final TextView textView=findViewById(R.id.text);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String phone = "";

                    Uri result = data.getData();

                    String id = result.getLastPathSegment();

                    cursor = getContentResolver().query(
                            CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);

                    if (cursor.moveToFirst()) {
                        String name="";
                        String phoneNum="";
                        name= cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME));
                        phoneNum= cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER));

                        textView.setText(name+" "+ phoneNum);

                       PhoneList friend = new PhoneList(name, phoneNum);
                        cursor.close();
                        dbHandler.addHandler(friend);
                        dbHandler.printAll();
                    }
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler= new MyDBHandler(this, null, null, 1);
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        if (dbHandler != null) {
            dbHandler.close();
        }
        dbHandler = null;
    }
}
