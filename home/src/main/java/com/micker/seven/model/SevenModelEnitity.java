package com.micker.seven.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SevenModelEnitity implements Parcelable {

    @NonNull
    public int id;

    public String title;

    public String content;

    public String author;

    public String tranlate;

    public SevenModelEnitity() {

    }

    protected SevenModelEnitity(Parcel in) {
        title = in.readString();
        content = in.readString();
        author = in.readString();
        tranlate = in.readString();
        id = in.readInt();
    }

    public static final Creator<SevenModelEnitity> CREATOR = new Creator<SevenModelEnitity>() {
        @Override
        public SevenModelEnitity createFromParcel(Parcel in) {
            return new SevenModelEnitity(in);
        }

        @Override
        public SevenModelEnitity[] newArray(int size) {
            return new SevenModelEnitity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(author);
        dest.writeString(tranlate);
        dest.writeInt(id);
    }
}
