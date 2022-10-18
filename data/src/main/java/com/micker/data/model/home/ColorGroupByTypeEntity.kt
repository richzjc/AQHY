package com.micker.data.model.home

import android.os.Parcel
import android.os.Parcelable
import com.alibaba.fastjson.annotation.JSONField
import com.micker.data.Poko

@Poko
data class ColorGroupByTypeEntity(@JSONField(name="modifyTime")
                                  val modifyTime: Long = 0,
                                  @JSONField(name="createTime")
                                  val createTime: Long = 0,
                                  @JSONField(name="name")
                                  val name: String? = "",
                                  @JSONField(name="weight")
                                  val weight: Int = 0,
                                  @JSONField(name="id")
                                  val id: String? = "",
                                  @JSONField(name="type")
                                  val type: Int = 0,
                                  @JSONField(name="vip")
                                  val vip: Int = 0,
                                  @JSONField(name="colors")
                                  val colors: List<ColorsItem>?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.createTypedArrayList(ColorsItem)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(modifyTime)
        parcel.writeLong(createTime)
        parcel.writeString(name)
        parcel.writeInt(weight)
        parcel.writeString(id)
        parcel.writeInt(type)
        parcel.writeInt(vip)
        parcel.writeTypedList(colors)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ColorGroupByTypeEntity> {
        override fun createFromParcel(parcel: Parcel): ColorGroupByTypeEntity {
            return ColorGroupByTypeEntity(parcel)
        }

        override fun newArray(size: Int): Array<ColorGroupByTypeEntity?> {
            return arrayOfNulls(size)
        }
    }
}