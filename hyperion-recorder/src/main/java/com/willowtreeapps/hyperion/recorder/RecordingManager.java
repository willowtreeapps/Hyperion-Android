package com.willowtreeapps.hyperion.recorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
final class RecordingManager {

    private static final String TAG = "RecordingManager";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final CopyOnWriteArrayList<OnRecordingChangedListener> listeners = new CopyOnWriteArrayList<>();
    private static MediaRecorder mediaRecorder;
    private static MediaProjectionManager projectionManager;
    private static MediaProjection mediaProjection;
    private static VirtualDisplay virtualDisplay;
    private static MediaProjectionCallback mediaProjectionCallback;
    private static DisplayMetrics displayMetrics;

    private static boolean recording;

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
        String outputPath = context.getFilesDir().getPath() + "/hyperion_recorder/" + videoId + ".mp4";

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

    static void requestStart(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(projectionManager.createScreenCaptureIntent(), requestCode);
    }

    static void start(int resultCode, Intent data) {
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

    static void stop() throws RecordingException {
        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            stopScreenSharing();
        } catch (RuntimeException ex) {
            throw new RecordingException("Failed to stop media recorder.", ex);
        } finally {
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
            setRecording(false);
        }
    }

    interface OnRecordingChangedListener {
        void onRecordingChanged(boolean recording);
    }

}