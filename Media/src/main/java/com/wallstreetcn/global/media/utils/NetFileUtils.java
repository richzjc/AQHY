package com.wallstreetcn.global.media.utils;

import com.kronos.volley.toolbox.StringRequest;
import com.micker.helper.rx.RxUtils;
import com.micker.rpc.ResponseListener;
import com.micker.rpc.VolleyQueue;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Response;

public class NetFileUtils {

    public static void loadFileLength(String downloadUrl, ResponseListener<Long> listener) {
//        DecimalFormat decimalFormat = new DecimalFormat("##0.00");

        RxUtils.just(downloadUrl).map(s -> {
            Response response = null;
            try {
                response = VolleyQueue.getInstance().sync(new StringRequest(downloadUrl));
                return response.body().contentLength();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return 0L;
        }).subscribeOn(RxUtils.getSchedulerIo())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
//                    String fileSize = decimalFormat.format((long) o / 1024 / 1024) + "MB";
                    if (listener != null)
                        listener.onSuccess(o, false);
                }, Throwable::printStackTrace);
    }

    public static String getSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

}
