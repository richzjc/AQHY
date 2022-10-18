package com.micker.global.manager;

import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.micker.global.R;

/**
 * Created by Leif Zhang on 2016/12/21.
 * Email leifzhanggithub@gmail.com
 */

public class MsgCountDownTimer extends android.os.CountDownTimer {

    private TextView view;

    public MsgCountDownTimer(TextView view, long millisInFuture) {
        super(millisInFuture, 1000);
        this.view = view;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        try {
            String text = "重新获取" + millisUntilFinished / 1000;
            view.setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish() {
        try {
            view.setText("获取验证码");
            view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.color_666666));
            view.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
