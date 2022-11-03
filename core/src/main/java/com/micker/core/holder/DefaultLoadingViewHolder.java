package com.micker.core.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.micker.core.R;
import com.micker.core.callback.IViewHolder;

/**
 * Created by Leif Zhang on 2016/12/9.
 * Email leifzhanggithub@gmail.com
 */

public class DefaultLoadingViewHolder implements IViewHolder {
    private View view;

    public DefaultLoadingViewHolder(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.base_fragment_dialog_loading, parent, false);
        view.setLayoutParams(new StaggeredGridLayoutManager.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public View getView() {
        return view;
    }
}
