package com.micker.data.model.aqhy;

import android.os.Parcel;
import android.os.Parcelable;

public class LaunchConfigEntity implements Parcelable {

    public String id;
    public String name;
    public String desc;
    public String image;
    public String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeString(this.image);
        dest.writeString(this.url);
    }

    public LaunchConfigEntity() {
    }

    protected LaunchConfigEntity(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.desc = in.readString();
        this.image = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<LaunchConfigEntity> CREATOR = new Parcelable.Creator<LaunchConfigEntity>() {
        @Override
        public LaunchConfigEntity createFromParcel(Parcel source) {
            return new LaunchConfigEntity(source);
        }

        @Override
        public LaunchConfigEntity[] newArray(int size) {
            return new LaunchConfigEntity[size];
        }
    };
}
