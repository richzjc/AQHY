package com.micker.aqhy.application.initial;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.micker.aqhy.application.init.FirstInit;

import java.util.ArrayList;
import java.util.List;

public class FirstInitial implements Initializer<FirstInit> {

    @NonNull
    @Override
    public FirstInit create(@NonNull Context context) {
        FirstInit init = new FirstInit();
        init.init(context);
        return init;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return new ArrayList<>();
    }
}
