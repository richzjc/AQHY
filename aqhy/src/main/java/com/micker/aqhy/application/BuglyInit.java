package com.micker.aqhy.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import com.micker.aqhy.R;
import com.micker.aqhy.activity.MainActivity;
import com.micker.helper.ResourceUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.ui.UILifecycleListener;

/**
 * Created by zhangyang on 2017/6/16.
 */

public class BuglyInit {
    private static String TAG = "";

    public static void init(Application application) {
        String buglyAppId = ResourceUtils.getResStringFromId(R.string.buglyAppId);
        Beta.autoCheckUpgrade = true;
        Beta.canShowApkInfo = false;
        Beta.tipsDialogLayoutId = R.layout.tail_dialog_update;
        Beta.upgradeDialogLayoutId = R.layout.tail_dialog_update;
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onCreate");

            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStart");
            }

            @Override
            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onResume");
            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onPause");
            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStop");
            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onDestroy");
            }
        };
        Bugly.init(application, buglyAppId, false);
    }

}
