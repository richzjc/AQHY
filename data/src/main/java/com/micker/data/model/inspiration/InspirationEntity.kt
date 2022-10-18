package com.micker.data.model.inspiration

import android.os.Parcel
import android.os.Parcelable
import com.alibaba.fastjson.annotation.JSONField
import com.micker.data.IDifference
import com.micker.data.IEqualsAdapter
import com.micker.data.Poko
import com.micker.data.model.home.HomeSubItemEntity

@Poko
data class InspirationEntity(
    @JSONField(name = "image")
    val image: String? = "",
    @JSONField(name = "modifyTime")
    val modifyTime: Long = 0,
    @JSONField(name = "svgId")
    val svgId: String? = "",
    @JSONField(name = "createTime")
    val createTime: Long = 0,
    @JSONField(name = "svgUrl")
    val svgUrl: String? = "",
    @JSONField(name = "id")
    val id: String? = "",
    @JSONField(name = "state")
    val state: Int = 0,
    @JSONField(name = "userId")
    val userId: String? = "",
    @JSONField(name = "content")
    val content: String? = "",
    @JSONField(name = "user")
    val user: InspirationUserEntity? = null,
    @JSONField(name = "svgItem")
    val svgItem: HomeSubItemEntity? = null
) : Parcelable, IDifference, IEqualsAdapter {

    var isLogined : Boolean = false

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(InspirationUserEntity::class.java.classLoader),
        parcel.readParcelable(HomeSubItemEntity::class.java.classLoader)
    ){
        isLogined = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeLong(modifyTime)
        parcel.writeString(svgId)
        parcel.writeLong(createTime)
        parcel.writeString(svgUrl)
        parcel.writeString(id)
        parcel.writeInt(state)
        parcel.writeString(userId)
        parcel.writeString(content)
        parcel.writeParcelable(user, flags)
        parcel.writeParcelable(svgItem, flags)
        parcel.writeByte(if (isLogined) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InspirationEntity> {
        override fun createFromParcel(parcel: Parcel): InspirationEntity {
            return InspirationEntity(parcel)
        }

        override fun newArray(size: Int): Array<InspirationEntity?> {
            return arrayOfNulls(size)
        }
    }

    fun getNewSubItemEntity(): HomeSubItemEntity {
        val itemEntity = HomeSubItemEntity()
        itemEntity.id = id
        itemEntity.image = image
        itemEntity.content = content
        itemEntity.createTime = createTime
        itemEntity.modifyTime = modifyTime
        itemEntity.svgId = svgId
        itemEntity.svgUrl = svgUrl
        itemEntity.reset = false
        return itemEntity
    }

    override fun getUniqueId(): String {
        return id!!
    }

    override fun equals(other: Any?): Boolean {
        return if (other is InspirationEntity) {
            other.id == id
        } else
            super.equals(other)
    }

    override fun diffEquals(obj: Any?): Boolean {
        return if(obj is InspirationEntity) {
            if(state != obj.state)
                false
            else isLogined == obj.isLogined
        }else
            super.equals(obj)
    }
}