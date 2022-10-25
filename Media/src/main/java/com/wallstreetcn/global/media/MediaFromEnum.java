package com.wallstreetcn.global.media;

/**
 * 播放模式
 * Created by wcy on 2015/12/26.
 */

/**
 * AD:广告
 * ARTICLE:普通文章
 * LIVEROOM:直播间
 * PREMIUMARTICLE: 特辑文章
 * NEWSROOM:以前首页的视频列表
 */

public enum MediaFromEnum {
    AD("ad"),
    ARTICLE("article"),
    LIVEROOM("liveRoom"),
    NEWSROOM("newRoom"),
    DISCUSSION("discussion");

    private String value;

    MediaFromEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
