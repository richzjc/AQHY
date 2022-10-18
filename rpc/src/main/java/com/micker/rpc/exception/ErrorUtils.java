package com.micker.rpc.exception;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.micker.helper.snack.MToastHelper;
import com.micker.rpc.VolleyQueue;

/**
 * Created by zhangyang on 16/4/12.
 */
public class ErrorUtils {

    public static void responseFailed(String msgs, boolean isNeedToast, int statusCode) {
        if (!TextUtils.isEmpty(msgs)) {
            ErrCodeMsgEntity entity = JSON.parseObject(msgs, ErrCodeMsgEntity.class);
            responseToEntity(entity, isNeedToast);
        } else if (statusCode == ErrorCode.EMPTYURL && isNeedToast) {
            //   MToastHelper.showToast(ResourceUtils.getResStringFromId(R.string.rpc_network_request_timeout));
        }
    }

    private static void responseToEntity(ErrCodeMsgEntity entity, boolean isNeedToast) {
        if (entity != null) {
            BaseErrorCodeFactory factory = VolleyQueue.getInstance().getFactory();
            IErrorAction action = null;
            if (factory != null)
                action = factory.getAction(entity);
            if (action != null) {
                action.responseToErrCode();
            } else if (isNeedToast && !TextUtils.isEmpty(entity.msg)) {
                MToastHelper.showToast(entity.msg);
            }
        }
    }
}
