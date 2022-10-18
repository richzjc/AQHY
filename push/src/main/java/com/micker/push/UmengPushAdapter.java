package com.micker.push;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.micker.helper.SharedPrefsUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;

/**
 * Created by Leif Zhang on 16/8/29.
 * Email leifzhanggithub@gmail.com
 */
public class UmengPushAdapter extends BasePushAdapter {

    private PushAgent mPushAgent;
    public static final String TAG = "UmengPushAdapter";

    public UmengPushAdapter() {

    }

    @Override
    public void init(Context context) {
        super.init(context);
        PushAgent.DEBUG = true;
        UMConfigure.setLogEnabled(false);

        mPushAgent = PushAgent.getInstance(mContext);
        mPushAgent.setResourcePackageName("com.wallstreetcn.alien");

        registerToken();


        mPushAgent.setDisplayNotificationNumber(10);
        mPushAgent.setMessageHandler(new WSCNUmengMessageHandler());
        mPushAgent.setNotificationClickHandler(new WSCNNotifyHandler());
        if (true) {
            mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        } else {
            mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        }
        if (true) {
            mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        } else {
            mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        }
    }

    private void registerToken() {
        if (mPushAgent != null) {
            mPushAgent.register(new IUmengRegisterCallback() {
                @Override
                public void onSuccess(String s) {
                    isReady = true;
                    deleteTag();
                    saveRegId(s);
                    Log.i("pushToken", s);
                    pushTagTokenType(true);
                    mPushAgent.onAppStart();
                    mPushAgent.enable(new SimpleUmengCallback());
                }

                @Override
                public void onFailure(String s, String s1) {
                    Log.i("umeng", "s = " + s + "; \ns1 = " + s1);
                    pushCallback();
                }
            });
        }else{
            pushCallback();
        }
    }

    private void deleteTag() {
    }

    @Override
    public void setTags() {
        String regId = SharedPrefsUtil.getString("regId", "");
        if (TextUtils.isEmpty(regId)) {
            registerToken();
        } else {
            pushTagTokenType(false);
        }
    }

    @Override
    public void toggle(boolean isEnable) {
        if (isEnable) {
            registerToken();
        } else {
            mPushAgent.disable(new SimpleUmengCallback() {
                @Override
                public void onSuccess() {
                    pushTagTokenType(false);
                }
            });
        }
    }

    @Override
    public void saveRegId() {

    }

    @Override
    public void readyToDealAlias(String... alias) {
        super.readyToDealAlias(alias);
    }

    @Override
    protected String getType() {
        return "";
    }


    public void saveRegId(String key) {
        Log.i(TAG, key);
        SharedPrefsUtil.saveString("regId", key);
        pushTagTokenType(false);
    }

    @Override
    public void toggleVoice(boolean isEnable) {
    }

    @Override
    public void toggleVibrate(boolean isEnable) {
    }
}