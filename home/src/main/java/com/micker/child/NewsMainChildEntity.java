package com.micker.child;

import android.os.Parcel;
import android.os.Parcelable;

import com.micker.data.IDifference;

public class NewsMainChildEntity implements Parcelable, IDifference {
    public String id;
    public String imageUrl;
    public String title;
    public String stage;
    public String desc;
    public String router;
    public float ratio;

    public NewsMainChildEntity(){

    }

    protected NewsMainChildEntity(Parcel in) {
        id = in.readString();
        imageUrl = in.readString();
        title = in.readString();
        stage = in.readString();
        desc = in.readString();
        router = in.readString();
        ratio = in.readFloat();
    }

    public static final Creator<NewsMainChildEntity> CREATOR = new Creator<NewsMainChildEntity>() {
        @Override
        public NewsMainChildEntity createFromParcel(Parcel in) {
            return new NewsMainChildEntity(in);
        }

        @Override
        public NewsMainChildEntity[] newArray(int size) {
            return new NewsMainChildEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeString(stage);
        dest.writeString(desc);
        dest.writeString(router);
        dest.writeFloat(ratio);
    }

    @Override
    public String getUniqueId() {
        return id;
    }
}
