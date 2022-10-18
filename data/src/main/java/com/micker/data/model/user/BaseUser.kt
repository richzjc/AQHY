package com.micker.data.model.user

import android.os.Parcel
import android.os.Parcelable
import com.alibaba.fastjson.annotation.JSONField
import com.micker.data.Poko

@Poko
data class BaseUser(@JSONField(name="birthday")
                    val birthday: String? = "",
                    @JSONField(name="startAge")
                    val startAge: Int = 0,
                    @JSONField(name="popVip")
                    val popVip: Boolean = false,
                    @JSONField(name="gender")
                    val gender: String? = "",
                    @JSONField(name="purpose")
                    val purpose: String? = "",
                    @JSONField(name="charmPointLevel")
                    val charmPointLevel: Int = 0,
                    @JSONField(name="nickName")
                    val nickName: String? = "",
                    @JSONField(name="avatar")
                    val avatar: String? = "",
                    @JSONField(name="meetGender")
                    val meetGender: String? = "",
                    @JSONField(name="endAge")
                    val endAge: Int = 0,
                    @JSONField(name="pointLevel")
                    val pointLevel: Int = 0,
                    @JSONField(name="phone")
                    val phone: String? = "",
                    @JSONField(name="allPoint")
                    val allPoint: Int = 0,
                    @JSONField(name="name")
                    val name: String? = "",
                    @JSONField(name="meetConstellation")
                    val meetConstellation: String? = "",
                    @JSONField(name="email")
                    val email: String? = "",
                    @JSONField(name="charmPoint")
                    val charmPoint: Int = 0) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(birthday)
        parcel.writeInt(startAge)
        parcel.writeByte(if (popVip) 1 else 0)
        parcel.writeString(gender)
        parcel.writeString(purpose)
        parcel.writeInt(charmPointLevel)
        parcel.writeString(nickName)
        parcel.writeString(avatar)
        parcel.writeString(meetGender)
        parcel.writeInt(endAge)
        parcel.writeInt(pointLevel)
        parcel.writeString(phone)
        parcel.writeInt(allPoint)
        parcel.writeString(name)
        parcel.writeString(meetConstellation)
        parcel.writeString(email)
        parcel.writeInt(charmPoint)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseUser> {
        override fun createFromParcel(parcel: Parcel): BaseUser {
            return BaseUser(parcel)
        }

        override fun newArray(size: Int): Array<BaseUser?> {
            return arrayOfNulls(size)
        }
    }
}