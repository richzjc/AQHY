package com.wallstreetcn.global.media.model;

import android.text.TextUtils;

import com.wallstreetcn.global.media.utils.WscnMediaUtils;
import com.wallstreetcn.helper.utils.Util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayUrlUtils {

    public static String getPlayUrlWithNet(List<PlayUriEntity> list) {
        if (list == null || list.isEmpty())
            return "";
        boolean wifi = Util.isConnectWIFI();
        if (wifi) return getUrlEntity(list, PlayUriEntity.DEFAULT).uri;

        list = sort(list);
        for (PlayUriEntity entity : list) {
            if (WscnMediaUtils.offline(entity.uri))
                return entity.uri;
        }

        for (int index = list.size() - 1; index >= 0; index++) {
            PlayUriEntity entity = list.get(index);
            if (entity != null)
                return entity.uri;
        }
        return "";

//        PlayUriEntity url;
//        if ((url = getUrlEntity(list, PlayUriEntity.SD)) != null) return url.uri;
//        if ((url = getUrlEntity(list, PlayUriEntity.HD)) != null) return url.uri;
//        if ((url = getUrlEntity(list, PlayUriEntity.FHD)) != null) return url.uri;
//        return getUrlEntity(list, PlayUriEntity.DEFAULT).uri;
    }

    //原画,高清，标清.....
    private static List<PlayUriEntity> sort(List<PlayUriEntity> list) {
        Collections.sort(list, new Comparator<PlayUriEntity>() {
            @Override
            public int compare(PlayUriEntity o1, PlayUriEntity o2) {
                return Integer.compare(o1.getWeight(), o2.getWeight());
            }
        });
        return list;
    }

    //key 默认分辨率链接作为key
    public static String getPlayUrlsKey(List<PlayUriEntity> play_uris) {
        PlayUriEntity entity = getUrlEntity(play_uris, PlayUriEntity.ORIGINAL);
        if (entity == null) {
            return "";
        }
        return entity.uri;
    }

    private static PlayUriEntity getUrlEntity(List<PlayUriEntity> play_uris, String type) {
        PlayUriEntity url = null;
        if (play_uris == null) {
            return url;
        }
        for (PlayUriEntity entity : play_uris) {
            if (TextUtils.equals(entity.resolution, type)) {
                url = entity;
            }
        }
        return url;
    }

    public static PlayUriEntity getUriEntityWithNet(List<PlayUriEntity> list) {
        if (list == null || list.isEmpty())
            return null;

        list = sort(list);
        PlayUriEntity cached = null;
        for (PlayUriEntity entity : list) {
            if (WscnMediaUtils.isDownload(entity.uri)) {//标记已下载的视频
                entity.cached = true;
                return entity;
            } else if (cached == null && WscnMediaUtils.offline(entity.uri)) {//选择 已缓存的视频
                cached = entity;
            }
        }
        if (cached != null) return cached;

        boolean wifi = Util.isConnectWIFI();
        if (wifi) return getUrlEntity(list, PlayUriEntity.DEFAULT);

        for (int index = list.size() - 1; index >= 0; index++) {
            PlayUriEntity entity = list.get(index);
            if (entity != null)
                return entity;
        }
        return null;

//        PlayUriEntity url;
//        if ((url = getUrlEntity(list, PlayUriEntity.SD)) != null) return url;
//        if ((url = getUrlEntity(list, PlayUriEntity.HD)) != null) return url;
//        if ((url = getUrlEntity(list, PlayUriEntity.FHD)) != null) return url;
//        return getUrlEntity(list, PlayUriEntity.DEFAULT);
    }
}
