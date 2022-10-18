package com.micker.aqhy.util

import android.content.Context
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXTextObject
import com.tencent.mm.opensdk.openapi.WXAPIFactory

fun setMsgToWX(context: Context) {
    try {
        val apiFactory = WXAPIFactory.createWXAPI(context, "wx3d2687b42605ddf4")
        val textObj = WXTextObject();
        textObj.text = "发送消息了哟"

        val msg =  WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = "发送消息了哟"

        val req = SendMessageToWX.Req();
        req.transaction = "${System.currentTimeMillis()}"
        req.message = msg
        apiFactory.sendReq(req)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}