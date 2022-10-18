package com.micker.rpc.exception;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wscn on 16/12/16.
 */

public class ErrCodeMsgEntity implements Parcelable {

    public int succ;
    public String msg;
    public String exception;

    public ErrCodeMsgEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.succ);
        dest.writeString(this.msg);
        dest.writeString(this.exception);
    }

    protected ErrCodeMsgEntity(Parcel in) {
        this.succ = in.readInt();
        this.msg = in.readString();
        this.exception = in.readString();
    }

    public static final Creator<ErrCodeMsgEntity> CREATOR = new Creator<ErrCodeMsgEntity>() {
        @Override
        public ErrCodeMsgEntity createFromParcel(Parcel source) {
            return new ErrCodeMsgEntity(source);
        }

        @Override
        public ErrCodeMsgEntity[] newArray(int size) {
            return new ErrCodeMsgEntity[size];
        }
    };
}
