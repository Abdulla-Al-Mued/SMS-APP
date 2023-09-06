package com.example.smsmessage;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.smsmessage.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static MessageAdapter adapter;
    private static MainActivity instance;
    private static List<SmsMessage> smsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        instance = this;

        smsList = new ArrayList<>();

        adapter = new MessageAdapter(smsList, this);
        binding.smsRecyclerView.setAdapter(adapter);
        binding.smsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.RECEIVE_SMS") != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    "android.permission.READ_SMS",
                    "android.permission.RECEIVE_SMS"
            }, REQUEST_CODE_ASK_PERMISSIONS);
        } else {

            readSMS();
        }


    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void readSMS() {

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
            smsList.addAll(smsList1);

            adapter.notifyDataSetChanged();

        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                readSMS();

            } else {

                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();

            }
        }
    }
}