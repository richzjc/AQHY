package com.wallstreetcn.global.media.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.micker.core.callback.IViewHolder;
import com.wallstreetcn.global.media.R;

import androidx.annotation.DrawableRes;

/**
 * Created by  Leif Zhang on 2017/12/18.
 * Email leifzhanggithub@gmail.com
 */

public class MediaControlProgressBar implements IViewHolder {
    private View view;
    private ProgressBar bar;
    private ImageView leftIv, rightIv;

    public MediaControlProgressBar(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.global_view_media_progress, null);
        bar = view.findViewById(R.id.progressBar);
        leftIv = view.findViewById(R.id.leftImage);
        rightIv = view.findViewById(R.id.rightImage);
    }

    public void initBar(@DrawableRes int leftIcon, @DrawableRes int rightIcon, int maxProgress) {
        bar.setMax(maxProgress);
        leftIv.setImageResource(leftIcon);
        rightIv.setImageResource(rightIcon);
    }

    public void setProgress(int progress) {
        bar.setProgress(progress);
    }

    @Override
    public View getView() {
        return view;
    }

    public void setVisibility(int visibility) {
        view.setVisibility(visibility);
    }
}
