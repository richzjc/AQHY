package com.micker.data.model.user

import android.os.Parcel
import android.os.Parcelable
import com.alibaba.fastjson.annotation.JSONField
import com.micker.data.Poko

@Poko
data class AccountUserInfoEntity(@JSONField(name="distance")
                                 val distance: Int = 0,
                                 @JSONField(name="level")
                                 val level: Int = 0,
                                 @JSONField(name="pubPicTotal")
                                 val pubPicTotal: Int = 0,
                                 @JSONField(name="baseUser")
                                 val baseUser: BaseUser?,
                                 @JSONField(name="befollowed")
                                 val befollowed: Boolean = false,
                                 @JSONField(name="bangCoin")
                                 val bangCoin: Int = 0,
                                 @JSONField(name="attentionCount")
                                 val attentionCount: Int = 0,
                                 @JSONField(name="userId")
                                 val userId: String? = "",
                                 @JSONField(name="followed")
                                 val followed: Boolean = false,
                                 @JSONField(name="frozenBangCoin")
                                 val frozenBangCoin: Int = 0,
                                 @JSONField(name="zhengMei")
                                 val zhengMei: Int = 0,
                                 @JSONField(name="token")
                                 val token: String? = "") : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(BaseUser::class.java.classLoader),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(distance)
        parcel.writeInt(level)
        parcel.writeInt(pubPicTotal)
        parcel.writeParcelable(baseUser, flags)
        parcel.writeByte(if (befollowed) 1 else 0)
        parcel.writeInt(bangCoin)
        parcel.writeInt(attentionCount)
        parcel.writeString(userId)
        parcel.writeByte(if (followed) 1 else 0)
        parcel.writeInt(frozenBangCoin)
        parcel.writeInt(zhengMei)
        parcel.writeString(token)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AccountUserInfoEntity> {
        override fun createFromParcel(parcel: Parcel): AccountUserInfoEntity {
            return AccountUserInfoEntity(parcel)
        }

        override fun newArray(size: Int): Array<AccountUserInfoEntity?> {
            return arrayOfNulls(size)
        }
    }
}