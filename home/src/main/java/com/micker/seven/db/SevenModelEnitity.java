package com.micker.seven.db;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "poetry")
public class SevenModelEnitity {

    @PrimaryKey
    @NonNull
    public String title;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "author")
    public String author;

    @ColumnInfo(name = "tranlate")
    public String tranlate;

    public SevenModelEnitity() {

    }

//    protected SevenModelEnitity(Parcel in) {
//        title = in.readString();
//        content = in.readString();
//        author = in.readString();
//        tranlate = in.readString();
//    }
//
//    public static final Creator<SevenModelEnitity> CREATOR = new Creator<SevenModelEnitity>() {
//        @Override
//        public SevenModelEnitity createFromParcel(Parcel in) {
//            return new SevenModelEnitity(in);
//        }
//
//        @Override
//        public SevenModelEnitity[] newArray(int size) {
//            return new SevenModelEnitity[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(title);
//        dest.writeString(content);
//        dest.writeString(author);
//        dest.writeString(tranlate);
//    }
}
