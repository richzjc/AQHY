package com.micker.seven.activity

import android.content.Intent
import android.view.View
import com.micker.core.base.BaseActivity
import com.micker.core.base.BasePresenter
import com.micker.helper.TLog
import com.micker.helper.snack.MToastHelper
import com.micker.home.R
import com.micker.seven.const.list
import com.micker.seven.model.SevenModelEnitity
import kotlinx.android.synthetic.main.aqhy_activity_seven_stage.*
import java.util.regex.Pattern

class SevenActivity : BaseActivity<Any, BasePresenter<Any>>() {

    private var isPlay = false
    override fun doGetContentViewId() = R.layout.aqhy_activity_seven_stage

    override fun doInitSubViews(view: View) {
        super.doInitSubViews(view)
        updateData()
        setListener()
    }

    private fun updateData() {
        var position = intent.getIntExtra("position", 0)
        val entity = list[position]
        regex(entity)
        title_view?.text = "${entity.title}\n ---${entity.author}"
        sv?.scrollTo(0, 0)
    }

    private fun setListener() {
        last?.setOnClickListener {
            var position = intent.getIntExtra("position", 0)
            var newPosition = position - 1
            if (newPosition < 0)
                newPosition = list.size - 1

            intent.putExtra("position", newPosition)
            updateData()
            isPlay = false
            play?.text = "播放"
            poetry?.updatePlayStatus(isPlay)
        }

        next?.setOnClickListener {
            var position = intent.getIntExtra("position", 0)
            var newPosition = position + 1
            if (newPosition >= list.size)
                newPosition = 0

            intent.putExtra("position", newPosition)
            updateData()
            isPlay = false
            play?.text = "播放"
            poetry?.updatePlayStatus(isPlay)
        }

        play?.setOnClickListener {
            isPlay = !isPlay
            if (isPlay)
                play?.text = "暂停"
            else
                play?.text = "播放"

            poetry?.updatePlayStatus(isPlay)
        }

        test?.setOnClickListener {
            isPlay = false
            poetry?.updatePlayStatus(isPlay)
            val intent = Intent(this, SevenTestActivity::class.java)
            intent.putExtras(getIntent())
            startActivity(intent)
        }
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
        poetry?.bindData(resultList)

    }

}