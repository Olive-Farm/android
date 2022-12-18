package com.farmer.olive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final String TAG = "SMSBroadcastReceiver";

    public SMSBroadcastReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent recieved: " + intent.getAction());

        if (intent.getAction() == SMS_RECEIVED) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (messages.length > -1) {
                    Log.i(TAG, "Message recieved: " + messages[0].getMessageBody());
                    String line = messages[0].getMessageBody();


                    // String line = "[우리]3/8 19:53 1000-534-**** 입금 4,800,000원 전자금융";
                    String pattern = "입금 (.+)원 (.+)";


                    Pattern r = Pattern.compile(pattern); // Create a Pattern object
                    Matcher m = r.matcher(line); // Now Create matcher object

//                    String tv ="";

                    if(m.find()) {
                        System.out.println(m.group(0) + "\n" + m.group(1) + "\n" + m.group(2));
                    }else {
                        System.out.println("NO MATCH");
                    }
                }
            }
        }
    }
}
