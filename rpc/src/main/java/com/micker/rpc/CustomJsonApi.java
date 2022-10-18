package com.micker.rpc;

import android.os.Bundle;
import android.text.TextUtils;
import com.kronos.volley.Request;
import com.kronos.volley.Response;
import com.kronos.volley.VolleyError;
import com.kronos.volley.toolbox.BaseApiParser;
import com.kronos.volley.toolbox.StringRequest;
import com.micker.helper.rx.RxUtils;
import com.micker.rpc.exception.ErrorCode;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * Created by zhangyang on 16/1/27.
 */
public abstract class CustomJsonApi<T> extends AbstractApi<T> {
    public CustomJsonApi() {
        super();
    }

    public CustomJsonApi(Bundle bundle) {
        super(bundle);
    }

    public CustomJsonApi(ResponseListener<T> responseListener) {
        super(responseListener);
    }

    public CustomJsonApi(ResponseListener<T> responseListener, Bundle bundle) {
        super(responseListener, bundle);
    }

    @Override
    public StringRequest getRequest() {
        String url = getRealUrl();
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        RpcJsonRequest request = new RpcJsonRequest(url);
        request.setRequestListener(response -> {
            try {
                if (responseListener != null) {
                    responseListener.onSuccess(response.getData(), response.isCache);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).setErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    int errorCode = error.networkResponse == null ? 600 :
                            error.networkResponse.statusCode;
                    String errorMessage = error.networkResponse == null ? "" :
                            error.networkResponse.errorResponseString;
                    onError(errorCode, errorMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setMethod(Method()).setHeader(getHeader()).setApiParser(getParser()).setCacheTime(cacheTime).setIsRefreshNeed(isNeedRefresh);
        request.setRequestBody(getRealJsonBody());
        return request;
    }

    private JSONObject getRealJsonBody(){
        JSONObject jobj = getRequestJSONBody();
        if(jobj == null){
            jobj = new JSONObject();
        }
        Map<String, String> map = VolleyQueue.getInstance().getPostBody();
        for (String key : map.keySet()) {
            try {
                Object object = map.get(key);
                jobj.put(key, object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jobj;
    }


    public final int onVolleyError(VolleyError error) {
        try {
            int errorCode = error.networkResponse == null ? ErrorCode.EMPTYURL :
                    error.networkResponse.statusCode;
            String errorMessage = error.networkResponse == null ? "" :
                    error.networkResponse.errorResponseString;
            onError(errorCode, errorMessage);
            return errorCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ErrorCode.EMPTYURL;
    }

    @Override
    public Observable<T> observable() {
        return super.observable().observeOn(AndroidSchedulers.mainThread()).doOnError(throwable -> {
            if (throwable instanceof VolleyError) {
                onVolleyError((VolleyError) throwable);
            }
        }).observeOn(RxUtils.getSchedulerIo());
    }


    public JSONObject getRequestJSONBody() {
        return null;
    }

    @Override
    public int Method() {
        return Request.Method.POST;
    }

    @Override
    public BaseApiParser getParser() {
        return new StringApiParser();
    }

    public JSONObject getJsonFromBundle() {
        JSONObject jsonObject = new JSONObject();
        for (String key : bundle.keySet()) {
            try {
                Object object = bundle.get(key);
                if (object instanceof Boolean) {
                    jsonObject.put(key, object);
                } else {
                    jsonObject.put(key, object + "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }


}
