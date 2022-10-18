package com.micker.data.model.home

import com.alibaba.fastjson.annotation.JSONField
import com.micker.data.Poko

@Poko
data class StatusActionEntity(@JSONField(name="undo")
                              val undo: Boolean = false,
                              @JSONField(name="callbackId")
                              val callbackId: String = "",
                              @JSONField(name="redo")
                              val redo: Boolean = false)