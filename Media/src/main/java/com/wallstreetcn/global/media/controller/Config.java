package com.wallstreetcn.global.media.controller;

import android.text.TextUtils;

import com.wallstreetcn.global.media.model.PlayUriEntity;
import com.wallstreetcn.helper.utils.data.CollectionUtil4Data;

import java.util.List;

/**
 * Created by chanlevel on 2017/1/23.
 */

public class Config {


    //  private ArrayMap<String, PlayUriEntity> playUriMap = new ArrayMap<>();
    public List<PlayUriEntity> playUrls;

    public boolean isLive = false;
    public boolean isFullScreen = false;
    public boolean isFavArticle;
    public String videoTitle;

    public boolean favVisible = false;
    public boolean shareVisible = false;
    public boolean speedEnable = false;

    public boolean hasMultiUrls = false;

    public PlayUriEntity uriEntity;

    public Config() {

    }

    private Config(Builder builder) {
        isLive = builder.isLive;
        videoTitle = builder.videoTitle;
        favVisible = builder.favVisible;
        shareVisible = builder.shareVisible;
        speedEnable = builder.speedEnable;
    }

    public void putPlayUrls(List<PlayUriEntity> playUrls, String type) {
        this.playUrls = playUrls;
        hasMultiUrls = !(CollectionUtil4Data.isEmpty(playUrls) || playUrls.size() < 2);
        config(type);
    }

    private void config(String type) {
        for (PlayUriEntity entity : playUrls) {
            if (TextUtils.equals(type, entity.resolution)) {
                this.uriEntity = entity;
            }
        }
    }

    public PlayUriEntity getPlayUriEntityByType(String type) {
        PlayUriEntity playUriEntity = null;
        for (PlayUriEntity entity : playUrls) {
            if (TextUtils.equals(type, entity.resolution)) {
                playUriEntity = entity;
                break;
            }
        }
        return playUriEntity;
    }

    public String resolutionStr() {
        if (TextUtils.isEmpty(uriEntity.name))
            return PlayUriEntity.resolution2Str(uriEntity.resolution);
        return uriEntity.name;
    }

    public String resolution() {
        return uriEntity.resolution;
    }

    public static final class Builder {
        private boolean isLive;
        private String videoTitle;
        private boolean favVisible;
        private boolean shareVisible;
        private boolean speedEnable;

        public Builder() {
        }

        public Builder isLive(boolean val) {
            isLive = val;
            return this;
        }

        public Builder videoTitle(String val) {
            videoTitle = val;
            return this;
        }

        public Builder favVisible(boolean val) {
            favVisible = val;
            return this;
        }

        public Builder shareVisible(boolean val) {
            shareVisible = val;
            return this;
        }

        public Builder speedEnable(boolean enable) {
            this.speedEnable = enable;
            return this;
        }

        public Config build() {
            return new Config(this);
        }
    }
}
