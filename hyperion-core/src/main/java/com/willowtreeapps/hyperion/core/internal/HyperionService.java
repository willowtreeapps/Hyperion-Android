package com.willowtreeapps.hyperion.core.internal;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.willowtreeapps.hyperion.core.R;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class HyperionService extends Service {

    private static final int NOTIFICATION_ID = 0x400;
    private static final String CHANNEL_ID = "hyperion-activation-channel";

    private static final int ACTION_REQUEST_CODE_OPEN_MENU = 0x100;
    private static final String ACTION_OPEN_MENU = "open-menu";

    private BroadcastReceiver actionOpenMenuReceiver = new OpenMenuReceiver();
    private IBinder binder = new Binder(this);
    private NotificationManager notificationManager;
    private WeakReference<Activity> activity;

    void attach(Activity activity) {
        this.activity = new WeakReference<>(activity);
        initChannels();
        startForeground(NOTIFICATION_ID, createNotification());
    }

    void detach(Activity activity) {
        if (this.activity != null) {
            final Activity current = this.activity.get();
            if (current == activity) {
                stopForeground(true);
                this.activity = null;
            }
        }
    }

    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(createContentPendingIntent())
                .setTicker("")
                .setSmallIcon(R.drawable.hype_logo)
                .setOngoing(true)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .setVibrate(new long[]{0});

        String contentTitle = getString(R.string.hype_notification_content_title);
        String contentText = getString(R.string.hype_notification_content_text);
        String subText = getString(R.string.hype_notification_subtext);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            builder.setContentTitle(contentTitle);
            builder.setContentText(contentText.isEmpty() ? subText : contentText);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            builder.setContentTitle(contentTitle);
            builder.setContentText(contentText);
            builder.setSubText(subText);
        } else {
            builder.setContentTitle(contentTitle.isEmpty() ? null : contentTitle);
            builder.setContentText(contentText.isEmpty() ? null : contentText);
            builder.setSubText(subText);
        }

        return builder.build();
    }

    private PendingIntent createContentPendingIntent() {
        final int pendingIntentFlags;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntentFlags = PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        } else {
            pendingIntentFlags = PendingIntent.FLAG_CANCEL_CURRENT;
        }
        return PendingIntent.getBroadcast(this, ACTION_REQUEST_CODE_OPEN_MENU,
                new Intent(ACTION_OPEN_MENU), pendingIntentFlags);
    }

    private void initChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    getString(R.string.hype_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getString(R.string.hype_notification_channel_description));
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(false);
            channel.setImportance(NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(actionOpenMenuReceiver, new IntentFilter(ACTION_OPEN_MENU), RECEIVER_EXPORTED);
        } else {
            registerReceiver(actionOpenMenuReceiver, new IntentFilter(ACTION_OPEN_MENU));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binder = null;
        unregisterReceiver(actionOpenMenuReceiver);
        actionOpenMenuReceiver = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    final class OpenMenuReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            WeakReference<Activity> ref = HyperionService.this.activity;
            if (ref != null) {
                final Activity activity = ref.get();
                if (activity != null) {
                    AppComponent.Holder.getInstance(HyperionService.this)
                            .getPublicControl().open(activity);
                }
            }
        }
    }

    private final static class Binder extends android.os.Binder {

        private final WeakReference<HyperionService> serviceRef;

        private Binder(final HyperionService service) {
            this.serviceRef = new WeakReference<>(service);
        }

        @Nullable
        HyperionService getService() {
            return serviceRef.get();
        }
    }

    @ActivityScope
    static final class Connection implements ServiceConnection {

        private final Activity activity;
        @Nullable
        private HyperionService service;

        @Inject
        Connection(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            this.service = ((HyperionService.Binder) service).getService();
            if (this.service != null) {
                this.service.attach(activity);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (this.service != null) {
                this.service.detach(activity);
            }
            service = null;
        }

        void forceDisconnect() {
            // `onServiceDisconnected` doesn't seem to be called as expected, so this method guarantees unbinding.
            if (this.service != null) {
                this.service.detach(activity);
            }
            service = null;
        }
    }
}