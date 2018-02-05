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
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.willowtreeapps.hyperion.core.R;

import java.lang.ref.WeakReference;

public class HyperionService extends Service {

    private static final int NOTIFICATION_ID = 0x400;
    private static final String CHANNEL_ID = "hyperion-activation-channel";

    private static final int ACTION_REQUEST_CODE_OPEN_MENU = 0x100;
    private static final String ACTION_OPEN_MENU = "open-menu";

    private final IBinder binder = new Binder();
    private final BroadcastReceiver actionOpenMenuReceiver = new OpenMenuReceiver();
    private NotificationManager notificationManager;
    private WeakReference<Activity> activity;

    void attach(Activity activity) {
        this.activity = new WeakReference<>(activity);
        initChannels();
        startForeground(NOTIFICATION_ID, createNotification(activity));
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

    private Notification createNotification(Activity activity) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(createContentPendingIntent())
                .setSubText(getString(R.string.hype_notification_subtext))
                .setTicker("")
                .setSmallIcon(R.drawable.logo)
                .setOngoing(true)
                .setVibrate(new long[] { 0 })
                .build();
    }

    private PendingIntent createContentPendingIntent() {
        return PendingIntent.getBroadcast(this, ACTION_REQUEST_CODE_OPEN_MENU,
                new Intent(ACTION_OPEN_MENU), PendingIntent.FLAG_CANCEL_CURRENT);
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
        registerReceiver(actionOpenMenuReceiver, new IntentFilter(ACTION_OPEN_MENU));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(actionOpenMenuReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    final class OpenMenuReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final Activity activity = HyperionService.this.activity.get();
            if (activity != null) {
                AppComponent.Holder.getInstance().getPublicControl().open(activity);
            }
        }
    }

    final class Binder extends android.os.Binder {
        HyperionService getService() {
            return HyperionService.this;
        }
    }

    static final class Connection implements ServiceConnection {

        private final Activity activity;
        private HyperionService service;

        Connection(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            this.service = ((HyperionService.Binder) service).getService();
            this.service.attach(activity);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            this.service.detach(activity);
            service = null;
        }
    }
}