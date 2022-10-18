package com.micker.aqhy.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.facebook.drawee.drawable.ScalingUtils;
import com.micker.aqhy.activity.GuideActivity;
import com.micker.core.imageloader.ImageLoadManager;
import com.micker.core.imageloader.WscnImageView;
import java.util.List;

/**
 * Created by Leif Zhang on 16/8/17.
 * Email leifzhanggithub@gmail.com
 */
public class GuideAdapter extends PagerAdapter {
    private List<Integer> images;

    public GuideAdapter(List<Integer> images) {
        this.images = images;
    }


    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        WscnImageView imageView = new WscnImageView(container.getContext());
        imageView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
        ImageLoadManager.loadImage(images.get(position), imageView, 0);
        container.addView(imageView);
        if(position == images.size() - 1){
            imageView.setOnClickListener(view -> {
                if(container.getContext() instanceof GuideActivity){
                    GuideActivity activity = (GuideActivity) container.getContext();
                    activity.responseToJump();
                }
            });
        }
        return imageView;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
