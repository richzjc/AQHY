package com.wallstreetcn.global.media.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;


import com.micker.helper.system.ScreenUtils;
import com.micker.helper.text.span.RoundedBackgroundSpan;

import java.util.List;

/**
 * Created by Leif Zhang on 2017/1/13.
 * Email leifzhanggithub@gmail.com
 */

public class PlayUriEntity implements Parcelable {

    public String  resolution;
    public String  uri;
    public String  name;
    public boolean cached;

    public PlayUriEntity() {
    }

    public static final String FHD      = "fhd";
    public static final String HD       = "hd";
    public static final String SD       = "sd";
    public static final String ORIGINAL = "original";
    public static final String DEFAULT  = ORIGINAL;

    public String getTypeString() {
//        return resolution2Str(resolution);
        String value = name;
        if (TextUtils.isEmpty(value))
            value = resolution2Str(resolution);
        return value;
    }

    public Spannable getTypeSpannable() {
        String value = getTypeString();
        SpannableStringBuilder span = new SpannableStringBuilder(value);
        if (!cached) return span;
        String type = "已下载";
        span.append(" ");
        span.append(type);
        span.setSpan(new RoundedBackgroundSpan(Color.parseColor("#1478f0"), Color.WHITE, ScreenUtils.dip2px(2)), value.length() + 1, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new AbsoluteSizeSpan(9, true), value.length() + 1, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    public int getWeight() {
        if (TextUtils.isEmpty(resolution))
            return 10;
        switch (resolution.toLowerCase()) {
            case DEFAULT:
                return 1;
            case FHD:
                return 3;
            case HD:
                return 4;
            case SD:
                return 5;
        }
        return 10;
    }


    public static String resolution2Str(String resolution) {
        switch (resolution) {
            case FHD:
                return "1080p";
            case HD:
                return "720p";
            case SD:
                return "480p";
            default:
                return "原画";
//                return ResourceUtils.getResStringFromId(R.string.live_room_original_painting);
        }
    }


    public static String getPlayUrl(List<PlayUriEntity> play_uris, String type) {
        String url = "";
        for (PlayUriEntity entity : play_uris) {
            if (TextUtils.equals(entity.resolution, type)) {
                url = entity.uri;
            }
        }
        return url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.resolution);
        dest.writeString(this.uri);
        dest.writeString(this.name);
        dest.writeByte(this.cached ? (byte) 1 : (byte) 0);
    }

    protected PlayUriEntity(Parcel in) {
        this.resolution = in.readString();
        this.uri = in.readString();
        this.name = in.readString();
        this.cached = in.readByte() != 0;
    }

    public static final Creator<PlayUriEntity> CREATOR = new Creator<PlayUriEntity>() {
        @Override
        public PlayUriEntity createFromParcel(Parcel source) {
            return new PlayUriEntity(source);
        }

        @Override
        public PlayUriEntity[] newArray(int size) {
            return new PlayUriEntity[size];
        }
    };
}
