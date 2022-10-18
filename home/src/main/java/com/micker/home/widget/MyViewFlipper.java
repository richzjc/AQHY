package com.micker.home.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ViewFlipper;

public class MyViewFlipper extends ViewFlipper implements View.OnTouchListener {

    private int listSize = 0;
    private int mCurrentItem = 0;    //初始化在第一个位置
    private float originalX;//ACTION_DOWN事件发生时的手指坐标
    private float originalY;
    private int flipper_width = 0;
    public static final int SCROLL_STATE_IDLE = 0;//空闲状态
    public static final int SCROLL_STATE_DRAGGING = 1;//滑动状态
    public static final int SCROLL_STATE_SETTLING = 2;//滑动后自然沉降的状态
    private OnPageChangeListener mOnPageChangeListener;

    public static final int SCROLL_HORIZONTAL = 1;
    public static final int SCROLL_VERTICLE = 2;
    public static final int SCROLL_DEFAULT = 0;
    private int scrollFlag;
    private ViewConfiguration configuration;

    private Handler mAutoScrollHandler = new Handler();
    private int mAutoScrollTime = 3500;

    public MyViewFlipper(Context context) {
        this(context, null);
    }

    public MyViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        configuration = ViewConfiguration.get(context);
        setOnTouchListener(this);    //对viewFlipper设定监听事件
        setLongClickable(true);//设置可以接受事件
    }

    public void setListSize(int size) {
        this.listSize = size;
        resetAutoScroll();
    }

    public void setmCurrentItem(int mCurrentItem) {
        this.mCurrentItem = mCurrentItem;
    }

    //获取上一个下标
    private int nextItem(int index) {
        mCurrentItem = index;
        if (mCurrentItem >= listSize) {
            mCurrentItem = 0;
        } else if (mCurrentItem < 0) {
            mCurrentItem = listSize - 1;
        }
        return mCurrentItem;
    }

    //获取当前屏幕中显示的Poster
    public <T extends View> T getCurrentPosterView() {
        return (T) getCurrentView();
    }

    //获取没在屏幕中显示的Poster
    public <T extends View> T getOtherPosterView() {
        return (T) getChildAt(getChildCount() - 1 - getDisplayedChild());
    }

    //获取下一个下标
    public int previousItem(int index) {
        mCurrentItem = index;
        if (mCurrentItem >= listSize) {
            mCurrentItem = 0;
        } else if (mCurrentItem < 0) {
            mCurrentItem = listSize - 1;
        }
        return mCurrentItem;
    }

    //仅获取下标
    public int getNextItem() {
        int next = mCurrentItem + 1;
        if (next >= listSize) {
            return 0;
        } else if (next < 0) {
            return listSize - 1;
        }
        return next;
    }

    //仅获取下标
    public int getPreviousItem() {
        int previous = mCurrentItem - 1;
        if (previous >= listSize) {
            return 0;
        } else if (previous < 0) {
            return listSize - 1;
        }
        return previous;
    }

    //获取没在屏幕中显示的View的下标
    public int getOtherItem() {
        return getChildCount() - 1 - getDisplayedChild();
    }

    //获取没在屏幕中显示的View
    public View getOtherView() {
        return getChildAt(getChildCount() - 1 - getDisplayedChild());
    }

    public void showPreviousWithAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, getWidth());
        animator.addUpdateListener(animation -> {

            //另一个子View的下标
            getCurrentView().setTranslationX((Float) animation.getAnimatedValue());
            getOtherView().setVisibility(VISIBLE);
            getOtherView().setTranslationX((Float) animation.getAnimatedValue() - getWidth());
            if (Math.abs((float) animation.getAnimatedValue()) == getWidth()) {
                previousItem(mCurrentItem - 1);
                showPrevious();
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(mCurrentItem, false);
                    mOnPageChangeListener.onPageScrollStateChanged(SCROLL_STATE_IDLE);
                }
            }
        });
        animator.start();
    }

    public void showNextWithAnimation() {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(mCurrentItem, 0, 0);
            mOnPageChangeListener.onPageScrollStateChanged(SCROLL_STATE_SETTLING);
        }


        ValueAnimator animator = ValueAnimator.ofFloat(0, -getWidth());
        animator.addUpdateListener(animation -> {
            //另一个子View的下标
            getCurrentView().setTranslationX((Float) animation.getAnimatedValue());
            getOtherView().setVisibility(VISIBLE);
            getOtherView().setTranslationX((Float) animation.getAnimatedValue() + getWidth());
            if (Math.abs((float) animation.getAnimatedValue()) == getWidth()) {
                nextItem(mCurrentItem + 1);
                showNext();
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(mCurrentItem, true);
                    mOnPageChangeListener.onPageScrollStateChanged(SCROLL_STATE_IDLE);
                }
            }
        });
        animator.start();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopAutoScroll();
                originalX = event.getX();
                originalY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (scrollFlag == SCROLL_DEFAULT) {
                    if (Math.abs(event.getX() - originalX) >= Math.abs(event.getY() - originalY)) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                resetAutoScroll();
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private Runnable mSmoothScroll = () -> {
        if (listSize > 0) {
            showNextWithAnimation();
        }
        resetAutoScroll();
    };


    public void stopAutoScroll() {
        mAutoScrollHandler.removeCallbacks(mSmoothScroll);
    }

    public void resetAutoScroll() {
        mAutoScrollHandler.removeCallbacks(mSmoothScroll);
        mAutoScrollHandler.postDelayed(mSmoothScroll, mAutoScrollTime);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float dx = event.getX() - originalX;

        float pageOffset = Math.abs(dx) / flipper_width;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scrollFlag = SCROLL_DEFAULT;
                originalX = event.getX();
                originalY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (scrollFlag == SCROLL_DEFAULT) {
                    if (Math.abs(event.getX() - originalX) >= Math.abs(event.getY() - originalY)) {
                        scrollFlag = SCROLL_HORIZONTAL;
                        if (Math.abs(event.getX() - originalX) > configuration.getScaledTouchSlop()) {
                            responseToMove(dx, pageOffset);
                            return true;
                        }
                    } else {
                        scrollFlag = SCROLL_VERTICLE;
                    }
                } else if (scrollFlag == SCROLL_HORIZONTAL) {
                    responseToMove(dx, pageOffset);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (scrollFlag == SCROLL_HORIZONTAL) {
                    responseActionUp(dx, pageOffset);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void responseToMove(float dx, float pageOffset) {
        if (dx > 0 && (mCurrentItem == 0)) {
            return;
        } else if (dx < 0 && (mCurrentItem == listSize - 1)) {
            return;
        }
        getCurrentView().setTranslationX(dx);
        getOtherView().setVisibility(VISIBLE);

        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(mCurrentItem, pageOffset, dx);
            mOnPageChangeListener.onPageScrollStateChanged(SCROLL_STATE_DRAGGING);
        }

        if (dx > 0) {
            getOtherView().setTranslationX(dx - flipper_width);
        } else {
            getOtherView().setTranslationX(dx + flipper_width);
        }
    }

    private void responseActionUp(float dx, float pageOffset) {
        if (dx > 0 && (mCurrentItem == 0)) {
            return;
        } else if (dx < 0 && (mCurrentItem == listSize - 1)) {
            return;
        }

        final boolean isNext = dx < 0;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(SCROLL_STATE_SETTLING);
        }
        if (pageOffset > 0.15) {
            //切换
            ValueAnimator animator = ValueAnimator.ofFloat(dx, isNext ? -flipper_width : flipper_width);
            animator.addUpdateListener(animation -> {
                //另一个子View的下标
                getCurrentView().setTranslationX((Float) animation.getAnimatedValue());
                getOtherView().setTranslationX((Float) animation.getAnimatedValue() + (isNext ? flipper_width : -flipper_width));
                if (Math.abs((float) animation.getAnimatedValue()) == flipper_width) {
                    Log.e("mCurrentItem", mCurrentItem + "");

                    if (isNext) {
                        nextItem(mCurrentItem + 1);
                        showNext();
                    } else {
                        previousItem(mCurrentItem - 1);
                        showPrevious();
                    }
                    Log.e("mCurrentItem", mCurrentItem + "");
                    if (mOnPageChangeListener != null) {
                        mOnPageChangeListener.onPageSelected(mCurrentItem, isNext);
                        mOnPageChangeListener.onPageScrollStateChanged(SCROLL_STATE_IDLE);
                    }
                }
            });
            animator.start();
        } else {
            //回弹
            ValueAnimator animator = ValueAnimator.ofFloat(dx, 0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    getCurrentView().setTranslationX((Float) animation.getAnimatedValue());
                    getOtherView().setTranslationX((Float) animation.getAnimatedValue() + (isNext ? flipper_width : -flipper_width));
                    if ((float) animation.getAnimatedValue() == 0) {
                        if (mOnPageChangeListener != null) {
                            mOnPageChangeListener.onPageScrollStateChanged(SCROLL_STATE_IDLE);
                        }
                    }
                }
            });
            animator.start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (flipper_width == 0)
            flipper_width = getWidth();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public interface OnPageChangeListener {
        void onPageScrolled(int position, float positionOffset, float positionOffsetPixels);

        void onPageSelected(int position, boolean isNext);

        void onPageScrollStateChanged(int state);
    }

}
