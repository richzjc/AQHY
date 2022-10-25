package com.wallstreetcn.global.media.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wallstreetcn.global.media.R;
import com.wallstreetcn.global.media.utils.SharedMediaUtils;
import com.wallstreetcn.global.media.utils.WscnMediaUtils;

import com.wallstreetcn.global.media.utils.NetFileUtils;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.Util;
import com.wallstreetcn.helper.utils.snack.MToastHelper;
import com.wallstreetcn.helper.utils.system.TDevice;
import com.wallstreetcn.rpc.ResponseListener;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class WscnMediaNetView extends LinearLayout {

    public WscnMediaNetView(Context context) {
        super(context);
        init();
    }

    public WscnMediaNetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WscnMediaNetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TextView tipsText;
    private TextView playBtn;

    private OnClickListener mOnPlayListener;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.global_view_media_net, this);
        if (getBackground() == null) {
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
        }
        tipsText = findViewById(R.id.tipsText);
        playBtn = findViewById(R.id.playBtn);
        playBtn.setOnClickListener(v -> {
            if (!TDevice.isNetworkConnected()) {
                MToastHelper.showToast(ResourceUtils.getResStringFromId(R.string.wall_global_network_unconnected));
            } else if (Util.isConnectWIFI()) {
                if (mOnPlayListener != null)
                    mOnPlayListener.onClick(v);
            } else if (!SharedMediaUtils.isAllowNoWifiPlay()) {
                WscnMediaUtils.openNoWifiTips((dialog, which) -> {
                    if (which == 0) {
                        SharedMediaUtils.setAllowNoWifiPlay(true);
                        if (mOnPlayListener != null)
                            mOnPlayListener.onClick(v);
                    }
                });
            } else {
                if (mOnPlayListener != null)
                    mOnPlayListener.onClick(v);
            }
        });
    }

    public void setUrl(String url) {
        if (TextUtils.equals(url, mVideoUrl))
            return;
        mVideoSize = "";
        setTipsText();
        mVideoUrl = url;
        initAudioLength(mVideoUrl, model -> {
            if (model > 0)
                mVideoSize = "\n视频大小" + NetFileUtils.getSize(model);
            setTipsText();
        });
    }

    public void setPlayOnClickListener(OnClickListener listener) {
        this.mOnPlayListener = listener;
    }

    private void setTipsText() {
        if (tipsText != null) {
            if (!TDevice.isNetworkConnected()) {
                tipsText.setText(ResourceUtils.getResStringFromId(R.string.wall_global_network_unconnected) + mVideoSize);
//                playBtn.setVisibility(INVISIBLE);
            } else {
                tipsText.setText(ResourceUtils.getResStringFromId(R.string.live_room_mobile_tips) + mVideoSize);
                playBtn.setVisibility(VISIBLE);
            }
        }
    }

    public void onDestroy() {

    }

    public String mVideoSize = "";
    private String mVideoUrl;

    public static void initAudioLength(String downloadUrl, SizeCallback callback) {
        NetFileUtils.loadFileLength(downloadUrl, new ResponseListener<Long>() {
            @Override
            public void onSuccess(Long model, boolean isCache) {
                if (callback != null)
                    callback.size(model);
            }

            @Override
            public void onErrorResponse(int code, String error) {

            }
        });
    }

    public interface SizeCallback {
        void size(long size);
    }
}
