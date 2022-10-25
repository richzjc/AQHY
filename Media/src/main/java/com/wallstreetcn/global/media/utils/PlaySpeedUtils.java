package com.wallstreetcn.global.media.utils;

import java.util.ArrayList;
import java.util.List;

public class PlaySpeedUtils {

    public static final float s_speed = 1.f;

    public static float speeds[] = {0.75f, s_speed, 1.25f, 1.5f, 2f};

    public static List<CharSequence> getSpeedsValues() {
        List<CharSequence> list = new ArrayList<>();
        for (float speed : speeds) {
            list.add(speed + "x");
        }
        return list;
    }

    public static int getIndexSpeed(float speed) {
        for (int i = 0; i < speeds.length; i++) {
            if (speed == speeds[i])
                return i;
        }
        return 0;
    }
}
