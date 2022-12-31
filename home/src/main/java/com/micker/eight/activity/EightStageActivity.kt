package com.micker.eight.activity

import android.view.View
import com.kronos.router.BindRouter
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.home.R
import kotlinx.android.synthetic.main.aqhy_activity_eight_stage.*

@BindRouter(urls = ["wscn://aqhy.com/eight/stage/action"])
class EightStageActivity : BaseActivity<Any, BasePresenter<Any>>() {

    private var isStart : Boolean = false
    override fun doGetContentViewId() =  R.layout.aqhy_activity_eight_stage

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        setListener()
    }

    private fun setListener(){
        start?.setOnClickListener {
            if(isStart) {
                eight_view?.end()
                start?.text = "开始"
                isStart = false
            }else{
                eight_view?.start()
                start?.text = "结束"
                isStart = true
            }
        }
    }

}