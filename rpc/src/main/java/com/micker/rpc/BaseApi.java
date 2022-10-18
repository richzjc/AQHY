package com.micker.rpc;

import com.kronos.volley.toolbox.BaseApiParser;
import com.kronos.volley.toolbox.StringRequest;
import io.reactivex.Observable;

import java.util.Map;


/**
 * Created by zhangyang on 16/1/20.
 */
public interface BaseApi<T> {
    StringRequest getRequest();

    String getUrl();

    Map<String, String> getHeader();

    int Method();

    BaseApiParser getParser();

    void start();

    void cancel();

    T sync() throws Exception;

    Observable<T> observable();
}
