package com.micker.first.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FirstStageModel implements Parcelable {

    public String findWord;
    public String proguardWord;

    public FirstStageModel(){

    }

    protected FirstStageModel(Parcel in) {
        findWord = in.readString();
        proguardWord = in.readString();
    }

    public static final Creator<FirstStageModel> CREATOR = new Creator<FirstStageModel>() {
        @Override
        public FirstStageModel createFromParcel(Parcel in) {
            return new FirstStageModel(in);
        }

        @Override
        public FirstStageModel[] newArray(int size) {
            return new FirstStageModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(findWord);
        dest.writeString(proguardWord);
    }
}
