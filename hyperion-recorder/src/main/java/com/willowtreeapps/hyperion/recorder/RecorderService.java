package com.willowtreeapps.hyperion.recorder;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.willowtreeapps.hyperion.plugin.v1.PluginExtension;

import java.lang.ref.WeakReference;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RecorderService extends Service {

    private static final int NOTIFICATION_ID = 0x410;
    private static final int ACTION_REQUEST_CODE_OPEN_MENU = 0x100;

    private BroadcastReceiver actionReceiver;
    private IBinder binder = new Binder(this);

    private Notification createNotification() {
        String actionText = getString(R.string.hr_notification_action_stop_recording);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PluginExtension.NOTIFICATION_CHANNEL_ID)
                .addAction(new NotificationCompat.Action.Builder(null, actionText, createContentPendingIntent()).build())
                .setTicker("")
                .setSmallIcon(R.drawable.hr_icon)
                .setOngoing(true)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .setVibrate(new long[]{0});

        String contentTitle = getString(R.string.hr_notification_content_title);
        String contentText = getString(R.string.hr_notification_content_text);
        String subText = getString(R.string.hr_notification_subtext);

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
                new Intent(NotificationActionReceiver.ACTION_STOP_RECORDING), pendingIntentFlags);
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    public void onCreate() {
        super.onCreate();
        actionReceiver = new NotificationActionReceiver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(actionReceiver, NotificationActionReceiver.createFilter(), Context.RECEIVER_EXPORTED);
        } else {
            registerReceiver(actionReceiver, NotificationActionReceiver.createFilter());
        }
        startForeground(NOTIFICATION_ID, createNotification());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        binder = null;
        unregisterReceiver(actionReceiver);
        actionReceiver = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopSelf();
        return super.onUnbind(intent);
    }

    private final static class NotificationActionReceiver extends BroadcastReceiver {
        static final String ACTION_STOP_RECORDING = "hr-stop-recording";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) return;
            switch (action) {
                case ACTION_STOP_RECORDING:
                    if (RecordingManager.isRecording()) {
                        try {
                            RecordingManager.stop();
                        } catch (RecordingException e) {
                            // nop. user should still see notification if recording is in progress
                        }
                    }
                    break;
            }
        }

        public static IntentFilter createFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_STOP_RECORDING);
            return filter;
        }
    }

    final static class Binder extends android.os.Binder {

        private final WeakReference<RecorderService> serviceRef;

        private Binder(final RecorderService service) {
            this.serviceRef = new WeakReference<>(service);
        }

        @Nullable
        RecorderService getService() {
            return serviceRef.get();
        }
    }
}
