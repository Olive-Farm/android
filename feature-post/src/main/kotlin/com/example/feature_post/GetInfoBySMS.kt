package com.example.feature_post

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*
import com.farmer.data.History
import com.farmer.data.OliveDao
import com.farmer.data.network.OliveApi
import com.farmer.data.repository.OliveRepository
import com.farmer.data.repository.OliveRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


fun readSMSMessage(cr: ContentResolver, viewModel: PostViewModel) {

    val messageList: MutableList<Message?> = mutableListOf()
    Log.d("@@@시작성공", viewModel.toString())
    val allMessage: Uri = Uri.parse("content://sms/inbox")


    val query_set: Array<String> = arrayOf("address", "date", "body")
    val c: Cursor? = cr.query(allMessage, query_set, null, null, "date DESC")

    while (c!! != null && c!!.moveToNext()) {

        var msg = Message()
        var address = ""               //번호 추출
        if (c.getType(0) == Cursor.FIELD_TYPE_STRING) {
            address = c.getString(0)
        }
        msg.address = address


        var timestamp: Long = c.getLong(1)             //시간 및 timeStamp로 문자 수신 년도 추출
        msg.timeStamp = timestamp
        msg.year = getDateTime(msg.timeStamp)


        var body: String = ""                                  //문자 내용 추출
        if (c.getType(2) == Cursor.FIELD_TYPE_STRING) {
            body = c.getString(2)
        }
        msg.body = body

        messageList.add(msg)

        parseSMS(msg, viewModel)


    }

    c.close();

}


fun parseSMS(message: Message, viewModel: PostViewModel) {

    val regex_total = """([0-9,]*)원\s(.+)\s([0-9]*\/[0-9]*)\s([0-9]*:[0-9]*)\s(.+)\s(?:.+원)""".toRegex()
    val regex_not_total = """([0-9,]*)원\s(.+)\s([0-9]*\/[0-9]*)\s([0-9]*:[0-9]*)\s(.+)""".toRegex()

    var (amount, inst, date, time, memo) = arrayOf<String>("-", "-", "-", "-", "-")

    var matchResult_total = regex_total.find(message.body)
    var matchResult_non_total = regex_not_total.find(message.body)
    if (matchResult_total == null && matchResult_non_total == null){
        return
    }

    try {

        amount = matchResult_total!!.destructured.component1()
        inst = matchResult_total!!.destructured.component2()
        date = matchResult_total!!.destructured.component3()
        time = matchResult_total!!.destructured.component4()
        memo = matchResult_total!!.destructured.component5()

    } catch (e: Exception) {
        amount = matchResult_non_total!!.destructured.component1()
        inst = matchResult_non_total!!.destructured.component2()
        date = matchResult_non_total!!.destructured.component3()
        time = matchResult_non_total!!.destructured.component4()
        memo = matchResult_non_total!!.destructured.component5()
    }

        // date 분리 (월,일 따로)
    val str = date
    var arr = str.split("/")
    amount = amount!!.replace(",", "")

    val sms_year = message.year.toInt()
    val sms_month = arr[0].toInt()
    val sms_day = arr[1].toInt()
    val sms_amount = amount.toInt()


    viewModel.nameSMS = memo
    viewModel.amountSMS = sms_amount
    viewModel.yearSMS = sms_year
    viewModel.monthSMS = sms_month
    viewModel.daySMS = sms_day

 /*   Log.d("@@@추출 성공한 문자", message.body)
    Log.d("@@@추출내용", viewModel.nameSMS)
    Log.d("@@@추출금액", viewModel.amountSMS.toString())
    Log.d("@@@추출년도", viewModel.yearSMS.toString())
    Log.d("@@@추출월자", viewModel.monthSMS.toString())
    Log.d("@@@추출일자", viewModel.daySMS.toString())*/

    viewModel.postSMSData()
    viewModel.refreshState()

}


fun getDateTime(s: Long): String {              //카드 사용의 년도 추출
    try {
        val sdf = SimpleDateFormat("yyyy")
        val netDate = Date(s)
        return sdf.format(netDate)
    } catch (e: Exception) {
        return e.toString()
    }
}
