package com.micker.webview.Widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;

import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.micker.core.manager.AppManager;
import com.micker.helper.ResourceUtils;
import com.micker.helper.router.ActivityHelper;
import com.micker.helper.router.RouterHelper;
import com.micker.webview.R;
import com.micker.webview.javascript.AndroidShareInterface;

/**
 * Created by Leif Zhang on 16/8/9.
 * Email leifzhanggithub@gmail.com
 */
public class WrapWebViewClient extends WSCNWebClient {
    private WebViewClient webViewClient;
    private AndroidShareInterface javaScriptShareObj;

    public WrapWebViewClient(WebView view, WebViewClient webViewClient) {
        this.webViewClient = webViewClient;
        javaScriptShareObj = new AndroidShareInterface(view);
    }

    public void reloadTemp(WebView view) {
        WebViewCompat.loadJsFunction(view, "window.__YutaAppOnPrepare()");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();
            if (!TextUtils.equals(scheme, "wscn") && !TextUtils.equals("http", scheme)
                    && !TextUtils.equals("https", scheme)) {
                startOtherApp(view, url);
                return true;
            }
            if (RouterHelper.openWithReturn(uri.toString(), view.getContext())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webViewClient.shouldOverrideUrlLoading(view, url);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        try {
            Uri uri = request.getUrl();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                String scheme = uri.getScheme();
                if (!TextUtils.equals(scheme, "wscn") && !TextUtils.equals("http", scheme)
                        && !TextUtils.equals("https", scheme)) {
                    startOtherApp(view, uri.toString());
                    return true;
                }
            }
            if (RouterHelper.openWithReturn(uri.toString(), view.getContext())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.shouldOverrideUrlLoading(view, request);

    }


    private void startOtherApp(final View view, final String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        final Context context = view.getContext();
        boolean isInstall = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
        if (isInstall) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
            builder.setCancelable(false);
            builder.setTitle(ResourceUtils.getResStringFromId(R.string.webview_goto_another_app));
            builder.setPositiveButton(ResourceUtils.getResStringFromId(R.string.webview_confirm_text), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ActivityHelper.startActivity(intent, (Activity) context);
                    if (!AppManager.getAppManager().isSingleActivity())
                        ((Activity) context).finish();
                }
            });
            builder.setNegativeButton(ResourceUtils.getResStringFromId(R.string.webview_cancel_text), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

//    private Intent getIntent(String url, Context context) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Uri uri = Uri.parse(url);
//            String schema = uri.getScheme();
//            if (schema.startsWith("file")) {
//                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                String authority = context.getPackageName() + ".provider";
//                Uri contentUri = FileProvider.getUriForFile(context, authority, new File(url));
//                intent.setData(contentUri);
//            } else {
//                intent.setData(Uri.parse(url));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//        } else {
//            intent.setData(Uri.parse(url));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//        return intent;
//    }

    @Override
    public void onPageFinished(WebView view, String url) {
        try {
            webViewClient.onPageFinished(view, url);
          /*  WebViewCompat.loadJsFunction(view, TextUtil.format("window.__YutaConnectionType('%s')",
                    TDevice.getCurrentNetworkType()));*/
            reloadTemp(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        handler.proceed();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        javaScriptShareObj.onWebViewLoadingStart();
        webViewClient.onPageStarted(view, url, favicon);
    }

    public String getShareTitle() {
        return javaScriptShareObj.getShareTitle();
    }

    public String getShareContent() {
        return javaScriptShareObj.getShareContent();
    }

    public String getShareImgUrl() {
        return javaScriptShareObj.getShareImgUrl();
    }

    public boolean isLoadingFinish() {
        return javaScriptShareObj.isLoadingFinish();
    }

    public void loadShareData() {
        javaScriptShareObj.onWebViewLoadingFinish();
    }
}
