package com.example.smsmessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    private MessageAdapter adapter;

    public SmsReceiver(MessageAdapter adapter) {
        this.adapter = adapter;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("SmsReceiver", "Received SMS");

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    android.telephony.SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);

                    // Get the sender's phone number and message body
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    String message = smsMessage.getMessageBody();
                    String date = String.valueOf(smsMessage.getTimestampMillis());

                    // Display a Toast with the sender and message
                    String toastText = "Message: " + message;
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

                    com.example.smsmessage.SmsMessage sms = new com.example.smsmessage.SmsMessage(sender, message, date);
                    adapter.addMessage(sms);
                }
            }
        }
    }
}
