package com.micker.webview.javascript;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.micker.helper.ResourceUtils;
import com.micker.webview.R;
import com.micker.webview.model.JavascriptFunction;


/**
 * Created by zhangyang on 16/6/27.
 */
public class AndroidShareInterface implements JavascriptFunction {
    private String share_title, share_content, share_imgUrl;
    private boolean isLoadingFinish = false;
    private WebView mWebView;


    public AndroidShareInterface(WebView webView) {
        this.mWebView = webView;
        mWebView.addJavascriptInterface(this, "share_obj");
    }

    public void onWebViewLoadingStart() {
        isLoadingFinish = false;
    }

    public void onWebViewLoadingFinish() {
        mWebView.loadUrl("javascript:window.share_obj.getTitle(document.querySelector" +
                "(\"meta[name='wscn-share-title']\").content).getContent(document.querySelector" +
                "(\"meta[name='wscn-share-content']\").content).getImgUrl(document.querySelector" +
                "(\"link[rel='wscn-share-image']\").href);");
    }

    @JavascriptInterface
    public AndroidShareInterface getTitle(String title) {
        share_title = TextUtils.isEmpty(title) ? ResourceUtils.getResStringFromId(R.string.webview_wall_sits_app) : title;
        return this;
    }

    @JavascriptInterface
    public AndroidShareInterface getContent(String content) {
        isLoadingFinish = true;
        share_content = TextUtils.isEmpty(content) ? ResourceUtils.getResStringFromId(R.string.webview_invite_discuss_wits) : content;
        return this;
    }

    @JavascriptInterface
    public AndroidShareInterface getImgUrl(String imgUrl) {
        share_imgUrl = imgUrl;
        return this;
    }

    public String getShareTitle() {
        return share_title;
    }

    public String getShareContent() {
        return share_content;
    }

    public String getShareImgUrl() {
        return share_imgUrl;
    }

    public boolean isLoadingFinish() {
        return isLoadingFinish;
    }
}
