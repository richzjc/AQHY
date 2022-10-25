package com.wallstreetcn.global.media;

import android.net.Uri;
import android.text.TextUtils;

import com.danikula.videocache.HttpProxyCacheServer;
import com.wallstreetcn.helper.utils.UtilsContextManager;

/**
 * Created by Leif Zhang on 2017/4/24.
 * Email leifzhanggithub@gmail.com
 */

public class VideoCacheManager {
    private        HttpProxyCacheServer proxy;
    private static VideoCacheManager    instance;

    public synchronized static VideoCacheManager getProxy() {
        if (instance == null) {
            synchronized (VideoCacheManager.class) {
                if (instance == null)
                    instance = new VideoCacheManager();
            }
        }
        return instance;
    }

    private VideoCacheManager() {
        proxy = new HttpProxyCacheServer.Builder(UtilsContextManager.getInstance().getApplication())
                .fileNameGenerator(url -> url.hashCode() + ".temp")
                .maxCacheSize(1024 * 1024 * 1024)
//                .maxCacheFilesCount(30)//大小限制 而不是文件个数
                .build();
    }

    public String getProxyUrl(String url) {
        try {
            Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();
            if (TextUtils.equals(scheme, "https") || TextUtils.equals(scheme, "http")) {
                return proxy.getProxyUrl(url);
            } else {
                return url;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public void shutDown() {
        if (proxy != null) {
//            proxy.shutdown();
//            proxy = null;
//            instance = null;
        }
    }

    public boolean isCached(String url) {
        return proxy != null && proxy.isCached(url);
    }
}
