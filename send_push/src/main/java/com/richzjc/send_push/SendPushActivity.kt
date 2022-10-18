package com.richzjc.send_push

import com.micker.core.base.BaseActivity
import kotlinx.android.synthetic.main.send_activity_push.*

class SendPushActivity : BaseActivity<SendPushCallback, SendPushPresenter>(), SendPushCallback {

    var sign: String? = ""
    var taskID: String? = ""

    override fun doGetContentViewId() = R.layout.send_activity_push

    override fun doGetPresenter(): SendPushPresenter? {
        return SendPushPresenter()
    }

    override fun doInitData() {
        super.doInitData()
        send_push?.setOnClickListener {
            mPresenter?.sendPush()
        }

        search_status?.setOnClickListener {
            mPresenter?.searchStatus(sign, taskID)
        }
    }

    override fun getSign(sign: String?) {
        this.sign = sign
    }

    override fun getTaskId(taskId: String?) {
        this.taskID = taskId
    }
}