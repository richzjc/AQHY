package com.micker.data.model.home

import android.os.Parcel
import android.os.Parcelable
import com.alibaba.fastjson.annotation.JSONField
import com.micker.data.Poko

@Poko
data class ColorsItem(@JSONField(name="image")
                      val image: String? = "",
                      @JSONField(name="color")
                      var color: String? = "",
                      @JSONField(name="groupId")
                      val groupId: String? = "",
                      @JSONField(name="weight")
                      val weight: Int = 0,
                      @JSONField(name="type")
                      var type: Int = 0,
                      @JSONField(name="content")
                      val content: String? = "",
                      @JSONField(name="filterId")
                      val filterId: String? = "",
                      @JSONField(name="groupName")
                      val groupName: String? = "",
                      @JSONField(name="modifyTime")
                      val modifyTime: Long = 0,
                      @JSONField(name="createTime")
                      val createTime: Long = 0,
                      @JSONField(name="name")
                      var name: String? = "",
                      @JSONField(name="width")
                      val width: Int = 0,
                      @JSONField(name="location")
                      val location: String? = "",
                      @JSONField(name="id")
                      val id: String? = "",
                      @JSONField(name="vip")
                      val vip: Int = 0,
                      @JSONField(name="direction")
                      val direction: Int = 0,
                      @JSONField(name="height")
                      val height: Int = 0) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeString(color)
        parcel.writeString(groupId)
        parcel.writeInt(weight)
        parcel.writeInt(type)
        parcel.writeString(content)
        parcel.writeString(filterId)
        parcel.writeString(groupName)
        parcel.writeLong(modifyTime)
        parcel.writeLong(createTime)
        parcel.writeString(name)
        parcel.writeInt(width)
        parcel.writeString(location)
        parcel.writeString(id)
        parcel.writeInt(vip)
        parcel.writeInt(direction)
        parcel.writeInt(height)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ColorsItem> {
        override fun createFromParcel(parcel: Parcel): ColorsItem {
            return ColorsItem(parcel)
        }

        override fun newArray(size: Int): Array<ColorsItem?> {
            return arrayOfNulls(size)
        }
    }
}