package com.micker.data.model.inspiration

import android.os.Parcel
import android.os.Parcelable
import com.alibaba.fastjson.annotation.JSONField
import com.micker.data.Poko

@Poko
data class InspirationUserEntity(@JSONField(name="birthday")
                                 val birthday: Long = 0,
                                 @JSONField(name="gender")
                                 val gender: String? = "",
                                 @JSONField(name="phone")
                                 val phone: String? = "",
                                 @JSONField(name="nickName")
                                 val nickName: String? = "",
                                 @JSONField(name="avatar")
                                 val avatar: String? = "",
                                 @JSONField(name="userId")
                                 val userId: String? = "",
                                 @JSONField(name="email")
                                 val email: String? = "",
                                 @JSONField(name="businessCreateTime")
                                 val businessCreateTime: Long = 0) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(birthday)
        parcel.writeString(gender)
        parcel.writeString(phone)
        parcel.writeString(nickName)
        parcel.writeString(avatar)
        parcel.writeString(userId)
        parcel.writeString(email)
        parcel.writeLong(businessCreateTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InspirationUserEntity> {
        override fun createFromParcel(parcel: Parcel): InspirationUserEntity {
            return InspirationUserEntity(parcel)
        }

        override fun newArray(size: Int): Array<InspirationUserEntity?> {
            return arrayOfNulls(size)
        }
    }
}