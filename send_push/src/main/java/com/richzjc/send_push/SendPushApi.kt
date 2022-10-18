package com.richzjc.send_push

import android.util.Log
import com.micker.rpc.CustomJsonApi
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class SendPushApi : CustomJsonApi<String>() {

    private var timeStampe : String = ""
    private var timeMills : Long  = 0
    init {
        timeMills = System.currentTimeMillis()
        timeStampe = (timeMills / 1000).toInt().toString()
    }

    override fun getUrl() = "http://msg.umeng.com/api/send?sign=${getSign()}"

    override fun getRequestJSONBody(): JSONObject {
        val jobj = JSONObject()
        jobj.put("appkey", "5e943dfa895ccae948000182")
        jobj.put("timestamp", timeStampe)
        jobj.put("type", "broadcast")
        val payLoadJobj = JSONObject()
        payLoadJobj.put("display_type", "notification")
        val bodyJobj = JSONObject()
        bodyJobj.put("ticker", "有新作品发布啦， 快来找找灵感吧")
        bodyJobj.put("title", "有新作品发布啦， 快来找找灵感吧")
        bodyJobj.put("text", "灵感是高潮，写在纸上是射精")
        bodyJobj.put("after_open", "go_app")
        payLoadJobj.put("body", bodyJobj)
        jobj.put("payload", payLoadJobj)

        val policyJobj = JSONObject()
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        policyJobj.put("start_time",format.format(Date(timeMills)))
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        policyJobj.put("expire_time", format.format(Date(calendar.timeInMillis)))
        jobj.put("policy", policyJobj)

        jobj.put("description", "有新作品发布啦， 快来看看")
        Log.i("jobj", jobj.toString())
        return jobj
    }


    private fun getSign() : String{
        val buidler = StringBuilder()
        buidler.append("POST").append("http://msg.umeng.com/api/send")

        val jobj = JSONObject()
        jobj.put("appkey", "5e943dfa895ccae948000182")
        jobj.put("timestamp", timeStampe)
        jobj.put("type", "broadcast")
        val payLoadJobj = JSONObject()
        val bodyJobj = JSONObject()
        bodyJobj.put("ticker", "有新作品发布啦， 快来找找灵感吧")
        bodyJobj.put("title", "有新作品发布啦， 快来找找灵感吧")
        bodyJobj.put("text", "灵感是高潮，写在纸上是射精")
        bodyJobj.put("after_open", "go_app")
        payLoadJobj.put("body", bodyJobj)

        payLoadJobj.put("display_type", "notification")

        jobj.put("payload", payLoadJobj)

        buidler.append(jobj.toString())
        buidler.append("yi3x7bpdwdzfco16d7oev5leom0ausro")
        val sign = MyMD5.getSignValue(buidler.toString())
        Log.i("jobj", "sign = ${sign}")
        return sign
    }

}