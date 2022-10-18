package com.micker.user.model

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import com.micker.data.IDifference

class UserItemEntity(val title : String?,
                     val routerUrl : String?,
                     val isNeedLogin : Boolean,
                     val marginTop : Int,
                     val rightText : String?) : Parcelable,
    IDifference {

    var onClickListener : View.OnClickListener? = null
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(routerUrl)
        parcel.writeByte(if (isNeedLogin) 1 else 0)
        parcel.writeInt(marginTop)
        parcel.writeString(rightText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserItemEntity> {
        override fun createFromParcel(parcel: Parcel): UserItemEntity {
            return UserItemEntity(parcel)
        }

        override fun newArray(size: Int): Array<UserItemEntity?> {
            return arrayOfNulls(size)
        }
    }

    override fun getUniqueId() = title
}