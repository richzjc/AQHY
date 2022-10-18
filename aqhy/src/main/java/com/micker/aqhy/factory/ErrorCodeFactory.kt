package com.micker.aqhy.factory

import com.micker.aqhy.factory.action.ErrorTokenAction
import com.micker.data.constant.ERR_ILLEGAL_TOKEN
import com.micker.rpc.exception.BaseErrorCodeFactory
import com.micker.rpc.exception.ErrCodeMsgEntity
import com.micker.rpc.exception.IErrorAction

class ErrorCodeFactory : BaseErrorCodeFactory {

    override fun getAction(entity: ErrCodeMsgEntity?): IErrorAction? {
        return when(entity?.succ){
            ERR_ILLEGAL_TOKEN -> ErrorTokenAction.getInstance()
            else -> null
        }
    }
}