package com.micker.webview.Widget;

import android.os.Build;
import android.webkit.WebView;


/**
 * Created by Leif Zhang on 16/8/30.
 * Email leifzhanggithub@gmail.com
 */
public class WebViewCompat {
    public static void loadJsFunction(WebView webView, String javascript) {
        if(webView == null)
            return;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            webView.evaluateJavascript(javascript, null);
        } else {
            webView.loadUrl(String.format("javascript:%s", javascript));
        }
    }
}
