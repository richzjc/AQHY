package com.micker.push;

import android.content.Context;
import android.os.Bundle;
import com.micker.core.manager.AppManager;
import com.micker.helper.router.ActivityHelper;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by Leif Zhang on 16/8/29.
 * Email leifzhanggithub@gmail.com
 */
public class WSCNNotifyHandler extends UmengNotificationClickHandler {
    @Override
    public void dealWithCustomAction(Context context, UMessage uMessage) {
        try {
            Class cls = Class.forName("com.micker.aqhy.activity.MainActivity");
            if (!AppManager.getAppManager().isActivityAlive(cls)) {
                goMainActivity(context, 0);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void goMainActivity(Context context, int index) {
        try {
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            Class cls = Class.forName("com.micker.aqhy.activity.MainActivity");
            ActivityHelper.startActivityNotInActivity(context,cls, bundle);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
