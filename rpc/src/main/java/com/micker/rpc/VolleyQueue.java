package com.micker.rpc;

import android.text.TextUtils;
import android.webkit.WebSettings;
import androidx.annotation.NonNull;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.kronos.volley.Network;
import com.kronos.volley.Request;
import com.kronos.volley.RequestQueue;
import com.kronos.volley.toolbox.BasicNetwork;
import com.kronos.volley.toolbox.DiskBasedCache;
import com.kronos.volley.toolbox.NetResponse;
import com.kronos.volley.toolbox.StringRequest;
import com.micker.helper.SharedPrefsUtil;
import com.micker.helper.UtilsContextManager;
import com.micker.helper.system.EquipmentUtils;
import com.micker.rpc.exception.BaseErrorCodeFactory;
import com.micker.rpc.manager.WscnDns;
import com.micker.rpc.manager.WscnOkHttpStack;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyang on 16/5/3.
 */
public class VolleyQueue {

    private RequestQueue requestQueue;
    private String userAgent;
    private Map<String, String> postBody;

    public void addRequest(Request request) {
        try {
            start();
            //removeRequest(request);
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUserAgent() {
        String userAgent = SharedPrefsUtil.getString("WebViewUa", "");
        if (TextUtils.isEmpty(userAgent)) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(UtilsContextManager.getInstance().getApplication());
                SharedPrefsUtil.saveString("WebViewUa", userAgent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userAgent;
    }

    private static VolleyQueue instance;

    public static VolleyQueue getInstance() {
        if (instance == null) {
            synchronized (VolleyQueue.class) {
                // 第二次检查
                if (instance == null) {
                    instance = new VolleyQueue();
                }
            }
        }
        return instance;
    }

    private VolleyQueue() {
        requestQueue = newRequestQueue();
        userAgent = getUserAgent();
        header = new HashMap<>();
        postBody = new HashMap<>();
    }

    /**
     * Default on-disk cache directory.
     */
    private static final String DEFAULT_CACHE_DIR = "volley";
    private WscnOkHttpStack stack;
    private OkHttpClient okClient;

    private RequestQueue newRequestQueue() {
        File cacheDir = new File(UtilsContextManager.getInstance().getApplication()
                .getCacheDir(), DEFAULT_CACHE_DIR);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .dns(new WscnDns())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 5, TimeUnit.MINUTES));
        client.addNetworkInterceptor(chain -> {
            okhttp3.Request.Builder builder = chain.request().newBuilder();
            if (!TextUtils.isEmpty(userAgent)) {
                builder.removeHeader("User-Agent").addHeader("User-Agent", userAgent);
            }
            return chain.proceed(builder.build());
        });
        if (EquipmentUtils.getVersionName().contains("-debug")) {
            client.addNetworkInterceptor(new StethoInterceptor());
        }
        okClient = client.build();
        stack = new WscnOkHttpStack(okClient);
        Network network = new BasicNetwork(stack);
        RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
        queue.start();
        return queue;
    }

    private HashMap<String, String> header;

    public void addHeader(Map<String, String> map) {
        header.putAll(map);
        requestQueue.addHeader(map);
    }

    public void addPostBody(Map<String, String> map) {
        postBody.putAll(map);
    }

    @NonNull
    public OkHttpClient getOkClient() {
        return okClient;
    }

    public Response sync(Request request) throws Exception {
        return stack.performRequest(request, new HashMap<>());
    }

    private boolean isRestart = false;

    public void start() {
        if (isRestart) {
            requestQueue.start();
            isRestart = false;
        }
    }

    public void stop() {
        requestQueue.stop();
        isRestart = true;
    }

    private BaseErrorCodeFactory factory;

    public BaseErrorCodeFactory getFactory() {
        return factory;
    }

    public void setFactory(BaseErrorCodeFactory factory) {
        this.factory = factory;
    }

    public void removeRequest(final String url) {
        requestQueue.cancelAll(request -> TextUtils.equals(request.getUrl(), url));
    }

    private void removeRequest(final Request<?> newRequest) {
        List<Request<?>> requestList = new ArrayList<>();
        requestQueue.cancelAll(request -> {
            if (TextUtils.equals(request.getUrl(), newRequest.getUrl())) {
                requestList.add(request);
                return true;
            }
            return false;
        });
        if (!requestList.isEmpty()) {
            requestList.add(newRequest);
            ResponseListListener listener = getRequest(requestList);
            newRequest.setErrorListener(listener);
            if (newRequest instanceof StringRequest) {
                ((StringRequest) newRequest).setRequestListener(listener);
            }
        }
    }

    private ResponseListListener getRequest(List<Request<?>> requestList) {
        List<com.kronos.volley.Response.Listener<NetResponse>> list = new ArrayList<>();
        List<com.kronos.volley.Response.ErrorListener> errorListenerList = new ArrayList<>();
        for (Request<?> request : requestList) {
            if (request instanceof RpcStringRequest) {
                RpcStringRequest stringRequest = (RpcStringRequest) request;
                list.add(stringRequest.requestListener);
                errorListenerList.add(stringRequest.getErrorListener());
            } else if (request instanceof RpcJsonRequest) {
                RpcJsonRequest jsonRequest = (RpcJsonRequest) request;
                list.add(jsonRequest.requestListener);
                errorListenerList.add(jsonRequest.getErrorListener());
            }
        }
        return new ResponseListListener(list, errorListenerList);
    }

    public Map<String, String> getRequestHeader() {
        return header;
    }
    public Map<String, String> getPostBody() {
        return postBody;
    }
}
