package com.micker.six.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SixModel implements Parcelable {
    public String leftTitle;
    public String rightTitle;

    public SixModel(){

    }

    protected SixModel(Parcel in) {
        leftTitle = in.readString();
        rightTitle = in.readString();
    }

    public static final Creator<SixModel> CREATOR = new Creator<SixModel>() {
        @Override
        public SixModel createFromParcel(Parcel in) {
            return new SixModel(in);
        }

        @Override
        public SixModel[] newArray(int size) {
            return new SixModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(leftTitle);
        dest.writeString(rightTitle);
    }
}
