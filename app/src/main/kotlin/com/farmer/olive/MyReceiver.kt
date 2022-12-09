package com.farmer.olive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmer.home.data.CashBookRepository
import com.farmer.home.ui.states.CalendarViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime


class MyReceiver : BroadcastReceiver() {

    @Composable
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

                    val regex = """([0-9]*,[0-9]*,*[0-9]*)*원 (.+) ([0-9]*\/[0-9]*) ([0-9]*:[0-9]*) (.+) """.toRegex()
                    val matchResult = regex.find(content)
                    val (amount, inst, date, time, memo) = matchResult!!.destructured
                    val datetime = date.replace("/", "-")
                    val arrangetime = LocalDateTime.now().year.toString() + "-" + datetime + " " + time
                    Log.d("시간 추출 ", arrangetime)
                    Log.d("할부 추출 ", inst)
                    Log.d("금액 추출 ", amount)
                    Log.d("내역 추출 ", memo)

                }
            }
        }
    }

//    override fun onReceive(context: Context?, intent: Intent?) {
//        if(intent?.action.equals("android.provider.Telephony.SMS_RECEIVED")){
//            val bundle = intent?.extras
//            val messages = parseSmsMessage(bundle!!)
//
//            if(messages?.size!! > 0){
//                val content = messages[0]?.messageBody.toString()
//                val amount = content?.replace("(([0-9]*,*[0-9]*)*원) ".toRegex(),"").toString()
//                val date = content?.replace("([0-9]*\\/[0-9]*) ".toRegex(),"").toString()
//                val regex = """([0-9]*:[0-9]*) (.+)""".toRegex()
//                val matchResult = regex.find("Mickey Mouse is 95 years old")
//                val (time, memo) = matchResult!!.destructured
//                Log.d("날짜 추출 ",content)
//                Log.d("날짜 추출 ",date)
//                Log.d("금액 추출 ",amount)
//                Log.d("시간 추출 ", time)
//                Log.d("시간 추출 ", memo)
//            }
//        }
//    }


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