package com.micker.webview.Template;

import android.view.View;
import com.micker.core.base.BaseFragment;
import com.micker.core.widget.pulltorefresh.PullToRefreshAdapterView;
import com.micker.webview.R;
import com.micker.webview.Widget.WSCNWebClient;
import com.micker.webview.Widget.WSCNWebView;

/**
 * Created by micker on 16/7/1.
 */
public class WSCNWebViewFragment extends BaseFragment {
    protected PullToRefreshAdapterView ptrView;
    protected WSCNWebView webView;

    @Override
    public int doGetContentViewId() {
        return R.layout.wscn_fragment_webview;
    }

    @Override
    public void doInitSubViews(View view) {
        super.doInitSubViews(view);
        ptrView = mViewQuery.findViewById(R.id.ptrView);
        webView = mViewQuery.findViewById(R.id.webView);
        webView.setWebViewClient(new WSCNWebClient());
    }

    @Override
    public void doInitData() {
        super.doInitData();
    }

    @Override
    public void onDetach() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            webView.getClass().getMethod("onResume").invoke(webView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doAfter() {
        super.doAfter();
    }
}
