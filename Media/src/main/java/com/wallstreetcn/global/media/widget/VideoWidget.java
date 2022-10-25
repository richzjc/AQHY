package com.wallstreetcn.global.media.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import com.wallstreetcn.helper.utils.TLog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.wallstreetcn.baseui.customView.IconView;

import com.wallstreetcn.global.media.R;
import com.wallstreetcn.helper.utils.rx.RxUtils;
import com.wallstreetcn.helper.utils.system.ScreenUtils;

import java.util.concurrent.TimeUnit;


/**
 * Created by Leif Zhang on 2017/1/12.
 * Email leifzhanggithub@gmail.com
 */

public class VideoWidget implements IFloatContainer {
    public static final String                     TAG = VideoWidget.class.getSimpleName();
    private             WindowManager              mWindowManager;
    private             Context                    mContext;
    private             WindowManager.LayoutParams params;
    private             VideoSize                  bigSize, smallSize;


    private int state = VideoViewState.STATE_TOP;


    public VideoWidget(Context context) {
        this.mContext = context;
    }

    public void init() {
        //获取WindowManager
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        //设置LayoutParams(全局变量）相关参数
        initLayoutParams();

        initBackgroundView();


        backgroundView.setSize(bigSize, smallSize);

        // backgroundView.setVisibility(View.GONE);
        mWindowManager.addView(backgroundView, params);
    }

    public WindowManager.LayoutParams getLayoutParams() {
        if (params == null) {
            params = new WindowManager.LayoutParams();
            initLayoutParams();
        }
        return params;
    }

    private void initLayoutParams() {
        screenSize = ScreenUtils.getDisplayWH(mContext);
        if (screenSize[0] > screenSize[1]) {
            int temp = screenSize[0];
            screenSize[0] = screenSize[1];
            screenSize[1] = temp;
        }

        params = new WindowManager.LayoutParams();
        params.flags = getLayoutParams().flags
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;

        params.dimAmount = 0.2f;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // SystemUiFlags.LayoutFullscreen
                | View.SYSTEM_UI_FLAG_FULLSCREEN;  //SystemUiFlags.Fullscreen
        params.gravity = Gravity.TOP | Gravity.LEFT | Gravity.START;

        //// TODO: 2017/3/7  测试  完全windowManager 的 情况
        if (false) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = Dimen.VIDEO_DEFAULT_HEIGHT;

            params.x = 0;
            params.y = 0;

            bigSize = new VideoSize(ScreenUtils.getScreenWidth(), Dimen.VIDEO_DEFAULT_HEIGHT, 0, 0);
            smallSize = new VideoSize(ScreenUtils.dip2px(160), ScreenUtils.dip2px(90), screenSize[0] / 2, screenSize[1] - ScreenUtils.dip2px(90) - ScreenUtils.dip2px(100));

        } else {
            params.width = ScreenUtils.dip2px(160);
            params.height = ScreenUtils.dip2px(90);
            params.x = screenSize[0] / 2;
            params.y = screenSize[1] - params.height - ScreenUtils.dip2px(100);

        }


        params.format = PixelFormat.RGBA_8888;
        params.alpha = 1.0f;  //  设置整个窗口的透明度

    }


    public void dismiss() {
        backgroundView.setVisibility(View.GONE);
    }

    public void destroy() {
        if (backgroundView.getParent() != null)
            mWindowManager.removeView(backgroundView);
    }


    public ViewGroup getVideoHolder() {
        return fl;
    }


    private WscnMediaView  videoView;
    private ViewGroup      fl;
    private BackgroundView backgroundView;
    private IconView       btnClose;
//    public static final String url = "http://baobab.wdjcdn.com/14564977406580.mp4";


    private void initBackgroundView() {
        backgroundView = new BackgroundView(mContext);
        backgroundView.setBackgroundColor(Color.BLACK);

        RelativeLayout.LayoutParams rlp;
        //// TODO: 2017/3/8    测试 动画
        rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Dimen.VIDEO_DEFAULT_HEIGHT);


        fl = new FrameLayout(mContext);
//        fl.setBackgroundColor(Color.YELLOW);
        backgroundView.addView(fl, rlp);

        btnClose = new IconView(mContext);
        btnClose.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ScreenUtils.dip2px(25), ScreenUtils.dip2px(25));
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP | RelativeLayout.ALIGN_PARENT_RIGHT);
        int margin = ScreenUtils.dip2px(5);
        lp.setMargins(margin, margin, margin, margin);
        btnClose.setText(R.string.icon_main_right_cancle);
        btnClose.setTextSize(15);
        btnClose.setGravity(Gravity.CENTER);
        btnClose.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClickClose();
            }
        });
        backgroundView.addView(btnClose, lp);
        backgroundView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClickVideo();
            }
        });
    }


    private int[] screenSize;
    private float wLeft, wTop;
    float offsetHorizontal;
    float offsetVertical;

    private boolean moved = false;

    @Override
    public int state() {
        return state;
    }

    @Override
    public void fullScreen(AnimationListener listener) {

        if (state == VideoViewState.STATE_FULLSCREEN) return;
        state = VideoViewState.STATE_FULLSCREEN;
        backgroundView.fullScreen();
        btnClose.setVisibility(View.GONE);
    }

    @Override
    public void fly(AnimationListener listener) {
        if (state == VideoViewState.STATE_FLOAT) return;
        state = VideoViewState.STATE_FLOAT;
        backgroundView.setAnimationListener(listener);
        backgroundView.doAnimate(false);
        btnClose.setVisibility(View.VISIBLE);
    }


    @Override
    public void land(AnimationListener listener) {
        if (btnClose != null) btnClose.setVisibility(View.GONE);

        if (state == VideoViewState.STATE_TOP) return;
        state = VideoViewState.STATE_TOP;
        backgroundView.setAnimationListener(listener);
        backgroundView.doAnimate(true);
        btnClose.setVisibility(View.GONE);

    }

    @Override
    public void join(WscnMediaView videoView) {
        this.videoView = videoView;
        backgroundView.setVisibility(View.VISIBLE);
        btnClose.setVisibility(View.VISIBLE);
        btnClose.bringToFront();
    }

    @Override
    public void leave() {
        dismiss();
    }

    @Override
    public void show(boolean show) {
        if (!show) dismiss();
        else backgroundView.setVisibility(View.VISIBLE);
    }

    @Override
    public ViewGroup videoHolder() {
        return getVideoHolder();
    }

    @Override
    public void over() {
        destroy();

    }


    /**
     * 带有按键监听事件和触摸事件的BackgroundView
     */
    class BackgroundView extends RelativeLayout {


        public BackgroundView(Context context) {
            super(context);
        }

        int mInitialMotionX, mInitialMotionY;
        boolean canMove;


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getAction();
            float x = event.getRawX();
            float y = event.getRawY();
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    mInitialMotionX = (int) x;
                    mInitialMotionY = (int) y;
                    offsetHorizontal = (int) (screenSize[0] * 0.05);
                    offsetVertical = (int) (screenSize[1] * 0.05);

                    wLeft = params.x;
                    wTop = params.y;
                    moved = false;
                    canMove = false;


                    break;
                }

                case MotionEvent.ACTION_MOVE:
                    float dx = x - mInitialMotionX;
                    float dy = y - mInitialMotionY;

                    canMove = canMove || Math.abs(dy) > 10;

                    if (canMove) {
                        params.x = (int) (wLeft + dx);
                        params.y = (int) (wTop + dy);
                        mWindowManager.updateViewLayout(backgroundView, params);
                        moved = true;
                    }


                    break;
                case MotionEvent.ACTION_UP:
                    canMove = false;
                    if (!moved) {
                        performClick();
                        return false;
                    }

                    break;
            }
            return false;
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
        }


        private void doAnimate(boolean bigger) {
            float start = bigger ? 0 : 1;
            float end = bigger ? 1 : 0;

            int DURATION = 300;
            final ValueAnimator an = ValueAnimator.ofFloat(start, end);
            an.setInterpolator(new LinearInterpolator());
            an.setDuration(DURATION);
            smallSize();
            an.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float scale = (float) animation.getAnimatedValue();
                    scale(scale);
                }
            });
            an.start();
            RxUtils.delayDo(DURATION + 5, TimeUnit.MILLISECONDS, aLong -> {

                // if (bigger) bigSize();

                stableLayout();
                stableHolderLayout();

                if (al != null) al.finish();

            });


        }

        private VideoSize bigSize, smallSize;

        public void setSize(VideoSize bigSize, VideoSize smallSize) {
            this.bigSize = bigSize;
            this.smallSize = smallSize;
        }

        private void scale(float scale) {
            params.x = (int) (scale * (bigSize.x - smallSize.x) + smallSize.x);
            params.y = (int) (scale * (bigSize.y - smallSize.y) + smallSize.y);

            TLog.d(VideoWidget.class.getSimpleName(), params.height + " _ " + params.width + " _ " + params.x + " _ " + params.y + " _ ");

            mWindowManager.updateViewLayout(backgroundView, params);
        }

        private void bigSize() {
            params.height = bigSize.height;
            params.width = bigSize.width;
            mWindowManager.updateViewLayout(backgroundView, params);
        }

        private void smallSize() {
            params.height = smallSize.height;
            params.width = smallSize.width;
            mWindowManager.updateViewLayout(backgroundView, params);
        }

        private void fullScreen() {
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mWindowManager.updateViewLayout(backgroundView, params);

            stableHolderLayout();
            if (al != null) al.finish();

        }

        private void stableHolderLayout() {
            ViewGroup.LayoutParams lp = fl.getLayoutParams();
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            fl.setLayoutParams(lp);
        }

        private void stableLayout() {
            switch (state()) {
                case VideoViewState.STATE_FLOAT:
                    params.x = smallSize.x;
                    params.y = smallSize.y;

                    smallSize();
                    break;
                case VideoViewState.STATE_FULLSCREEN:
                    params.x = bigSize.x;
                    params.y = bigSize.y;
                    fullScreen();
                    break;
                case VideoViewState.STATE_TOP:
                    params.x = bigSize.x;
                    params.y = bigSize.y;
                    bigSize();
                    break;
            }

        }


        IFloatContainer.AnimationListener al;

        public void setAnimationListener(IFloatContainer.AnimationListener al) {
            this.al = al;
        }
    }

    private onFloatVideoClickListener listener;

    public void setFloatClickListener(onFloatVideoClickListener listener) {
        this.listener = listener;
    }

    public interface onFloatVideoClickListener {
        void onClickClose();

        void onClickVideo();
    }

    private static class VideoSize {
        int width, height, x, y;

        public VideoSize(int width, int height, int x, int y) {
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;
        }
    }


}
