package com.micker.push;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.micker.helper.SharedPrefsUtil;
import com.micker.helper.rx.RxUtils;
import com.micker.helper.system.EquipmentUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by Leif Zhang on 16/8/29.
 * Email leifzhanggithub@gmail.com
 */
public abstract class BasePushAdapter implements IPushAdapter {

    protected Context mContext;
    protected boolean isReady = false;

    @Override
    public void init(Context context) {
        mContext = context;
    }

    @Override
    public final void setAlias(String... alias) {
        if (isReady) {
            RxUtils.empty().doOnNext(s -> readyToDealAlias(alias))
                    .subscribeOn(RxUtils.getSchedulerIo())
                    .subscribe(s -> {

                    }, Throwable::printStackTrace);
        } else {
            RxUtils.empty().delay(1, TimeUnit.SECONDS).subscribe(s -> setAlias(alias), Throwable::printStackTrace);
        }
    }

    public void readyToDealAlias(String... alias) {

    }

    protected abstract String getType();

    protected String getDeviceId() {
        return EquipmentUtils.getIMEI();
    }

    @Override
    public void pushTagTokenType(boolean isOpenApp) {
        String regId = SharedPrefsUtil.getString("regId");
        if (!TextUtils.isEmpty(regId)) {
//            pushToken(regId, getType(), isOpenApp);
            pushCallback();
        } else {
            pushCallback();
        }
    }


    public void pushCallback(){
//        if(mContext instanceof PushCallback)
//            ((PushCallback)mContext).responsePushCallback(time);
    }

    public void pushToken(String token, String type, boolean isOpenApp) {
//        long userId = 0;
//        if(AccountManager.INSTANCE.isLogin())
//            userId = AccountManager.INSTANCE.getAccountUserId();
//        Bundle bundle = new Bundle();
//        bundle.putString("appType", "tail_android");
//        bundle.putString("deviceId", EquipmentUtils.getIMEI());
//        bundle.putString("deviceType", "android");
//        bundle.putString("deviceToken", token);
//        new UpdatePushTokenApi(bundle, userId, new ResponseListener<String>() {
//            @Override
//            public void onSuccess(String model, boolean isCache) {
//                pushCallback();
//            }
//
//            @Override
//            public void onErrorResponse(int code, String error) {
//                pushCallback();
//            }
//        }).start();
    }
}
