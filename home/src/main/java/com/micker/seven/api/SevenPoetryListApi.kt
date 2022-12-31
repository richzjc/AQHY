package com.micker.seven.api

import com.kronos.volley.toolbox.BaseApiParser
import com.micker.rpc.CustomHtmlApi

class SevenPoetryListApi(val title : String?) : CustomHtmlApi<String>() {

    override fun getUrl() = "https://haokan.baidu.com/web/search/page?query=${title}&sfrom=recommend"

    override fun getParser(): BaseApiParser {
        return super.getParser()
    }
}