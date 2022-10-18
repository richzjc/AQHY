package com.micker.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.micker.helper.system.EquipmentUtils;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by Leif Zhang on 16/8/29.
 * Email leifzhanggithub@gmail.com
 */
public class WSCNUmengMessageHandler extends UmengMessageHandler {
    @Override
    public void dealWithCustomMessage(final Context context, final UMessage msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                UTrack.getInstance(context).trackMsgClick(msg);
            }
        });
    }

    @Override
    public void dealWithNotificationMessage(Context context, UMessage uMessage) {
        super.dealWithNotificationMessage(context, uMessage);
    }

    @Override
    public Notification getNotification(Context context, UMessage msg) {
        switch (msg.builder_id) {
            default:
                String channelId = "com.micker.aqhy";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
                Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
                builder.setLargeIcon(icon)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentText(msg.text)
                        .setContentTitle(msg.title)
                        .setTicker(msg.ticker)
                        .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(msg.title).bigText(msg.text))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setColor(ContextCompat.getColor(context, R.color.day_mode_background_color));
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(channelId, EquipmentUtils.getAppName(), NotificationManager.IMPORTANCE_HIGH);
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.createNotificationChannel(notificationChannel);
                }

                return builder.build();
        }
    }
}