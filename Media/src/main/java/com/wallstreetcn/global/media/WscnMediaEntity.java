package com.wallstreetcn.global.media;

import android.os.Parcel;
import android.os.Parcelable;

import com.wallstreetcn.global.media.widget.WscnMediaNetView;

public class WscnMediaEntity implements Parcelable {

    private String tag;
    private String url;
    private boolean nocache;//是否要缓存播放，如果是M3U8或者HLS，请设置为true
    private String title;
    public long size;
    private String key;
    public boolean mSilent = false;
    public int width;
    public int height;

    public static WscnMediaEntity buildEntityWithKey(String url, String key) {
        return buildEntityWithKey(url, "", key);
    }

    public static WscnMediaEntity buildEntityWithKey(String url, String tag, String key) {
        return buildEntityWithKey(url, tag, key, false);
    }

    public static WscnMediaEntity buildEntityWithKey(String url, String tag, String key, boolean nocache) {
        WscnMediaEntity entity = new WscnMediaEntity.Builder().url(url).tag(tag).key(key).nocache(nocache).build();
        return entity;
    }

    public void initSize() {
        WscnMediaNetView.initAudioLength(url, size -> {
            this.size = size;
        });
    }

    public boolean isNocache() {
        return nocache;
    }

    public void setNocache(boolean nocache) {
        this.nocache = nocache;
    }

    private WscnMediaEntity(Builder builder) {
        setTag(builder.tag);
        setUrl(builder.url);
        setNocache(builder.nocache);
        setTitle(builder.title);
        setKey(builder.key);
        this.width = builder.width;
        this.height = builder.height;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    static final class Builder {
        private String tag;
        private String url;
        private boolean nocache;
        private String title;
        private String key;
        private int width;
        private int height;

        public Builder() {
        }

        public Builder tag(String val) {
            tag = val;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder nocache(boolean val) {
            nocache = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder width(int key) {
            this.width = key;
            return this;
        }

        public Builder height(int key) {
            this.height = key;
            return this;
        }

        public WscnMediaEntity build() {
            return new WscnMediaEntity(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tag);
        dest.writeString(this.url);
        dest.writeByte(this.nocache ? (byte) 1 : (byte) 0);
        dest.writeString(this.title);
        dest.writeLong(this.size);
        dest.writeInt(width);
        dest.writeInt(height);
    }

    protected WscnMediaEntity(Parcel in) {
        this.tag = in.readString();
        this.url = in.readString();
        this.nocache = in.readByte() != 0;
        this.title = in.readString();
        this.size = in.readLong();
        this.width = in.readInt();
        this.height = in.readInt();
    }

    public static final Creator<WscnMediaEntity> CREATOR = new Creator<WscnMediaEntity>() {
        @Override
        public WscnMediaEntity createFromParcel(Parcel source) {
            return new WscnMediaEntity(source);
        }

        @Override
        public WscnMediaEntity[] newArray(int size) {
            return new WscnMediaEntity[size];
        }
    };
}
