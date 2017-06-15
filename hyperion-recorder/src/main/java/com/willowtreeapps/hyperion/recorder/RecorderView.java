package com.willowtreeapps.hyperion.recorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.willowtreeapps.hyperion.core.internal.ActivityResults;
import com.willowtreeapps.hyperion.core.internal.Dagger;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RecorderView extends DrawerView implements ActivityResults.Listener {

    private static final String TAG = "HyperionRecorder";
    private static final int REQUEST_CODE = 1000;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private final MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private MediaProjectionCallback mediaProjectionCallback;
    private ToggleButton toggleButton;
    private Button shareButton;
    private MediaRecorder mediaRecorder;

    @Inject
    Activity activity;
    @Inject
    ActivityResults activityResults;
    @Inject
    DisplayMetrics displayMetrics;

    public RecorderView(@NonNull Context context) {
        super(context);
        Dagger.<RecorderComponent>getComponent(context).inject(this);

        mediaRecorder = new MediaRecorder();
        projectionManager = (MediaProjectionManager) context
                .getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        inflate(context, R.layout.view_recorder, this);
        toggleButton = (ToggleButton) findViewById(R.id.toggle);
        toggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButton.isChecked()) {
                    initRecorder();
                    shareScreen();
                } else {
                    mediaRecorder.stop();
                    mediaRecorder.reset();
                    Log.v(TAG, "Stopping Recording");
                    stopScreenSharing();
                }
            }
        });

        shareButton = (Button) findViewById(R.id.share);
        shareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = v.getContext();
                Intent intent = new Intent(Intent.ACTION_SEND);
                File file = new File(context.getFilesDir() + "/video.mp4");
                Uri uri = FileProvider.getUriForFile(
                        v.getContext(), "com.willowtreeapps.hyperion.recorder.fileprovider", file);
                intent.setType("video/mp4");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                v.getContext().startActivity(Intent.createChooser(intent, "Share file with..."));
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        activityResults.register(this);
        destroyMediaProjection();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        activityResults.unregister(this);
    }

    private VirtualDisplay createVirtualDisplay() {
        return mediaProjection.createVirtualDisplay("HyperionRecorder",
                displayMetrics.widthPixels, displayMetrics.heightPixels, displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.getSurface(), null /*Callbacks*/, null
                /*Handler*/);
    }

    private void shareScreen() {
        if (mediaProjection == null) {
            activity.startActivityForResult(projectionManager.createScreenCaptureIntent(), REQUEST_CODE);
            return;
        }
        virtualDisplay = createVirtualDisplay();
        mediaRecorder.start();
    }

    private void stopScreenSharing() {
        if (virtualDisplay == null) {
            return;
        }
        virtualDisplay.release();
        //mMediaRecorder.release(); //If used: mMediaRecorder object cannot
        // be reused again
        destroyMediaProjection();
    }

    private void destroyMediaProjection() {
        if (mediaProjection != null) {
            mediaProjection.unregisterCallback(mediaProjectionCallback);
            mediaProjection.stop();
            mediaProjection = null;
        }
        Log.i(TAG, "MediaProjection Stopped");
    }

    private void initRecorder() {
        try {
            //mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(activity.getFilesDir().getPath() + "/video.mp4");
            mediaRecorder.setVideoSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setVideoEncodingBitRate(512 * 2000);
            mediaRecorder.setVideoFrameRate(30);
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATIONS.get(rotation + 90);
            mediaRecorder.setOrientationHint(orientation);
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE) {
            Log.e(TAG, "Unknown request code: " + requestCode);
            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(getContext(),
                    "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
            toggleButton.setChecked(false);
            return;
        }
        mediaProjectionCallback = new MediaProjectionCallback();
        mediaProjection = projectionManager.getMediaProjection(resultCode, data);
        mediaProjection.registerCallback(mediaProjectionCallback, null);
        virtualDisplay = createVirtualDisplay();
        mediaRecorder.start();
    }

    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            if (toggleButton.isChecked()) {
                toggleButton.setChecked(false);
                mediaRecorder.stop();
                mediaRecorder.reset();
                Log.v(TAG, "Recording Stopped");
            }
            mediaProjection = null;
            stopScreenSharing();
        }
    }
}