package com.micker.five.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.micker.data.IDifference;

public class FiveStageModel implements Parcelable, IDifference {
    public String title;
    public String videoUrl;

    public FiveStageModel(){

    }

    protected FiveStageModel(Parcel in) {
        title = in.readString();
        videoUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(videoUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FiveStageModel> CREATOR = new Creator<FiveStageModel>() {
        @Override
        public FiveStageModel createFromParcel(Parcel in) {
            return new FiveStageModel(in);
        }

        @Override
        public FiveStageModel[] newArray(int size) {
            return new FiveStageModel[size];
        }
    };

    @Override
    public String getUniqueId() {
        return videoUrl;
    }
}
