package com.wallstreetcn.global.media.controller;

import com.wallstreetcn.global.media.model.PlayUriEntity;

/**
 * Created by chanlevel on 2017/1/10.
 */

public interface LiveSourceChangeCallback {

    void change(PlayUriEntity url);

}
