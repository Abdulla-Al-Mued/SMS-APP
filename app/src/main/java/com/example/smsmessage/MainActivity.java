package com.example.smsmessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.Toast;

import com.example.smsmessage.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private MessageAdapter adapter;

    private List<SmsMessage> smsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        smsList = new ArrayList<>();

        adapter = new MessageAdapter(smsList, this);
        binding.smsRecyclerView.setAdapter(adapter);
        binding.smsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);

        }
        else {
            // Permission is already granted, proceed with reading SMS
            readSMS();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with reading SMS
                readSMS();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                // Permission denied, handle accordingly (e.g., show a message to the user)
            }
        }
    }

    private void readSMS() {

        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();


        Uri inboxUri = Uri.parse("content://sms/inbox");
        String[] projection = {"_id", "address", "body",Telephony.Sms.Inbox.DATE};

        Cursor cursor = getContentResolver().query(inboxUri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            List<SmsMessage> smsList1 = new ArrayList<>();

            do {
                @SuppressLint("Range") String sender = cursor.getString(cursor.getColumnIndex("address"));
                @SuppressLint("Range") String message = cursor.getString(cursor.getColumnIndex("body"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(Telephony.Sms.Inbox.DATE));


                SmsMessage sms = new SmsMessage(sender, message, date);
                smsList1.add(sms);
            } while (cursor.moveToNext());

            cursor.close();

            if (!smsList.isEmpty()){
                smsList.clear();
            }
            else {
                smsList.addAll(smsList1);
            }

            adapter.notifyDataSetChanged();


            // Now you have a list of SMS messages in smsList
            // You can pass this list to your RecyclerView adapter
            // and display them in the RecyclerView.
        }

    }
}