package com.micker.rpc;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kronos.volley.Request;
import com.kronos.volley.VolleyError;
import com.kronos.volley.toolbox.StringRequest;
import com.micker.helper.ResourceUtils;
import com.micker.helper.rx.RxUtils;
import com.micker.rpc.exception.ErrorCode;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by zhangyang on 16/1/20.
 */
public abstract class CustomHtmlApi<T> extends AbstractApi<T> {
    public CustomHtmlApi() {
        this(null, null);
    }

    public CustomHtmlApi(Bundle bundle) {
        this(null, bundle);
    }

    public CustomHtmlApi(ResponseListener<T> responseListener) {
        this(responseListener, null);
    }

    public CustomHtmlApi(ResponseListener<T> responseListener, Bundle bundle) {
        super(responseListener, bundle);
        setCacheExtra("");
    }


    @Override
    public StringRequest getRequest() {
        String url = getRealUrl();
        if (TextUtils.isEmpty(url)) {
            responseListener.onErrorResponse(ErrorCode.EMPTYURL, ResourceUtils.getResStringFromId(R.string.rpc_url_is_empty));
            return null;
        }
        RpcHtmlRequest request = new RpcHtmlRequest(url);
        request.setRequestListener(response -> {
            try {
                if (responseListener != null) {
                    responseListener.onSuccess(response.getData(), response.isCache);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).setErrorListener(this::onVolleyError).setMethod(Method()).setHeader(getHeader()).setApiParser(getParser())
                .setCacheTime(cacheTime).setIsRefreshNeed(isNeedRefresh).setIgnoreExpired(ignoreExpired);
        if (Method() == Request.Method.POST) {
            request.setRequestBody(getRequestBody());
        }
        if (!TextUtils.isEmpty(cacheExtra)) {
            request.setCacheExtra(cacheExtra);
        }
        return request;
    }

    @Override
    public void setCacheExtra(String cacheExtra) {
        String language = VolleyQueue.getInstance().getRequestHeader().get("X-Language");
        language = TextUtils.isEmpty(language) ? "" : language;
        super.setCacheExtra(cacheExtra + language);
    }

    @Override
    public Observable<T> observable() {
        return super.observable().observeOn(AndroidSchedulers.mainThread()).doOnError(throwable -> {
            if (throwable instanceof VolleyError) {
                onVolleyError((VolleyError) throwable);
                throwable.printStackTrace();
            }
        }).observeOn(RxUtils.getSchedulerIo());
    }

    public final int onVolleyError(VolleyError error) {
        try {
            int errorCode = error.networkResponse == null ? ErrorCode.EMPTYURL :
                    error.networkResponse.statusCode;
            String errorMessage = error.networkResponse == null ? "" :
                    error.networkResponse.errorResponseString;
            Log.i("onVolleyError", getRealUrl());
            onError(errorCode, errorMessage);
            return errorCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ErrorCode.EMPTYURL;
    }

    @Override
    public int Method() {
        return Request.Method.GET;
    }

    @NonNull
    protected HashMap<String, String> getHashMapFromBundle() {
        HashMap<String, String> map = new HashMap<>();
        for (String key : bundle.keySet()) {
            map.put(key, String.valueOf(bundle.get(key)));
        }
        return map;
    }

}
