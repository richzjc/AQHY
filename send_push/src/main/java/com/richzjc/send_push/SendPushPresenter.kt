package com.richzjc.send_push

import android.util.Log
import com.micker.core.base.BasePresenter
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class SendPushPresenter : BasePresenter<SendPushCallback>() {
    fun sendPush() {
        thread {
            val startTime = System.currentTimeMillis();
            val timestamp = (startTime / 1000).toInt().toString()

            val jobj = JSONObject()
            jobj.put("timestamp", timestamp)
            jobj.put("appkey", "5e943dfa895ccae948000182")
            jobj.put("type", "broadcast")
            jobj.put("production_mode", true)

            val payLoadJobj = JSONObject()
            payLoadJobj.put("display_type", "notification")
            val bodyJobj = JSONObject()
            bodyJobj.put("ticker", "有新作品发布啦， 快来找找灵感")
            bodyJobj.put("title", "有新作品发布啦， 快来找找灵感")
            bodyJobj.put("text", "灵感来源于不断的模仿，创作")
            bodyJobj.put("after_open", "go_app")
            payLoadJobj.put("body", bodyJobj)
            jobj.put("payload", payLoadJobj)

            val policyJobj = JSONObject()
            val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, 2)
            policyJobj.put("expire_time", format.format(Date(calendar.timeInMillis)))
            jobj.put("policy", policyJobj)

            jobj.put("description", "灵感来源于不断的模仿，创作")
            Log.i("jobj", jobj.toString())

            var url = "$host$postPath"
            val postBody = jobj.toString()
            val sign = MyMD5.getSignValue("POST$url${postBody}yi3x7bpdwdzfco16d7oev5leom0ausro")
            url = "$url?sign=$sign"


            val JSON = MediaType.parse("application/json; charset=utf-8");
            val client = OkHttpClient()

            val body = RequestBody.create(JSON, postBody);
            val request = Request.Builder()
                .header("User-Agent", USER_AGENT)
                .url(url)
                .post(body)
                .build()
            val response = client.newCall(request).execute();

            Log.i("code", "code = ${response.code()}")
            Log.i("code", postBody)
            if (response.code() == 200) {
                viewRef?.getSign(sign)
                val jobj = JSONObject(response.body()?.string())
                val data = jobj.optString("data", "")
                val taskObj = JSONObject(data)
                viewRef?.getTaskId(taskObj.optString("task_id"))
            }
        }
    }

    fun searchStatus(sign: String?, taskID: String?) {
        thread {

            val startTime = System.currentTimeMillis();
            val timestamp = (startTime / 1000).toInt().toString()

            val jobj = JSONObject()
            jobj.put("timestamp", timestamp)
            jobj.put("appkey", "5e943dfa895ccae948000182")
            jobj.put("task_id", taskID)

            var url = "$host/api/status"
            val postBody = jobj.toString()
            val sign = MyMD5.getSignValue("POST$url${postBody}yi3x7bpdwdzfco16d7oev5leom0ausro")
            url = "$url?sign=$sign"


            val JSON = MediaType.parse("application/json; charset=utf-8");
            val client = OkHttpClient()

            val body = RequestBody.create(JSON, postBody);
            val request = Request.Builder()
                .header("User-Agent", USER_AGENT)
                .url(url)
                .post(body)
                .build()
            val response = client.newCall(request).execute();

            Log.i("code", "code = ${response.code()}")
            Log.i("code", "body = ${response.body()?.string()}")
            if (response.code() == 200) {

            }
        }
    }

    companion object {
        protected const val host = "http://msg.umeng.com"

        // The upload path
        protected const val uploadPath = "/upload"

        // The post path
        protected const val postPath = "/api/send"

        protected const val USER_AGENT = "Mozilla/5.0"
    }
}