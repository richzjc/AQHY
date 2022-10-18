package com.micker.webview.Template;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.micker.core.base.BaseActivity;
import com.micker.core.widget.TitleBar;
import com.micker.core.widget.pulltorefresh.IRefreshListener;
import com.micker.core.widget.pulltorefresh.PullToRefreshAdapterView;
import com.micker.helper.observer.Observer;
import com.micker.helper.observer.ObserverManger;
import com.micker.webview.R;
import com.micker.webview.Widget.NestedWebView;
import com.micker.webview.Widget.WSCNWebChromeClient;
import com.micker.webview.Widget.WSCNWebClient;


/**
 * Created by micker on 16/7/1.
 */
public class WSCNWebViewActivity extends BaseActivity implements IRefreshListener, Observer {

    private String url = "",
            title = "";

    private TitleBar titleBar;
    private PullToRefreshAdapterView mPullToRefreshLayout;
    private NestedWebView webView;
    private CoordinatorLayout coordinator;

    @Override
    public int doGetContentViewId() {
        return R.layout.wscn_activity_webview;
    }

    @Override
    public void doInitSubViews(View view) {
        super.doInitSubViews(view);
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        mPullToRefreshLayout = (PullToRefreshAdapterView)findViewById(R.id.mPullToRefreshLayout);
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinator);
        webView = (NestedWebView)findViewById(R.id.webView);
        mPullToRefreshLayout.setCanRefresh(false);
        setListener();
    }

    private void setListener() {
        mPullToRefreshLayout.setRefreshListener(this);
        webView.setWebChromeClient(new WSCNWebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 60) {
                    dismissDialog();
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                titleBar.setTitle(title);
                WSCNWebViewActivity.this.title = title;
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }
        });

        webView.setWebViewClient(client);
    }

    WSCNWebClient client = new WSCNWebClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.setVisibility(View.VISIBLE);
            mPullToRefreshLayout.onRefreshComplete();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            mPullToRefreshLayout.onRefreshComplete();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mPullToRefreshLayout.onRefreshComplete();
        }

    };


    @Override
    public void doInitData() {
        super.doInitData();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            url = bundle.getString("url", "");
            title = bundle.getString("title", "");
            titleBar.setTitle(title);
        }

        showDialog();
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("file:///android_assets")) {
                String[] array = url.split("/");
                if (array.length > 2) {
                    int length = array.length;
                    webView.loadHtml(array[length - 2], array[length - 1]);
                    titleBar.setRightBtn2Text("");
                } else {
                    webView.loadUrl(url);
                }
            } else {
                webView.loadUrl(url);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            webView.getClass().getMethod("onResume").invoke(webView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        ObserverManger.getInstance().removeObserver(this);
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        webView.reload();
    }

    public static final int ACCOUNT_STATE_CHANGED = 6100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObserverManger.getInstance().registerObserver(this, ACCOUNT_STATE_CHANGED);
    }

    @Override
    public void update(int id, Object... args) {
        try {
            webView.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
