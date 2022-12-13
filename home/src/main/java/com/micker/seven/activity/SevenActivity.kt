package com.micker.seven.activity

import android.view.View
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.helper.TLog
import com.micker.helper.snack.MToastHelper
import com.micker.home.R
import com.micker.seven.const.list
import com.micker.seven.model.SevenModelEnitity
import java.util.regex.Pattern

class SevenActivity : BaseActivity<Any, BasePresenter<Any>>() {

    private val position by lazy {
        intent.getIntExtra("position", 0)
    }

    override fun doGetContentViewId() = R.layout.aqhy_activity_first_stage

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        val entity = list[position]
        regex(entity)
    }

    private fun regex(entity: SevenModelEnitity) {
        val splitStr = "。，？；！、"
        val length = splitStr.length
        val value = entity.content.replace("\n", "")
        val resultList = ArrayList<String>()
        resultList.add(value)
        (0 until length)?.forEach {
            val list = ArrayList<String>()
            val pattern = Pattern.compile(splitStr[it].toString())
            resultList.forEach {
                val matcher = pattern.matcher(it)
                var index = 0
                while (matcher.find()) {
                    val end = matcher.end()
                    list.add(it.subSequence(index, end).toString())
                    index = end
                }

                if (index < it.length) {
                    list.add(it.substring(index, it.length))
                }
            }
            resultList.clear()
            resultList.addAll(list)
        }

        resultList.forEach {
            TLog.e("@@@", "${it}")
        }
    }

}