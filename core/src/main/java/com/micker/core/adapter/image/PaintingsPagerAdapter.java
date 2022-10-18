package com.micker.core.adapter.image;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import com.alexvasilkov.gestures.GestureController;
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.views.GestureFrameLayout;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.micker.core.R;
import com.micker.core.callback.GestureSettingsSetupListener;
import com.micker.core.imageloader.ImageLoadManager;
import com.micker.core.imageloader.WscnImageView;
import com.micker.helper.router.RouterHelper;
import com.micker.helper.system.ScreenUtils;

import java.util.List;

public class PaintingsPagerAdapter extends RecyclePagerAdapter<PaintingsPagerAdapter.ViewHolder> {

    private final ViewPager viewPager;
    private final GestureSettingsSetupListener setupListener;
    private List<String> items;
    private String title;
    private long createAt;

    public PaintingsPagerAdapter(ViewPager pager, List<String> items,
                                 GestureSettingsSetupListener listener) {
        this.viewPager = pager;
        this.items = items;
        this.setupListener = listener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup container) {
        ViewHolder holder = new ViewHolder(container);
        holder.frameLayout.getController().enableScrollInViewPager(viewPager);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (setupListener != null) {
            setupListener.onSetupGestureView(holder.frameLayout);
        }
        String imageEntity = items.get(position);
        ImageLoadManager.loadImage(imageEntity, holder.image, R.drawable.default_img,
                new BaseControllerListener<CloseableStaticBitmap>() {
                    @Override
                    public void onFinalImageSet(String id, @Nullable final CloseableStaticBitmap imageInfo, @Nullable Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        holder.image.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(holder.image.getLayoutParams());
                                    params.width = ScreenUtils.getScreenWidth();
                                    params.height = imageInfo.getHeight() * params.width / imageInfo.getWidth();
                                    if(params.height > ScreenUtils.getScreenHeight() * 0.8)
                                        holder.image.setScaleType(ImageView.ScaleType.FIT_START);
                                    else {
                                        holder.image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    }
                                    holder.image.setLayoutParams(params);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });

    }

    static class ViewHolder extends RecyclePagerAdapter.ViewHolder {
        final WscnImageView image;
        final GestureFrameLayout frameLayout;
        int scaleTouchSlop;

        ViewHolder(ViewGroup container) {
            super(LayoutInflater.from(container.getContext()).inflate(R.layout.base_recycler_item_image_gallery,
                    container, false));
            frameLayout = itemView.findViewById(R.id.layout);
            image = itemView.findViewById(R.id.imageView);
            ViewConfiguration viewConfiguration = ViewConfiguration.get(container.getContext());
            scaleTouchSlop = viewConfiguration.getScaledTouchSlop();
            frameLayout.getController().setOnGesturesListener(new GestureController.SimpleOnGestureListener() {
                private float downX, downY;
                private boolean isLongPress;

                @Override
                public void onDown(@NonNull MotionEvent e) {
                    downX = e.getX();
                    downY = e.getY();
                    isLongPress = false;
                }

                @Override
                public void onUpOrCancel(@NonNull MotionEvent e) {
                    float x = e.getX(), y = e.getY();
                    if (isLongPress) {
                        return;
                    }
                    if (Math.abs(downX - x) < scaleTouchSlop && Math.abs(downY - y) < scaleTouchSlop) {
                        Activity activity = RouterHelper.getActivity(image.getContext());
                        if (activity != null) {
                            activity.finish();
                        }
                    }
                }

                @Override
                public void onLongPress(@NonNull MotionEvent event) {
                    super.onLongPress(event);
                    isLongPress = true;
                }
            });

        }
    }
}
