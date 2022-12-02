package com.micker.first.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChineseWordModel implements Parcelable {
    public int bihua;
    public String word;

    public ChineseWordModel(int bihua, String word){
        this.bihua = bihua;
        this.word = word;
    }

    protected ChineseWordModel(Parcel in) {
        bihua = in.readInt();
        word = in.readString();
    }

    public static final Creator<ChineseWordModel> CREATOR = new Creator<ChineseWordModel>() {
        @Override
        public ChineseWordModel createFromParcel(Parcel in) {
            return new ChineseWordModel(in);
        }

        @Override
        public ChineseWordModel[] newArray(int size) {
            return new ChineseWordModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bihua);
        dest.writeString(word);
    }
}
