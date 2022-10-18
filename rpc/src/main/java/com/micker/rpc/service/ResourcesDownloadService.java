package com.micker.rpc.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.kronos.volley.Request;
import com.kronos.volley.toolbox.StringRequest;
import com.micker.helper.SharedPrefsUtil;
import com.micker.helper.file.CacheUtils;
import com.micker.helper.file.FileUtil;
import com.micker.helper.file.ZipHelper;
import com.micker.helper.router.RouterHelper;
import com.micker.helper.text.TextUtil;
import com.micker.rpc.VolleyQueue;
import okhttp3.Response;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Leif Zhang on 2016/11/18.
 * Email leifzhanggithub@gmail.com
 */
public class ResourcesDownloadService extends IntentService {
    public ResourcesDownloadService() {
        super("ResourcesDownloadService");
    }

    public static void startService(Context context, String url) {
        Intent intent = new Intent(context, ResourcesDownloadService.class);
        intent.putExtra("url", url);
        context.startService(intent);
    }

    private static final String DEFAULT_CACHE_DIR = CacheUtils.CACHE_RESOURCE;


    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        File cacheDir = new File(getCacheDir(), DEFAULT_CACHE_DIR);
        try {
            //String filePath = TextUtil.format("%s/%d.%s", cacheDir.getPath(), url.hashCode(), getSuffix(url));
            String filePath = getFilePath(this, url);
            Request request = new StringRequest(url);
            Response response = VolleyQueue.getInstance().sync(request);
            InputStream io = response.body().byteStream();
            FileUtil.writeFile(filePath, io);
            SharedPrefsUtil.save(url, true);
            if (TextUtils.equals(".zip", RouterHelper.getSuffix(url))) {
                handleZip(cacheDir, String.valueOf(url.hashCode()), filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void handleZip(File cacheDir, String url, String filePath) {
        File zipFile = new File(cacheDir, String.valueOf(url.hashCode()));
        ZipHelper.zipFile(filePath, zipFile.getPath());
    }


    public static String getFileDirectory(Context context, String url) {
        return TextUtil.format("%s/%d", context.getCacheDir() + File.separator + DEFAULT_CACHE_DIR, String.valueOf(url.hashCode()).hashCode());
    }

    public static String getFilePath(Context context, String url) {
        return TextUtil.format("%s/%d.%s", context.getCacheDir() + File.separator + DEFAULT_CACHE_DIR, url.hashCode(),
                RouterHelper.getSuffix(url));
    }


}
