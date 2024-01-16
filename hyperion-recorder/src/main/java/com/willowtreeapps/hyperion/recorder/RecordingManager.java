package com.willowtreeapps.hyperion.recorder;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.WindowManager;

import com.willowtreeapps.hyperion.plugin.v1.ActivityResults;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
final class RecordingManager {

    private static final String TAG = "RecordingManager";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final CopyOnWriteArrayList<OnRecordingChangedListener> listeners = new CopyOnWriteArrayList<>();
    private static RecorderServiceConnection connection;
    private static MediaRecorder mediaRecorder;
    private static MediaProjectionManager projectionManager;
    private static MediaProjection mediaProjection;
    private static VirtualDisplay virtualDisplay;
    private static MediaProjectionCallback mediaProjectionCallback;
    private static DisplayMetrics displayMetrics;

    private static boolean recording;
    private static String outputPath;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static void prepare(@NonNull Context context) throws RecordingException {
        mediaRecorder = new MediaRecorder();
        projectionManager = (MediaProjectionManager) context
                .getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        WindowManager windowManager = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        createDirectoryIfNeeded(context);
        String videoId = UUID.randomUUID().toString();
        outputPath = context.getFilesDir().getPath() + "/hyperion_recorder/" + videoId + ".mp4";

        try {
            //mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(outputPath);
            mediaRecorder.setVideoSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setVideoEncodingBitRate(512 * 2000);
            mediaRecorder.setVideoFrameRate(30);
            int rotation = windowManager.getDefaultDisplay().getRotation();
            int orientation = ORIENTATIONS.get(rotation + 90);
            mediaRecorder.setOrientationHint(orientation);
            mediaRecorder.prepare();
        } catch (IOException e) {
            throw new RecordingException("Failed to initialize the media recorder.", e);
        }
    }

    private static void createDirectoryIfNeeded(@NonNull Context context) {
        File file = new File(context.getFilesDir().getPath() + "/hyperion_recorder/");
        if (!file.exists()) {
            boolean created = file.mkdir();
            if (!created) {
                Log.d(TAG, "Failed to create hyperion recorder directory.");
            }
        }
    }

    static void requestStart(ActivityResults activityResults, int requestCode) {
        activityResults.startActivityForResult(projectionManager.createScreenCaptureIntent(), requestCode);
    }

    static void start(Context context, int resultCode, Intent data) {
        if (connection != null) {
            // Another recording session is in progress or trying to start
            return;
        }
        connection = new RecorderServiceConnection(context, resultCode, data);
        context.bindService(
                new Intent(context, RecorderService.class),
                connection,
                Context.BIND_AUTO_CREATE
        );
    }

    private static void onServiceReady(int resultCode, Intent data) {
        mediaProjectionCallback = new MediaProjectionCallback();
        mediaProjection = projectionManager.getMediaProjection(resultCode, data);
        mediaProjection.registerCallback(mediaProjectionCallback, null);
        virtualDisplay = createVirtualDisplay();
        mediaRecorder.start();
        setRecording(true);
    }

    private static VirtualDisplay createVirtualDisplay() {
        return mediaProjection.createVirtualDisplay("HyperionRecorder",
                displayMetrics.widthPixels, displayMetrics.heightPixels, displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.getSurface(), null /* Callbacks */, null /* Handler */);
    }

    static void reset() {
        mediaRecorder.reset();
        if (!new File(outputPath).delete()) {
            Log.e(TAG, "Failed to remove placeholder " + outputPath);
        }
    }

    static void stop() throws RecordingException {
        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            stopScreenSharing();
        } catch (RuntimeException ex) {
            throw new RecordingException("Failed to stop media recorder.", ex);
        } finally {
            stopService();
            setRecording(false);
        }
    }

    private static void stopScreenSharing() {
        if (virtualDisplay == null) {
            return;
        }
        virtualDisplay.release();
        //mMediaRecorder.release(); //If used: mMediaRecorder object cannot
        // be reused again
        destroyMediaProjection();
    }

    private static void destroyMediaProjection() {
        if (mediaProjection != null) {
            mediaProjection.unregisterCallback(mediaProjectionCallback);
            mediaProjection.stop();
            mediaProjection = null;
        }
    }

    private static void stopService() {
        RecorderServiceConnection oldConnection = connection;
        connection = null;
        if (oldConnection != null) {
            Context context = oldConnection.boundContext.get();
            if (context != null) {
                try {
                    context.unbindService(oldConnection);
                } catch (IllegalArgumentException e) {
                    // already unbonded
                }
            }
        }
    }

    static boolean isRecording() {
        return recording;
    }

    private static void setRecording(boolean recording) {
        boolean previous = RecordingManager.recording;
        RecordingManager.recording = recording;
        if (previous != recording) {
            for (OnRecordingChangedListener listener : listeners) {
                listener.onRecordingChanged(recording);
            }
        }
    }

    static void addOnRecordingChangedListener(OnRecordingChangedListener listener) {
        listeners.add(listener);
    }

    static boolean removeOnRecordingChangedListener(OnRecordingChangedListener listener) {
        return listeners.remove(listener);
    }

    private static class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            if (recording) {
                mediaRecorder.stop();
                mediaRecorder.reset();
            }
            mediaProjection = null;
            stopScreenSharing();
            stopService();
            setRecording(false);
        }
    }

    interface OnRecordingChangedListener {
        void onRecordingChanged(boolean recording);
    }

    final static class RecorderServiceConnection implements ServiceConnection {

        private final int resultCode;
        private final Intent data;

        private final WeakReference<Context> boundContext;

        RecorderServiceConnection(Context context, int resultCode, Intent data) {
            boundContext = new WeakReference<>(context);
            this.resultCode = resultCode;
            this.data = data;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (connection == this) {
                onServiceReady(resultCode, data);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (connection == this && isRecording()) {
                try {
                    stop();
                } catch (RecordingException e) {
                    Log.w(TAG, "unable to stop recording after RecordingService disconnected", e);
                }
            }
        }

        @Override
        public void onBindingDied(ComponentName name) {
            if (connection == this && isRecording()) {
                try {
                    stop();
                } catch (RecordingException e) {
                    Log.w(TAG, "unable to stop recording after RecordingService death", e);
                }
            }
        }
    }
}
