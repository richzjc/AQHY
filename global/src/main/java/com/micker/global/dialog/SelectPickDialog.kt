package com.micker.global.dialog

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.FragmentActivity
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.engine.GlideEngine
import com.huantansheng.easyphotos.listener.FinishSelectListener
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.micker.global.R
import com.micker.helper.snack.MToastHelper
import com.micker.helper.system.ScreenUtils
import com.richzjc.dialoglib.base.BaseDialogFragment
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissionsNew
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.global_dialog_select_pic.*
import java.util.ArrayList

class SelectPickDialog : BaseDialogFragment(), ObservableOnSubscribe<Int> {

    private var emmiter: ObservableEmitter<Int>? = null

    override fun doGetContentViewId() = R.layout.global_dialog_select_pic

    override fun doInitData() {
        take_photo?.setOnClickListener {
            dismiss()
            emmiter!!.onNext(1)
            emmiter!!.onComplete()
        }

        get_from_pic?.setOnClickListener {
            dismiss()
            emmiter!!.onNext(2)
            emmiter!!.onComplete()
        }

        cancle?.setOnClickListener {
            dismiss()
            emmiter!!.onNext(0)
            emmiter!!.onComplete()
        }
    }

    override fun getStyle(): Int {
        return R.style.DefaultDialog
    }

    override fun getDialogWidth(): Int {
        return ScreenUtils.getScreenWidth()
    }

    override fun getGravity(): Int {
        return Gravity.BOTTOM
    }

    override fun getDialog(): Dialog? {
        val dialog = super.getDialog()
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun subscribe(emitter: ObservableEmitter<Int>) {
        this.emmiter = emitter
    }
}

fun selectPicFromCameraOrPic(
    activity: FragmentActivity,
    count: Int,
    selectList : ArrayList<Photo>?,
    finishSelectListener: FinishSelectListener
) {
    val dialog = SelectPickDialog()
    Observable.create<Int>(dialog)
        .filter { it > 0 }
        .compose(ensure(activity, count, selectList, finishSelectListener))
        .subscribe()

    dialog?.show(activity.supportFragmentManager, "selectPic")
}

fun selectPicFromCamera(
    activity: FragmentActivity,
    finishSelectListener: FinishSelectListener
) {
    Observable.just(1)
        .filter { it > 0 }
        .compose(ensure(activity, 1, null,  finishSelectListener))
        .subscribe()
}

fun selectPicFromPic(
    activity: FragmentActivity,
    count: Int,
    selectList : ArrayList<Photo>?,
    finishSelectListener: FinishSelectListener
) {
    Observable.just(2)
        .filter { it > 0 }
        .compose(ensure(activity, count, selectList, finishSelectListener))
        .subscribe()
}


private fun <Int> ensure(
    activity: FragmentActivity,
    count: kotlin.Int,
    selectList: ArrayList<Photo>?,
    finishSelectListener: FinishSelectListener
): ObservableTransformer<Int, Boolean?>? {

    var realCount = count
    if (count <= 0)
        realCount = 1

    return ObservableTransformer { o ->
        o.map {
            if (it == 1) {
                RxPermissionsNew.requestPermissions(
                    activity,
                    "拍摄功能需获取本地相机权限，请确认下一步操作",
                    Manifest.permission.CAMERA
                )
                    .map {
                        if (!it) {
                            MToastHelper.showToast("需要相关权限，请允许")
                        } else {
                            EasyPhotos.createCamera(
                                activity,
                                true
                            )
                                .setFileProviderAuthority(activity.getPackageName() + ".fileProvider")
                                .setCount(1)
                                .setPuzzleMenu(false)
                                .setCleanMenu(false)
                                .setFinishSelectListener(finishSelectListener)
                                .start(101)
                        }
                        it
                    }
            } else if (it == 2) {
                RxPermissionsNew.requestPermissions(
                    activity,
                    "使用图片需获取本地相册权限，请确认下一步操作",
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                    .map {
                        if (!it) {
                            MToastHelper.showToast("需要相关权限，请允许")
                        } else {
                            EasyPhotos.createAlbum(
                                activity,
                                false,
                                false,
                                GlideEngine.getInstance()
                            )
                                .setFileProviderAuthority(activity.getPackageName() + ".fileProvider")
                                .setSelectedPhotos(selectList ?: ArrayList())
                                .setCount(realCount)
                                .setPuzzleMenu(false)
                                .setCleanMenu(false)
                                .setFinishSelectListener(finishSelectListener)
                                .start(101)
                        }
                        it
                    }
            } else {
                Observable.just(false)
            }
        }.flatMap {
            it
        }
    }
}