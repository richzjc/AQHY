package com.micker.core.imageloader;

import android.app.Application;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import okhttp3.OkHttpClient;

/**
 * Created by Leif Zhang on 2017/3/8.
 * Email leifzhanggithub@gmail.com
 */

public class ImageLoaderInit {
    public static void init(Application application, OkHttpClient httpClient) {
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(application)
                .setMaxCacheSize(75 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(50 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(30 * ByteConstants.MB)
                .build();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(application, httpClient)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(application, config);
    }
}
