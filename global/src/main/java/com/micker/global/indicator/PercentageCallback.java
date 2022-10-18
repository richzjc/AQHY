package com.micker.global.indicator;

/**
 * Created by wscn on 16/12/13.
 */

public interface PercentageCallback {

    void onEnter(int i, float interpolation);

    void onLeave(int i, float interpolation);
}
