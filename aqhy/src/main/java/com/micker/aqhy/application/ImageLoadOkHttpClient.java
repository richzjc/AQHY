package com.micker.aqhy.application;

import android.util.Log;
import com.micker.helper.image.ImageUtlFormatHelper;
import com.micker.rpc.manager.WscnDns;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ImageLoadOkHttpClient {
    private OkHttpClient okHttpClient;

    public ImageLoadOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .dns(new WscnDns())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 5, TimeUnit.MINUTES));
        client.addInterceptor(new ImageRetryInterceptor());
        okHttpClient = client.build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    static class ImageRetryInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            int code = response.code();
            if (code != 200 && checkUrl(request)) {
                Request.Builder builder = request.newBuilder();
                HttpUrl url = request.url();
                HttpUrl.Builder newUrlBuilder = new HttpUrl.Builder();
                HttpUrl newUrl = newUrlBuilder.scheme(url.scheme()).host(url.host()).encodedPath(url.encodedPath()).build();
                builder.url(ImageUtlFormatHelper.formatImageWithThumbnailJpeg(newUrl.toString(), 500, 0));
                response = chain.proceed(builder.build());
                Log.i("ImageRetryInterceptor", "result:" + response.isSuccessful());

            }
            return response;
        }


        private boolean checkUrl(Request request) {
            return ImageUtlFormatHelper.checkWhiteList(request.url().host());
        }
    }
}
