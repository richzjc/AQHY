package com.wallstreetcn.global.media;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.text.TextUtils;

import com.kronos.download.DownloadManager;
import com.kronos.download.DownloadModel;
import com.wallstreetcn.global.media.utils.SharedMediaUtils;
import com.wallstreetcn.global.media.utils.WscnMediaUtils;
import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.helper.utils.Util;
import com.wallstreetcn.helper.utils.UtilsContextManager;
import com.wallstreetcn.helper.utils.system.TDevice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class WscnMediaManager {

    private static WscnMediaManager instance;

    private WscnMediaEntity mMediaEntity;

    private IMediaPlayer mMediaPlayer;
    private Context context;

    private List<WscnMediaViewControl> mListViewControl;

    public static synchronized WscnMediaManager instance() {
        if (instance == null)
            instance = new WscnMediaManager();
        return instance;
    }

    private WscnMediaManager() {
        context = UtilsContextManager.getInstance().getApplication();
    }

    public void play(WscnMediaEntity entity, WscnMediaViewControl listener) {
        String preUrl = mMediaEntity == null ? "" : mMediaEntity.getUrl();
        String preTag = mMediaEntity == null ? "" : mMediaEntity.getTag();
        String preKey = mMediaEntity == null ? "" : mMediaEntity.getKey();

        if ((!TextUtils.equals(preUrl, entity.getUrl()) && !TextUtils.equals(preKey, entity.getKey()))
                || !TextUtils.equals(entity.getTag(), preTag)) {
            reset();
            dropPlayer();
        } else if (!TextUtils.equals(preUrl, entity.getUrl())) {
            reset();
        }
        if (mListViewControl == null)
            mListViewControl = new ArrayList<>();
        if (!mListViewControl.contains(listener))
            mListViewControl.add(listener);
        startPlay(entity);
    }


    private void startPlay(WscnMediaEntity entity) {
        this.mMediaEntity = entity;

        if (mMediaPlayer == null) {
            try {
                createIjkMediaPlayer();
                Uri uri = getPlayUri(entity);
                if (uri == null)
                    return;
                mMediaPlayer.setDataSource(context, uri);
                mMediaPlayer.setScreenOnWhilePlaying(true);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setScreenOnWhilePlaying(true);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        start();
    }

    private void start() {
        if (mMediaPlayer == null)
            return;
        if (!mMediaEntity.mSilent)
            requestAudio(true);
        mMediaPlayer.start();
    }

    public void requestAudio(boolean request) {
        if (afChangeListener != null) {
            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (request) {
                am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//                int currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
//                mMediaPlayer.setVolume(currentVolume, currentVolume);
            } else {
                am.abandonAudioFocus(afChangeListener);
            }
        }
    }

    private AudioManager.OnAudioFocusChangeListener afChangeListener = focusChange -> {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            pause();
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            pause();
        }
    };

    private void dropPlayer() {
        if (mListViewControl != null && !mListViewControl.isEmpty()) {
            for (WscnMediaViewControl control : mListViewControl) {
                if (control != null) {
                    control.onDropPlayer();
                    control = null;
                }
            }
            mListViewControl.clear();
        }
    }

    private void reset() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            mMediaPlayer = null;
        }
        VideoCacheManager.getProxy().shutDown();
    }

    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void destroy() {
        requestAudio(false);
        reset();
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    public IMediaPlayer getIjkMediaPlayer() {
        return mMediaPlayer;
    }

    public int getProgress() {
        long position = mMediaPlayer.getCurrentPosition();
        long duration = mMediaPlayer.getDuration();
        if (duration > 0) {
            long pos = 100L * position / duration;
            return (int) pos;
        } else {
            return 0;
        }
    }

    private void createIjkMediaPlayer() {
        mMediaPlayer = new WscnIjkExoMediaPlayer(context);
    }

    public static Uri getPlayUri(WscnMediaEntity entity) {
        String url = entity.getUrl();
        if (TextUtils.isEmpty(url))
            return null;
        Uri uri = Uri.parse(url);
        String path = uri.getPath();
        if (!TextUtils.isEmpty(path) && path.endsWith(".m3u8") && WscnMediaUtils.isDownload(url)) {
            DownloadModel model = DownloadManager.INSTANCE.getModel(url);
            if (model != null)
                return Uri.parse(model.getSdCardFile());
        }

        if (path.contains(".flv") || path.contains(".m3u8") || entity.isNocache()) {
            if (path.contains(".m3u8") && !path.endsWith(".m3u8")) {
                return uri.buildUpon().appendQueryParameter("xuffix", ".m3u8").build();
            }
            return uri;
        }
        if (WscnMediaUtils.isDownload(url)) {
            DownloadModel model = DownloadManager.INSTANCE.getModel(url);
            if (model != null)
                return Uri.parse(model.getSdCardFile());
        }
        if (!TextUtils.isEmpty(url)) {
            TLog.i("VideoCache", url);
            String proxy = VideoCacheManager.getProxy().getProxyUrl(url);
            return Uri.parse(proxy);
        }
        return null;
    }

    public boolean isCached() {
        if (mMediaPlayer != null) {
            String url = mMediaPlayer.getDataSource();
            return VideoCacheManager.getProxy().isCached(url);
        }
        return false;
    }

    public void netStateChanged() {
        if (mMediaEntity == null || !isPlaying())
            return;
        if (WscnMediaUtils.offline(mMediaEntity.getUrl()))
            return;
        if (!Util.isConnectWIFI() && !SharedMediaUtils.isAllowNoWifiPlay()) {
            pause();
            if (TDevice.isNetworkConnected()) {
                WscnMediaUtils.openNoWifiTips((dialog, which) -> {
                    if (which == 0) {
                        startPlay(mMediaEntity);
                    }
                });
            }
        }
    }

    public boolean enableSpeed() {
        return mMediaPlayer != null && mMediaPlayer instanceof WscnIjkExoMediaPlayer;
    }

    public boolean setSpeed(float speed) {
        if (enableSpeed()) {
            ((WscnIjkExoMediaPlayer) mMediaPlayer).setSpeed(speed, 1);
            return true;
        }
        return false;
    }
}
