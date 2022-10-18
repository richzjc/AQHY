package com.richzjc.send_push

interface SendPushCallback {

    fun getSign(sign : String?)
    fun getTaskId(taskId : String?)
}