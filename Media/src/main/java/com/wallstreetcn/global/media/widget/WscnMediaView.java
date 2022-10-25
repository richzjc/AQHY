package com.wallstreetcn.global.media.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;

import androidx.annotation.NonNull;

import com.wallstreetcn.global.media.MediaFromEnum;
import com.wallstreetcn.global.media.R;
import com.wallstreetcn.global.media.WscnIjkExoMediaPlayer;
import com.wallstreetcn.global.media.WscnMediaEntity;
import com.wallstreetcn.global.media.WscnMediaManager;
import com.wallstreetcn.global.media.WscnMediaViewControl;
import com.wallstreetcn.global.media.controller.BaseMediaController;
import com.wallstreetcn.global.media.utils.SharedMediaUtils;
import com.wallstreetcn.global.media.utils.WscnMediaUtils;
import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.helper.utils.Util;
import com.wallstreetcn.helper.utils.data.TraceUtils;
import com.wallstreetcn.helper.utils.image.ImageUtlFormatHelper;
import com.wallstreetcn.helper.utils.system.ScreenUtils;
import com.wallstreetcn.imageloader.ImageLoadManager;
import com.wallstreetcn.imageloader.WscnImageView;

import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.widget.media.IMediaController;
import tv.danmaku.ijk.media.widget.media.IRenderView;
import tv.danmaku.ijk.media.widget.media.TextureRenderView;

public class WscnMediaView extends FrameLayout implements MediaController.MediaPlayerControl, WscnMediaViewControl {
    private static String TAG = "WscnMediaView";
    private String mediaFrom = "";
    private String mediaFromId = "";
    private AttachDetachCallback detachCallback;

    public WscnMediaView(Context context) {
        super(context);
        init(context, null);
    }

    public WscnMediaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WscnMediaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private IRenderView.ISurfaceHolder mSurfaceHolder = null;
    protected IMediaPlayer mMediaPlayer = null;
    private IRenderView mRenderView;

    public IMediaPlayer getIMediaPlayer() {
        return mMediaPlayer;
    }

    private static final int[] s_allAspectRatio = {
            IRenderView.AR_ASPECT_FIT_PARENT,
            IRenderView.AR_ASPECT_FILL_PARENT,
            IRenderView.AR_ASPECT_WRAP_CONTENT,
            IRenderView.AR_MATCH_PARENT,
            IRenderView.AR_16_9_FIT_PARENT,
            IRenderView.AR_4_3_FIT_PARENT};

    private int mVideoRotationDegree;
    private int mCurrentAspectRatio = s_allAspectRatio[1];
    private int mSeekWhenPrepared = 0;

    private boolean mCanPause = true;
    private boolean mCanSeekBack = true;
    private boolean mCanSeekForward = true;
    private boolean mHasVolume = true;
    private WscnImageView imageView;
    private View loadingBar;

    private String firstFrameUrl;

    private void init(Context context, AttributeSet attrs) {
        if (getBackground() == null)
            setBackgroundColor(Color.parseColor("#000000"));
    }

    public IMediaPlayer createPlayer() {
        return WscnMediaManager.instance().getIjkMediaPlayer();
    }

    private synchronized void createRender() {
        if (mRenderView != null)
            return;
        TextureRenderView renderView = new TextureRenderView(getContext());
        setRenderView(renderView);
    }

    public WscnImageView getVideoImageView() {
        return imageView;
    }

    public void setDetachCallback(AttachDetachCallback detachCallback) {
        this.detachCallback = detachCallback;
    }

    public View getmRenderView() {
        if (mRenderView == null)
            return null;
        else
            return mRenderView.getView();
    }

    private int mVideoWidth;
    private int mVideoHeight;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private int mVideoSarNum;
    private int mVideoSarDen;

    private synchronized void setRenderView(IRenderView renderView) {
        if (mRenderView != null)
            return;
        mRenderView = renderView;
        renderView.setAspectRatio(mCurrentAspectRatio);
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            renderView.setVideoSize(mVideoWidth, mVideoHeight);
        }
        if (mVideoSarNum > 0 && mVideoSarDen > 0) {
            renderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
        }
        View renderUIView = mRenderView.getView();

        LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        lp.width = lp.height * 16 / 9;
        renderUIView.setLayoutParams(lp);
        addView(renderUIView);
//        addNetTipView();
        mRenderView.addRenderCallback(mSHCallback);
        mRenderView.setVideoRotation(mVideoRotationDegree);
        if (loadingBar == null) {
            loadingBar = LayoutInflater.from(getContext()).inflate(R.layout.base_fragment_dialog_loading, null);
            LayoutParams loadingLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            loadingBar.setLayoutParams(loadingLp);
            loadingBar.setVisibility(GONE);
            addView(loadingBar);
        }
    }

    private void addNetTipView() {
        if (mNetView != null) {
            mNetView.onDestroy();
            removeView(mNetView);
        }
        mNetView = new WscnMediaNetView(getContext());
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        mNetView.setLayoutParams(params);
        mNetView.setVisibility(GONE);
        addView(mNetView);
        if (mMediaEntity != null)
            mNetView.setUrl(mMediaEntity.getUrl());
    }

    private WscnMediaNetView mNetView;
    private WscnMediaEntity mMediaEntity;
    private boolean prepared = false;
    private boolean needWifiTips = true;

    public void setMediaEntity(WscnMediaEntity entity) {
        setMediaEntity(entity, null, "");
    }

    //添加了两个参数用于统计
    public void setMediaEntity(WscnMediaEntity entity, MediaFromEnum fromType, String fromId) {
        this.mMediaEntity = entity;
        this.mMediaEntity.initSize();

        if (fromType != null)
            mediaFrom = fromType.value();
        else
            mediaFrom = "";

        mediaFromId = fromId;

        if (TextUtils.isEmpty(firstFrameUrl))
            showFirstFrame();

        if (mMediaController != null && mMediaController instanceof BaseMediaController) {
            ((BaseMediaController) mMediaController).mediaEntity = entity;
        }
    }

    /**
     * 先于 {@link #setMediaEntity(WscnMediaEntity)} 设置
     *
     * @param firstFrameUrl
     */
    public void setFirstFrameUrl(String firstFrameUrl) {
        this.firstFrameUrl = firstFrameUrl;
        showFirstFrame();
    }

    public WscnMediaEntity getMediaEntity() {
        return mMediaEntity;
    }

    public void setNeedWifiTips(boolean tips) {
        this.needWifiTips = tips;
    }

    private void startLogic() {
        addNetTipView();
        WscnMediaManager.instance().pause();
        mNetView.setVisibility(VISIBLE);
        mNetView.setPlayOnClickListener(v -> {
            needWifiTips = false;
            mNetView.setVisibility(GONE);
            start();
        });

    }

    @Override
    public void start() {
        if (mMediaController instanceof BaseMediaController && ((BaseMediaController) mMediaController).controllerIsShow()) {
            pause();
            return;
        }

        if (mMediaEntity == null)
            return;

        createRender();
        if (!WscnMediaUtils.offline(mMediaEntity.getUrl())) {
            boolean notWifi = !Util.isConnectWIFI();
            if (needWifiTips && notWifi && !SharedMediaUtils.isAllowNoWifiPlay()) {
                startLogic();
                return;
            }
            if (notWifi) {
//                String sizeText = mMediaEntity.size > 0 ? NetFileUtils.getSize(mMediaEntity.size) : "";
//                MToastHelper.showToast(ResourceUtils.getResStringFromId(R.string.live_room_mobile_tips) + sizeText);
            }
        }
        if (mNetView != null) {
            mNetView.setVisibility(GONE);
        }

        int hash = mMediaPlayer == null ? 0 : mMediaPlayer.hashCode();
        WscnMediaManager.instance().play(mMediaEntity, this);
        IMediaPlayer iMediaPlayer = createPlayer();
        if (iMediaPlayer == null)
            return;
        if (hash == iMediaPlayer.hashCode()) {//是否reset player
            iMediaPlayer.start();
//            mRenderView.getView().setVisibility(VISIBLE);
        } else {
            setMediaPlayer(iMediaPlayer);
        }
        if (isInPlaybackState()) {
            mCurrentState = STATE_PLAYING;
        }
        mTargetState = STATE_PLAYING;
    }


    public void showFirstFrame() {
        setImageViewBitmap();
        if (!TextUtils.isEmpty(firstFrameUrl)) {
            String url = ImageUtlFormatHelper.formatImageWithThumbnail(firstFrameUrl, ScreenUtils.getScreenWidth(), 0);
            ImageLoadManager.loadRoundImage(url, imageView, 0, ScreenUtils.dip2px(5f));
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    private String getMediaPlayUrl() {
        if (mMediaEntity == null)
            return "";
        else
            return mMediaEntity.getUrl();
    }

    private void setImageViewBitmap() {
        if (imageView == null) {
            imageView = new WscnImageView(getContext());
            LayoutParams lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(lp);
            imageView.setVisibility(View.GONE);
            imageView.getHierarchy().setOverlayImage(new ColorDrawable(Color.parseColor("#4d000000")));
            addView(imageView, 0);
        }
        imageView.setVisibility(View.GONE);
    }

    private void onPrepare() {
        try {
            mRenderView.getView().setVisibility(VISIBLE);
            if(imageView != null)
                imageView.setVisibility(View.GONE);
            if (mMediaPlayer != null)
                mMediaPlayer.seekTo(WscnMediaUtils.getVideoPlayPosition(mMediaEntity));
            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        if (isInPlaybackState()) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mCurrentState = STATE_PAUSED;
                WscnMediaUtils.setVideoPlayPosition(mMediaEntity, mMediaPlayer.getCurrentPosition());
            }
        }
        showLoading(false);
        mTargetState = STATE_PAUSED;
    }

    public void suspend() {
        release(false);
    }

    private void resume() {
        openVideo();
    }

    @Override
    public int getDuration() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getDuration();
        }

        return -1;
    }

    @Override
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            mMediaPlayer.seekTo(msec);
            mSeekWhenPrepared = 0;
        } else {
            mSeekWhenPrepared = msec;
        }
    }

    @Override
    public boolean isPlaying() {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        if (mMediaPlayer != null) {
            return mCurrentBufferPercentage;
        }
        return 0;
    }


    @Override
    public boolean canPause() {
        return mCanPause;
    }

    @Override
    public boolean canSeekBackward() {
        return mCanSeekBack;
    }

    @Override
    public boolean canSeekForward() {
        return mCanSeekForward;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private Uri mUri;
    private Map<String, String> mHeaders;
    private IMediaController mMediaController;

    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;
    private int mCurrentBufferPercentage;

    private WscnMediaViewControl mOnDropPlayerListener;
    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private IMediaPlayer.OnBufferingUpdateListener bufferingUpdateListener;

    @Override
    public void onDropPlayer() {
        if (mOnDropPlayerListener != null) {
            mOnDropPlayerListener.onDropPlayer();
        }
        if (mRenderView != null && mRenderView.getView().getParent() != null) {
//            removeView(mRenderView.getView());
//            mRenderView.removeRenderCallback(mSHCallback);
//            mRenderView = null;
            mRenderView.getView().setVisibility(GONE);
        }
        if (imageView != null) {
            imageView.setVisibility(VISIBLE);
        }
    }

    private void openVideo() {
        if (mUri == null || mSurfaceHolder == null) {
            return;
        }
        release(false);
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = createPlayer();
                attachMediaController();
            }
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mCurrentBufferPercentage = 0;
            mMediaPlayer.setDataSource(getContext(), mUri, mHeaders);
            bindSurfaceHolder(mMediaPlayer, mSurfaceHolder);
            if (mHasVolume) {
                AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                am.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int focusChange) {

                    }
                }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
        } catch (Exception ex) {
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }
    }

    public void onComplete() {
        mCompletionListener.onCompletion(mMediaPlayer);
    }

    public void setMediaController(IMediaController controller) {
        if (mMediaController != null) {
            mMediaController.hide();
        }
        mMediaController = controller;
        if (mMediaController != null && mMediaController instanceof BaseMediaController) {
            ((BaseMediaController) mMediaController).mediaView = this;
        }
        attachMediaController();
    }

    private void attachMediaController() {
        if (mMediaPlayer != null && mMediaController != null) {
            ViewGroup anchorView = this;
            mMediaController.setAnchorView(anchorView);
            mMediaController.setMediaPlayer(this);
            mMediaController.setEnabled(isInPlaybackState());
        }
    }

    private boolean isInPlaybackState() {
        return (mMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }

    public void release(boolean cleartargetstate) {
        if (mMediaPlayer != null) {
            pause();
            // REMOVED: mPendingSubtitleTracks.clear();
            mCurrentState = STATE_IDLE;
            if (cleartargetstate) {
                mTargetState = STATE_IDLE;
            }
            WscnMediaManager.instance().requestAudio(false);
        }
    }

    public void releaseWithoutStop() {
        if (mMediaPlayer != null)
            mMediaPlayer.setDisplay(null);
    }


    IRenderView.IRenderCallback mSHCallback = new IRenderView.IRenderCallback() {
        @Override
        public void onSurfaceChanged(@NonNull IRenderView.ISurfaceHolder holder, int format, int w, int h) {
            if (holder.getRenderView() != mRenderView) {
                //  TLog.i(TAG, "onSurfaceChanged: unmatched render callback\n");
                return;
            }
            mSurfaceWidth = w;
            mSurfaceHeight = h;
            //boolean isValidState = (mTargetState == STATE_PLAYING);
            boolean hasValidSize = !mRenderView.shouldWaitForResize() || (mVideoWidth == w && mVideoHeight == h);
            if (mMediaPlayer != null && hasValidSize) {
                if (mSeekWhenPrepared != 0) {
                    seekTo(mSeekWhenPrepared);
                }
            }
        }

        @Override
        public void onSurfaceCreated(@NonNull IRenderView.ISurfaceHolder holder, int width, int height) {
            if (holder.getRenderView() != mRenderView) {
                //  TLog.e(TAG, "onSurfaceCreated: unmatched render callback\n");
                return;
            }
            mSurfaceHolder = holder;
            if (mMediaPlayer != null) {
                bindSurfaceHolder(mMediaPlayer, holder);
            } else {
                openVideo();
            }
        }

        @Override
        public void onSurfaceDestroyed(@NonNull IRenderView.ISurfaceHolder holder) {
            if (holder.getRenderView() != mRenderView) {
                // TLog.e(TAG, "onSurfaceDestroyed: unmatched render callback\n");
                return;
            }

            mSurfaceHolder = null;
            releaseWithoutStop();
        }
    };

    private void bindSurfaceHolder(IMediaPlayer mp, IRenderView.ISurfaceHolder holder) {
        if (mp == null) {
            return;
        }
        if (holder == null) {
            mp.setDisplay(null);
            return;
        }

        holder.bindToMediaPlayer(mp);
    }

    public void stopPlayback() {
        if (mMediaPlayer != null) {
            pause();
            WscnMediaManager.instance().destroy();
            mMediaPlayer = null;
            mCurrentState = STATE_IDLE;
            mTargetState = STATE_IDLE;
            stopAudio();
        }
    }

    public void showLoading(boolean show) {
        if (loadingBar != null)
            loadingBar.setVisibility(show ? VISIBLE : GONE);
    }

    public void stopAudio() {
        WscnMediaManager.instance().requestAudio(false);
    }


    IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener =
            new IMediaPlayer.OnVideoSizeChangedListener() {
                public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                    mVideoWidth = mp.getVideoWidth();
                    mVideoHeight = mp.getVideoHeight();
                    mVideoSarNum = mp.getVideoSarNum();
                    mVideoSarDen = mp.getVideoSarDen();
                    if (mVideoWidth != 0 && mVideoHeight != 0) {
                        if (mRenderView != null) {
                            mRenderView.setVideoSize(mVideoWidth, mVideoHeight);
                            mRenderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
                        }
                        // REMOVED: getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                        requestLayout();
                    }
                }
            };

    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        public void onPrepared(IMediaPlayer mp) {
            mCurrentState = STATE_PREPARED;
            if (mMediaController != null) {
                mMediaController.setEnabled(true);
            }
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();

            int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
            if (seekToPosition != 0) {
                seekTo(seekToPosition);
            }
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                //TLog.i("@@@@", "video size: " + mVideoWidth +"/"+ mVideoHeight);
                // REMOVED: getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                if (mRenderView != null) {
                    mRenderView.setVideoSize(mVideoWidth, mVideoHeight);
                    mRenderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
                    if (!mRenderView.shouldWaitForResize() || mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) {
                        // We didn't actually change the size (it was already at the size
                        // we need), so we won't get a "surface changed" callback, so
                        // start the video here instead of in the callback.
                        if (mTargetState == STATE_PLAYING) {
                            // start();
                            if (mMediaController != null) {
                                mMediaController.show();
                            }
                        } else if (!isPlaying() &&
                                (seekToPosition != 0 || getCurrentPosition() > 0)) {
                            if (mMediaController != null) {
                                // Show the media controls when we're paused into a video and make 'em stick.
                                mMediaController.show(0);
                            }
                        }
                    }
                }
            } else {
                // We don't know the video size yet, but should start anyway.
                // The video size might be reported to us later.
                if (mTargetState == STATE_PLAYING) {
                    start();
                }
            }
            onPrepare();
        }
    };

    private IMediaPlayer.OnCompletionListener mCompletionListener =
            new IMediaPlayer.OnCompletionListener() {
                public void onCompletion(IMediaPlayer mp) {
                    mCurrentState = STATE_PLAYBACK_COMPLETED;
                    mTargetState = STATE_PLAYBACK_COMPLETED;
                    if (mMediaController != null) {
                        mMediaController.hide();
                    }
                    if (mOnCompletionListener != null) {
                        mOnCompletionListener.onCompletion(mMediaPlayer);
                    }
                    WscnMediaUtils.setVideoPlayPosition(mMediaEntity, 0);
                    stopPlayback();
                }
            };

    private IMediaPlayer.OnInfoListener mInfoListener =
            new IMediaPlayer.OnInfoListener() {
                public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                    if (mOnInfoListener != null) {
                        mOnInfoListener.onInfo(mp, arg1, arg2);
                    }
                    TLog.d(TAG + "ddddddd", "arg1:" + arg1 + " arg2:" + arg2);
                    switch (arg1) {
                        case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                            TLog.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                            TLog.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                            TLog.d(TAG, "MEDIA_INFO_BUFFERING_START:");
                            showLoading(true);
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                            TLog.d(TAG, "MEDIA_INFO_BUFFERING_END:");
                            showLoading(false);
                            break;
                        case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                            TLog.d(TAG, "MEDIA_INFO_NETWORK_BANDWIDTH: " + arg2);
                            break;
                        case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                            TLog.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                            TLog.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                            TLog.d(TAG, "MEDIA_INFO_METADATA_UPDATE:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                            TLog.d(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                            TLog.d(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                            mVideoRotationDegree = arg2;
                            TLog.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                            if (mRenderView != null)
                                mRenderView.setVideoRotation(arg2);
                            break;
                        case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                            TLog.d(TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                            break;
                    }
                    return true;
                }
            };

    private IMediaPlayer.OnErrorListener mErrorListener =
            new IMediaPlayer.OnErrorListener() {
                public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                    if (checkUrlError()) {
                        return true;
                    }

                    String error = "Error: " + framework_err + "," + impl_err
                            + ",url:" + mMediaEntity != null ? mMediaEntity.getUrl() : "null"
                            + "dataSource:" + mMediaPlayer != null ? mMediaPlayer.getDataSource() : "null";
                    TLog.e(TAG, error);
                    TraceUtils.reportError(getContext(), new Throwable(error));
                    mCurrentState = STATE_ERROR;
                    mTargetState = STATE_ERROR;
                    if (mMediaController != null) {
                        mMediaController.hide();
                    }

                    /* If an error handler has been supplied, use it and finish. */
                    if (mOnErrorListener != null) {
                        if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
                            return true;
                        }
                    }
                    return true;
                }
            };

    private boolean checkUrlError() {
        boolean flag = false;
        if (mMediaController != null && mMediaController instanceof BaseMediaController) {
            flag = ((BaseMediaController) mMediaController).checkUrlError();
        }
        return flag;
    }

    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new IMediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                    mCurrentBufferPercentage = percent;
                    if (bufferingUpdateListener != null) {
                        bufferingUpdateListener.onBufferingUpdate(mp, percent);
                    }
                }
            };


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isInPlaybackState() && mMediaController != null) {
            toggleMediaControlsVisibility();
        }
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        if (isInPlaybackState() && mMediaController != null) {
            toggleMediaControlsVisibility();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isKeyCodeSupported = keyCode != KeyEvent.KEYCODE_BACK &&
                keyCode != KeyEvent.KEYCODE_VOLUME_UP &&
                keyCode != KeyEvent.KEYCODE_VOLUME_DOWN &&
                keyCode != KeyEvent.KEYCODE_VOLUME_MUTE &&
                keyCode != KeyEvent.KEYCODE_MENU &&
                keyCode != KeyEvent.KEYCODE_CALL &&
                keyCode != KeyEvent.KEYCODE_ENDCALL;
        if (isInPlaybackState() && isKeyCodeSupported && mMediaController != null) {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK ||
                    keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                if (mMediaPlayer.isPlaying()) {
                    pause();
                    mMediaController.show();
                } else {
                    start();
                    mMediaController.hide();
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                if (!mMediaPlayer.isPlaying()) {
                    start();
                    mMediaController.hide();
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                    || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                if (mMediaPlayer.isPlaying()) {
                    pause();
                    mMediaController.show();
                }
                return true;
            } else {
                toggleMediaControlsVisibility();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisibility() {
        if (mMediaController.isShowing()) {
            mMediaController.hide();
        } else {
            mMediaController.show();
        }
    }

    /**
     * Sets video path.
     *
     * @param path the path of the video.
     */
    private void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    /**
     * Sets video URI.
     *
     * @param uri the URI of the video.
     */
    private void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    /**
     * Sets video URI using specific headers.
     *
     * @param uri     the URI of the video.
     * @param headers the headers for the URI request.
     *                Note that the cross domain redirection is allowed by default, but that can be
     *                changed with key/value pairs through the headers parameter with
     *                "android-allow-cross-domain-redirect" as the key and "0" or "1" as the value
     *                to disallow or allow cross domain redirection.
     */
    private void setVideoURI(Uri uri, Map<String, String> headers) {
        mUri = uri;
        mHeaders = headers;
        mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void changeAspectRaito(int pos) {
        if (mRenderView != null) {
            mRenderView.setAspectRatio(s_allAspectRatio[pos]);
        }
    }

    /**
     * 回调用于页面处理， 不要操作播放器
     *
     * @param control
     */
    public void setOnDropListener(WscnMediaViewControl control) {
        this.mOnDropPlayerListener = control;
    }

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param l The callback that will be run
     */
    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        mOnPreparedListener = l;
    }

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l The callback that will be run
     */
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        mOnCompletionListener = l;
    }

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l The callback that will be run
     */
    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        mOnErrorListener = l;
    }

    /**
     * Register a callback to be invoked when an informational event
     * occurs during playback or setup.
     *
     * @param l The callback that will be run
     */
    public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
        mOnInfoListener = l;
    }

    public void setBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener bufferingUpdateListener) {
        this.bufferingUpdateListener = bufferingUpdateListener;
    }

    public IMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void setVolume(float volume) {
        if (mMediaPlayer != null)
            mMediaPlayer.setVolume(volume, volume);

        if (volume > 0)
            mMediaEntity.mSilent = false;
    }

    public void requestAudio(boolean request) {
        WscnMediaManager.instance().requestAudio(request);
    }

    private void setMediaPlayer(IMediaPlayer player) {
        this.mMediaPlayer = player;
        mMediaPlayer.setOnPreparedListener(mPreparedListener);
        mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
        mMediaPlayer.setOnCompletionListener(mCompletionListener);
        mMediaPlayer.setOnErrorListener(mErrorListener);
        mMediaPlayer.setOnInfoListener(mInfoListener);
        mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
        mCurrentState = !player.isPlaying() ? STATE_PAUSED : STATE_PLAYING;
        attachMediaController();
        bindSurfaceHolder(mMediaPlayer, mSurfaceHolder);
    }

    public void setHasVolume(boolean hasVolume) {
        mHasVolume = hasVolume;
        if (mHasVolume) {
            AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } else {
            stopAudio();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (detachCallback != null)
            detachCallback.detach();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (detachCallback != null)
            detachCallback.attach();
    }

    public interface AttachDetachCallback {
        void detach();

        void attach();
    }
}
