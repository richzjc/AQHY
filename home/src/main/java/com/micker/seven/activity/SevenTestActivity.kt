package com.micker.seven.activity

import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.home.R
import com.micker.seven.const.list
import com.micker.seven.model.SevenModelEnitity
import kotlinx.android.synthetic.main.aqhy_activity_seven_stage_test.*
import java.util.regex.Pattern

class SevenTestActivity : BaseActivity<Any, BasePresenter<Any>>() {

    override fun doGetContentViewId() = R.layout.aqhy_activity_seven_stage_test

    override fun doInitData() {
        super.doInitData()
        updateData()
    }

    private fun updateData() {
        var position = intent.getIntExtra("position", 0)
        val entity = list[position]
        regex(entity)
        title_view?.text = "${entity.title}\n ---${entity.author}"
        sv?.scrollTo(0, 0)
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
                    list.add(it.substring(index, it.length).replace(" ", ""))
                }
            }
            resultList.clear()
            resultList.addAll(list)
        }
        poetry?.bindDataTest(resultList)
    }
}