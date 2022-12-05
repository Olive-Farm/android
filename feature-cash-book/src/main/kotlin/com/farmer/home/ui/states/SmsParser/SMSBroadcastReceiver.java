package com.farmer.home.ui.states.SmsParser;

import android.content.BroadcastReceiver;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent recieved: " + intent.getAction());

        if (intent.getAction() == SMS_RECEIVED) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                if (messages.length > -1) {
                    Log.i(TAG, "Message recieved: " + messages[0].getMessageBody());
                    String line = messages[0].getMessageBody();


                   // String line = "[우리]3/8 19:53 1000-534-**** 입금 4,800,000원 전자금융";
                    String pattern = "입금 (.+)원 (.+)";


                    Pattern r = Pattern.compile(pattern); // Create a Pattern object
                    Matcher m = r.matcher(line); // Now Create matcher object

                    TextView tv = MainActivity.tv;

                    if(m.find()) {
                        tv.setText(m.group(0) + "\n" + m.group(1) + "\n" + m.group(2));
                    }else {
                        tv.setText("NO MATCH");
                    }
                }
            }
        }
    }

}

//activity_main.xml 파일에 <textview 쪽에
//android:id="@+id/text" 추가해야 하는데 xml 파일이 없음