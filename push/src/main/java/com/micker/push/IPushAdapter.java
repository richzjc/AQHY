package com.micker.push;

import android.content.Context;

/**
 * Created by Leif Zhang on 16/8/29.
 * Email leifzhanggithub@gmail.com
 */
public interface IPushAdapter {

    void init(Context context);

    void setTags();

    void toggle(boolean isEnable);

    void setAlias(String... alias);

    void saveRegId();

    void toggleVoice(boolean isEnable);

    void toggleVibrate(boolean isEnable);

    void pushTagTokenType(boolean isOpenApp);
}
