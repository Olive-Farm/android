package com.farmer.olive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log


class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.e("@@@onReceive", "나와랏!")
        intent.extras?.let {
            val messages = parseSmsMessage(it)
            Log.e("@@@message", "메시지 : $messages")
            messages?.let { messageList ->
                messageList.forEach { message ->
                    val sender = message?.originatingAddress
                    val content = message?.messageBody.toString()
                    Log.e("@@@sender", "sender : ${sender}")
                    Log.e("@@@content", "content : ${content}")
                }
            }
        }
    }

    private fun parseSmsMessage(bundle: Bundle): Array<SmsMessage?>? {
        // PDU: Protocol Data Units
        val objs = bundle["pdus"] as Array<Any>?
        val messages: Array<SmsMessage?> = arrayOfNulls<SmsMessage>(objs!!.size)
        for (i in objs!!.indices) {
            messages[i] = SmsMessage.createFromPdu(objs[i] as ByteArray)
        }
        return messages
    }
}