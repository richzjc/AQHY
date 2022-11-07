package com.micker.six.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SixListModel implements Parcelable {
    public String key;
    public List<SixModel> list;

    public SixListModel(){

    }

    protected SixListModel(Parcel in) {
        key = in.readString();
        list = in.createTypedArrayList(SixModel.CREATOR);
    }

    public static final Creator<SixListModel> CREATOR = new Creator<SixListModel>() {
        @Override
        public SixListModel createFromParcel(Parcel in) {
            return new SixListModel(in);
        }

        @Override
        public SixListModel[] newArray(int size) {
            return new SixListModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeTypedList(list);
    }
}
