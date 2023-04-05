package com.farmer.olive

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.database.Cursor.FIELD_TYPE_STRING
import android.net.Uri
import android.util.Log
import com.farmer.home.model.response.Message
import java.text.SimpleDateFormat
import java.util.*

fun readSMSMessage(context: Context) {
    val messageList : MutableList<Message> = mutableListOf()

    val allMessage : Uri = Uri.parse("content://sms/inbox")
    val cr : ContentResolver = context.contentResolver

    val query_set : Array<String> = arrayOf("address","date", "body")
    val c: Cursor? = cr.query(allMessage, query_set, null, null, "date DESC")

    while(c!! != null && c!!.moveToNext()) {

        var msg : Message = Message()

        var address : String = ""               //번호 추출
        if (c.getType(0) == FIELD_TYPE_STRING) {
            address = c.getString(0)
        }
        msg.address = address


        var timestamp : Long = c.getLong(1)             //시간 및 timeStamp로 문자 수신 년도 추출
        msg.timeStamp = timestamp
        msg.year = getDateTime(msg.timeStamp)

        var body : String = ""                                  //문자 내용 추출
        if (c.getType(2) == FIELD_TYPE_STRING) {
            body= c.getString(2)
        }
        msg.body = body

        messageList.add(msg)

        parseSMS(msg)
    }

    c.close();

}

fun parseSMS (message : Message) {
    val regex_total = """([0-9]*,[0-9]*,*[0-9]*)원 (.+) ([0-9]*\/[0-9]*) ([0-9]*:[0-9]*) (.+)\s(?:.+원)""".toRegex()
    val regex_not_total = """([0-9]*,[0-9]*,*[0-9]*)원 (.+) ([0-9]*\/[0-9]*) ([0-9]*:[0-9]*) (.+)""".toRegex()

    var (amount, inst, date, time, memo) = arrayOf<String>("-", "-", "-", "-", "-")

    message.body = message.body.replace("\n", " ").replace("("," ").replace(")", " ")

    try {
        var matchResult_total = regex_total.find(message.body)
        var matchResult_non_total = regex_not_total.find(message.body)
        try{

            amount=matchResult_total!!.destructured.component1()
            inst=matchResult_total!!.destructured.component2()
            date=matchResult_total!!.destructured.component3()
            time=matchResult_total!!.destructured.component4()
            memo=matchResult_total!!.destructured.component5()

        } catch(e: Exception){
            amount=matchResult_non_total!!.destructured.component1()
            inst=matchResult_non_total!!.destructured.component2()
            date=matchResult_non_total!!.destructured.component3()
            time=matchResult_non_total!!.destructured.component4()
            memo=matchResult_non_total!!.destructured.component5()
        }

        date = date!!.replace("/", "-")
        var arrangetime = message.year + "-" + date + " " + time + ":" + "00"
        amount = amount!!.replace(",", "")
        Log.d("timestamp", message.timeStamp.toString())
        Log.d("시간 추출 ", arrangetime)
        Log.d("할부 추출 ", inst)
        Log.d("금액 추출 ", amount)
        Log.d("내역 추출 ", memo)
        Log.d(TAG, "--------------------------------------------")
    }
    catch (e: Exception) {
    }

}

private fun getDateTime(s: Long): String {
    try {
        val sdf = SimpleDateFormat("yyyy")
        val netDate = Date(s)
        return sdf.format(netDate)
    } catch (e: Exception) {
        return e.toString()
    }
}
