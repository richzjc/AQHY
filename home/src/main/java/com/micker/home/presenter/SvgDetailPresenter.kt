package com.micker.home.presenter

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.micker.core.base.BasePresenter
import com.micker.data.model.home.*
import com.micker.global.util.UploadImageUtils
import com.micker.helper.file.CacheUtils
import com.micker.helper.file.FileUtil
import com.micker.helper.file.QDUtil
import com.micker.helper.file.QDUtil.getShareImageCache
import com.micker.helper.snack.MToastHelper
import com.micker.home.R
import com.micker.home.api.ColorGroupByTypeApi
import com.micker.home.api.SvgPathApi
import com.micker.home.callback.SvgDetailCallback
import com.micker.home.utils.ClipUtil
import com.micker.rpc.ResponseListener
import com.micker.webview.Widget.WSCNWebView
import org.json.JSONObject
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.net.URLDecoder

class SvgDetailPresenter(val svgUrl: String?) : BasePresenter<SvgDetailCallback>() {
    private val colorListMap by lazy { mutableMapOf<Int, ColorGroupDetailEntity>() }

    fun loadPath(callbackId: String?) {
        SvgPathApi(object : ResponseListener<String> {
            override fun onSuccess(model: String?, isCache: Boolean) {
                viewRef?.loadSvgSucc(model, callbackId)
            }

            override fun onErrorResponse(code: Int, error: String?) {
                Log.i("path", error!!)
                viewRef?.loadSvgError()
            }
        }, svgUrl).start()
    }

    fun loadColorType() {
        val list = ArrayList<ColorCategoryEntity>()
        val entity1 = ColorCategoryEntity()
        entity1.name = "单色"
        entity1.color = "#ed9cab"
        entity1.type = ColorTypeEntity.MaterialTypeSolidColor
        list.add(entity1)
        val entity2 = ColorCategoryEntity()
        entity2.name = "渐变"
        entity2.webColors = arrayOf("#ed9cab", "#ada3cc")
        entity2.type = ColorTypeEntity.MaterialTypeLinearColor
        list.add(entity2)
        val entity3 = ColorCategoryEntity()
        entity3.name = "放射"
        entity3.type = ColorTypeEntity.MaterialTypeGradialColor
        entity3.webColors = arrayOf("#ed9cab", "#ada3cc")
        list.add(entity3)
        val entity4 = ColorCategoryEntity()
        entity4.name = "模板"
        entity4.type = ColorTypeEntity.MaterialTypePattern
        entity4.image = "http://image.314.la/dcf03ca110d079ef261f44af11fe558c"
        list.add(entity4)
        val entity5 = ColorCategoryEntity()
        entity5.name = "滤镜"
        entity5.type = ColorTypeEntity.MaterialTypeFilter
        entity5.image = "http://image.314.la/dcf03ca110d079ef261f44af11fe558c"
        list.add(entity5)
        viewRef?.loadColorCategory(list)
    }

    fun loadColorListByCategory(type: Int) {
        if (colorListMap.containsKey(type)) {
            viewRef?.loadColorList(colorListMap[type])
        } else {
            val bundle = Bundle()
            bundle.putInt("page", 1)
            bundle.putInt("pageSize", 50)
            bundle.putInt("type", type)
            ColorGroupByTypeApi(object : ResponseListener<List<ColorGroupByTypeEntity>> {
                override fun onSuccess(model: List<ColorGroupByTypeEntity>?, isCache: Boolean) {
                    val entity = ColorGroupDetailEntity.packageData(model)
                    colorListMap.put(type, entity)
                    viewRef?.loadColorList(entity)
                }

                override fun onErrorResponse(code: Int, error: String?) {
                    MToastHelper.showToast("加载失败")
                }

            }, bundle).start()
        }
    }

    fun statusAction(args: JSONObject?) {
        try {
            val entity = JSON.parseObject<StatusActionEntity>(args?.toString(), StatusActionEntity::class.java)
            viewRef?.statusAction(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveData(args: JSONObject?, view: WSCNWebView?) {
        Log.i("height", "${view?.height}   ${view!!.contentHeight}  ${view!!.scale}")
        val bitmap = ClipUtil.clipWebView(view)
        if (bitmap != null) {
            val path = QDUtil.saveShareViewToDisk(view.context, bitmap)
            if (!TextUtils.isEmpty(path)) {
                Luban.with(view.context)
                    .load(path)
                    .ignoreBy(300)
                    .setTargetDir(File(getShareImageCache(view.context).absolutePath, "${System.currentTimeMillis()}").absolutePath)
                    .setCompressListener(object : OnCompressListener {
                        override fun onSuccess(file: File?) {
                            realUpload(path, args, view)
                        }

                        override fun onError(e: Throwable?) {
                            realUpload(path, args, view)
                        }

                        override fun onStart() {
                        }

                    })
                    .launch()
            }
        }
    }

    private fun realUpload(
        path: String?,
        args: JSONObject?,
        view: WSCNWebView?
    ) {
        UploadImageUtils.uploadImage(path, object : ResponseListener<String> {
            override fun onSuccess(updateImageUrl: String?, isCache: Boolean) {
                FileUtil.deleteFile(path)
                uploadSvg(args, updateImageUrl, view)
            }

            override fun onErrorResponse(code: Int, error: String?) {
                FileUtil.deleteFile(path)
                viewRef?.uploadError()
            }
        }, "")
    }

    private fun uploadSvg(
        args: JSONObject?,
        updateImageUrl: String?,
        view: WSCNWebView?
    ){
        val content = args?.optString("content", "")
        val filePath = "${FileUtil.CURRENT_PATH}${File.separator}${CacheUtils.CACHE_RESOURCE}${File.separator}svg.svg"
        val flag = FileUtil.saveFile(filePath, URLDecoder.decode(content, "utf-8"))
        if(flag){
            UploadImageUtils.uploadImage(filePath, object : ResponseListener<String> {
                override fun onSuccess(contentUrl: String?, isCache: Boolean) {
                    FileUtil.deleteFile(filePath)
                    val entity = view?.getTag(R.id.tag_sub_item_entity) as? HomeSubItemEntity
                    val bundle = Bundle()
                    bundle.putString("contentUrl", contentUrl)
                    bundle.putString("imageUrl", updateImageUrl)
                    bundle.putParcelable("entity", entity)
                    viewRef?.uploadSuccess(bundle)
                }

                override fun onErrorResponse(code: Int, error: String?) {
                    FileUtil.deleteFile(filePath)
                    viewRef?.uploadError()
                }
            }, "image/svg+xml")
        }else{
            viewRef?.uploadError()
        }
    }
}