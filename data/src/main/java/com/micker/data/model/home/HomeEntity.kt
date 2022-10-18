package com.micker.data.model.home

import android.os.Parcel
import android.os.Parcelable
import com.alibaba.fastjson.annotation.JSONField
import com.micker.data.Poko

@Poko
data class HomeEntity(@JSONField(name="image")
                      val image: String? = "",
                      @JSONField(name="modifyTime")
                      val modifyTime: Long = 0,
                      @JSONField(name="createTime")
                      val createTime: Long = 0,
                      @JSONField(name="name")
                      val name: String? = "",
                      @JSONField(name="id")
                      val id: String? = "",
                      @JSONField(name="type")
                      val type: Int = 0,
                      @JSONField(name="vip")
                      val vip: Int = 0,
                      @JSONField(name="items")
                      val items: List<HomeSubItemEntity>?,
                      @JSONField(name="desc")
                      val desc: String? = "") : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.createTypedArrayList(HomeSubItemEntity),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeLong(modifyTime)
        parcel.writeLong(createTime)
        parcel.writeString(name)
        parcel.writeString(id)
        parcel.writeInt(type)
        parcel.writeInt(vip)
        parcel.writeTypedList(items)
        parcel.writeString(desc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeEntity> {
        override fun createFromParcel(parcel: Parcel): HomeEntity {
            return HomeEntity(parcel)
        }

        override fun newArray(size: Int): Array<HomeEntity?> {
            return arrayOfNulls(size)
        }
    }
}