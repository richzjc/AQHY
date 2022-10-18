package com.micker.aqhy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MainGridModel implements Parcelable {
    public String name;
    public String router;

    public MainGridModel() {

    }

    protected MainGridModel(Parcel in) {
        name = in.readString();
        router = in.readString();
    }

    public static final Creator<MainGridModel> CREATOR = new Creator<MainGridModel>() {
        @Override
        public MainGridModel createFromParcel(Parcel in) {
            return new MainGridModel(in);
        }

        @Override
        public MainGridModel[] newArray(int size) {
            return new MainGridModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(router);
    }
}
