package com.micker.data.model.home

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.alibaba.fastjson.annotation.JSONField
import com.micker.data.Poko

@Poko
data class HomeSubItemEntity(
    @JSONField(name = "image")
    var image: String? = "",
    @JSONField(name = "imgWidth")
    var imgWidth: Int = 0,
    @JSONField(name = "modifyTime")
    var modifyTime: Long = 0,
    @JSONField(name = "createTime")
    var createTime: Long = 0,
    @JSONField(name = "svgUrl")
    var svgUrl: String? = "",
    @JSONField(name = "name")
    var name: String? = "",
    @JSONField(name = "imgHeight")
    var imgHeight: Int = 0,
    @JSONField(name = "id")
    var id: String? = "",
    @JSONField(name = "vip")
    var vip: Int = 0,
    @JSONField(name = "content")
    var content: String? = ""
) : Parcelable {

    var reset: Boolean = true
    var svgId: String? = ""
        get() {
            if (TextUtils.isEmpty(field))
                field = id
            return field
        }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
        reset = parcel.readByte() != 0.toByte()
        svgId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeInt(imgWidth)
        parcel.writeLong(modifyTime)
        parcel.writeLong(createTime)
        parcel.writeString(svgUrl)
        parcel.writeString(name)
        parcel.writeInt(imgHeight)
        parcel.writeString(id)
        parcel.writeInt(vip)
        parcel.writeString(content)
        parcel.writeByte(if (reset) 1 else 0)
        parcel.writeString(svgId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeSubItemEntity> {
        override fun createFromParcel(parcel: Parcel): HomeSubItemEntity {
            return HomeSubItemEntity(parcel)
        }

        override fun newArray(size: Int): Array<HomeSubItemEntity?> {
            return arrayOfNulls(size)
        }
    }

}