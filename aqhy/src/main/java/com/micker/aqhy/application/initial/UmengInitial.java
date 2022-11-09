package com.micker.aqhy.application.initial;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;
import com.micker.aqhy.application.init.UmengInit;

import java.util.ArrayList;
import java.util.List;

public class UmengInitial implements Initializer<UmengInit> {
    @NonNull
    @Override
    public UmengInit create(@NonNull Context context) {
        UmengInit init = new UmengInit();
//        init.init(context);
        return init;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        List<Class<? extends Initializer<?>>> list = new ArrayList();
        list.add(FirstInitial.class);
        return list;
    }
}
