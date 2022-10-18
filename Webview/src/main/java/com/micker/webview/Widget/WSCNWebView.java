package com.micker.webview.Widget;


import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.*;
import com.micker.helper.SharedPrefsUtil;
import com.micker.helper.Util;
import com.micker.helper.file.FileUtil;
import com.micker.helper.image.ImageUtlFormatHelper;
import com.micker.helper.router.DoubleClickHelper;
import com.micker.helper.rx.RxUtils;
import com.micker.helper.system.EquipmentUtils;
import com.micker.helper.system.ScreenUtils;
import com.micker.rpc.VolleyQueue;
import com.micker.rpc.host.HostManager;
import com.micker.webview.javascript.CustomCallBack;
import com.micker.webview.javascript.JsBridge;
import com.micker.webview.util.WebViewDownloadUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.io.File;

/**
 * Created by micker on 16/7/1.
 */
public class WSCNWebView extends WebView {

    public WSCNWebView(Context context) {
        super(context);
        init();
    }

    public WSCNWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WSCNWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        configSettings();
        setListener();
    }

    private void setListener() {
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String s1, String s2, String s3, long l) {
                DoubleClickHelper.cleanDownTime();
                WebViewDownloadUtil.startDownload(url, s3, getContext());
            }
        });
    }

    private JsBridge jsBridge;

    private void configSettings() {
        jsBridge = new JsBridge(this);

        WebSettings webSetting = this.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        // 使用默认的缓存策略，cache没有过期就用cache
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        String ua = webSetting.getUserAgentString();
        SharedPrefsUtil.saveString("WebViewUa", ua);
        String platformName = Util.isPayment() ? "WscnProApp" : "WscnApp";
        ua += String.format(" %s/%s Yuta/%s", platformName, EquipmentUtils.getVersionName(), "0.1.2");
        ua += String.format(" %s/%s", "wscnStatusBarHeight", String.valueOf(ScreenUtils.px2dip(Util.getStatusBarHeight(getContext()))));
        ua += String.format(" packageName/%s", getContext().getPackageName());
        ua += String.format(" channel/%s", EquipmentUtils.getChannel());
        ua += String.format(" environment/%s", HostManager.getEnvironment());
        String mode = " wscnTheme/day";
        ua += mode;
        webSetting.setUserAgentString(ua);
        //   if (EquipmentUtils.getVersionName().contains("-debug")) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        //  }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }


    private WrapWebViewClient webViewClient;

    @Override
    public void setWebViewClient(WebViewClient client) {
        webViewClient = new WrapWebViewClient(this, client);
        super.setWebViewClient(webViewClient);
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        super.setWebChromeClient(new WrapWebChromeClient(client));
    }

    public void setWebViewFontSize(int fontSize) {
        getSettings().setDefaultFontSize(fontSize);
    }

    public void refreshData() {
        webViewClient.reloadTemp(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        jsBridge.Detach();
    }

    public String getShareTitle() {
        return webViewClient.getShareTitle();
    }

    public String getShareContent() {
        return webViewClient.getShareContent();
    }

    public void loadShareData() {
        webViewClient.loadShareData();
    }

    public String getShareImgUrl() {
        return webViewClient.getShareImgUrl();
    }

    public boolean isLoadingFinish() {
        return webViewClient.isLoadingFinish();
    }

    public void addMethod(CustomCallBack callBack) {
        String[] names = callBack.getCallbackFunctionName().split(",");
        for(String name : names){
            jsBridge.addMethod(name, callBack);
        }
    }

    public void loadJavaScript(String callbackId, String json) {
        jsBridge.loadJavaScript(callbackId, json);
    }

    @Deprecated
    public void loadJavaScriptWithoutDelete(String callbackId, String json) {
        jsBridge.loadJavaScriptWithoutDelete(callbackId, json);
    }

    public void loadHtmlFolder(final String folder, final String fileName) {
        RxUtils.just(folder + "/" + fileName).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return new String(FileUtil.readFile(folder + File.separator + fileName), "UTF-8");
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                String replacement = SharedPrefsUtil.getIsNightMode() ? "night-theme" : "";
                return s.replaceAll("#\\{(theme)\\}", replacement);
            }

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                loadDataWithBaseURL("file://" + folder + File.separator, s, "text/html", "utf-8", null);
            }

        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }

        });
    }

    public void loadHtml(final String assetFileFolder, final String fileName) {
        RxUtils.just(assetFileFolder + "/" + fileName).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return new String(FileUtil.readInputStream(getResources().getAssets()
                        .open(s)), "UTF-8");
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                String assets_theme = SharedPrefsUtil.getIsGreenColor() ? "greenup-theme" : "";
                s = s.replaceAll("#\\{(assets-theme)\\}", assets_theme);
                String replacement = SharedPrefsUtil.getIsNightMode() ? "night-theme" : "";
                return s.replaceAll("#\\{(theme)\\}", replacement);
            }

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                loadDataWithBaseURL("file:///android_asset/" + assetFileFolder + "/", s, "text/html", "utf-8", null);
            }

        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }

        });
    }

    int startScrollY = -1;
    boolean direction;
    OnScrollListener listener;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (startScrollY < 0) {
            startScrollY = t;
            direction = (t - oldt) > 0;
        }

        if (Math.abs(t - oldt) > 1 && listener != null)
            listener.onScroll(t - oldt);

        boolean flag = (t - oldt) > 0;
        if (flag != direction) {
            direction = flag;
            startScrollY = oldt;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((event.getAction() == MotionEvent.ACTION_CANCEL) || (event.getAction() == MotionEvent.ACTION_UP)) {
            startScrollY = -1;
        }
        return super.onTouchEvent(event);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    public interface OnScrollListener {
        void onScroll(int dy);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void loadUrl(String url) {
        if (ImageUtlFormatHelper.checkWhiteList(url)) {
            super.loadUrl(url, VolleyQueue.getInstance().getRequestHeader());
        } else {
            super.loadUrl(url);
        }
    }
}

