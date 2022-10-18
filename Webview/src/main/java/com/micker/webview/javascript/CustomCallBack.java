package com.micker.webview.javascript;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by zhangyang on 16/3/22.
 */
public abstract class CustomCallBack implements JsCallback {
    protected String callbackFunctionName;
    private Activity mActivity;
    private String callBackId;

    public void attach(Activity context) {
        this.mActivity = context;
    }

    public void detach() {
        this.mActivity = null;
    }

    public String getCallbackFunctionName() {
        return callbackFunctionName;
    }

    public AppCompatActivity getActivity() {
        return (AppCompatActivity) mActivity;
    }

    @Override
    public void setCallBackId(String callBackId) {
        this.callBackId = callBackId;
    }

    @Override
    public String getCallBackId() {
        return callBackId;
    }

    public boolean redirectloadDownload(){return false;};
}
