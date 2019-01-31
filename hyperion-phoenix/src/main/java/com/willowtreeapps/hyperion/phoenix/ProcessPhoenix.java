package com.willowtreeapps.hyperion.phoenix;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;

import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Based on Jake Wharton's Process Phoenix library: https://github.com/JakeWharton/ProcessPhoenix
 *
 * Process Phoenix facilitates restarting your application process. This should only be used for
 * things like fundamental state changes in your debug builds (e.g., changing from staging to
 * production).
 * <p>
 * Trigger process recreation by calling {@link #triggerRebirth} with a {@link Context} instance.
 */
@HyperionIgnore
public class ProcessPhoenix extends Activity {

    private static final String KEY_RESTART_INTENTS = "phoenix_restart_intents";
    private static final String KEY_CLEAR_CACHE = "phoenix_clear_cache";
    private static final String KEY_CLEAR_DATA = "phoenix_clear_data";

    private static RebirthOptions options = RebirthOptions.DEFAULT;

    static void setOptions(RebirthOptions options) {
        ProcessPhoenix.options = options;
    }

    static RebirthOptions getOptions() {
        return options;
    }

    /**
     * Call to restart the application process using the {@linkplain Intent#CATEGORY_DEFAULT default}
     * activity as an intent.
     * <p>
     * Behavior of the current process after invoking this method is undefined.
     */
    public static void triggerRebirth(Context context) {
        triggerRebirth(context, getRestartIntent(context));
    }

    /**
     * Call to restart the application process using the specified intents.
     * <p>
     * Behavior of the current process after invoking this method is undefined.
     */
    public static void triggerRebirth(Context context, Intent... nextIntents) {
        Intent intent = new Intent(context, ProcessPhoenix.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK); // In case we are called with non-Activity context.
        intent.putParcelableArrayListExtra(KEY_RESTART_INTENTS, new ArrayList<>(Arrays.asList(nextIntents)));
        intent.putExtra(KEY_CLEAR_CACHE, options.shouldClearCache());
        intent.putExtra(KEY_CLEAR_DATA, options.shouldClearData());
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
        Runtime.getRuntime().exit(0); // Kill kill kill!
    }

    private static Intent getRestartIntent(Context context) {
        String packageName = context.getPackageName();
        Intent intent;

        if (options.shouldRestartSelf()) {
            intent = new Intent(context, context.getClass());
        } else {
            intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        }

        if (intent != null) {
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
            return intent;
        }

        throw new IllegalStateException("Unable to determine default activity for "
                + packageName
                + ". Does an activity specify the DEFAULT category in its intent filter?");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final ArrayList<Intent> intents = intent.getParcelableArrayListExtra(KEY_RESTART_INTENTS);
        final boolean shouldClearCache = intent.getBooleanExtra(KEY_CLEAR_CACHE, false);
        final boolean shouldClearData = intent.getBooleanExtra(KEY_CLEAR_DATA, false);

        if (shouldClearCache) {
            clearCache();
        }

        if (shouldClearData) {
            clearData();
        }

        startActivities(intents.toArray(new Intent[intents.size()]));
        finish();
        Runtime.getRuntime().exit(0); // Kill kill kill!
    }

    private void clearCache() {
        try {
            File[] files = getCacheDir().listFiles();

            for (File file : files) {
                delete(file);
            }
        } catch (Exception e) {
            // this is fine.
        }
    }

    private void clearData() {
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    delete(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    private static boolean delete(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (String child : children) {
                    deletedAll = delete(new File(file, child)) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }

    /**
     * Checks if the current process is a temporary Phoenix Process.
     * This can be used to avoid initialisation of unused resources or to prevent running code that
     * is not multi-process ready.
     *
     * @return true if the current process is a temporary Phoenix Process
     */
    public static boolean isPhoenixProcess(Context context) {
        int currentPid = Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
        if (runningProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.pid == currentPid && processInfo.processName.endsWith(":phoenix")) {
                    return true;
                }
            }
        }
        return false;
    }
}
