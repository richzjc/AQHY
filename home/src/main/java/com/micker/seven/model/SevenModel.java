package com.micker.seven.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SevenModel implements Parcelable {

    public String title;
    public String author;
    public String content;

    public SevenModel(){

    }

    protected SevenModel(Parcel in) {
        title = in.readString();
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<SevenModel> CREATOR = new Creator<SevenModel>() {
        @Override
        public SevenModel createFromParcel(Parcel in) {
            return new SevenModel(in);
        }

        @Override
        public SevenModel[] newArray(int size) {
            return new SevenModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(content);
    }
}
