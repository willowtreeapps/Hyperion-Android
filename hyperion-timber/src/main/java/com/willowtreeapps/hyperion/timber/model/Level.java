package com.willowtreeapps.hyperion.timber.model;

import android.support.annotation.ColorRes;
import android.util.Log;

import com.willowtreeapps.hyperion.timber.R;

public enum Level {
    // FIXME
    ASSERT(R.color.tmb_log_level_assert),
    DEBUG(R.color.tmb_log_level_debug),
    ERROR(R.color.tmb_log_level_error),
    INFO(R.color.tmb_log_level_info),
    VERBOSE(R.color.tmb_log_level_verbose),
    WARN(R.color.tmb_log_level_warn);

    @ColorRes
    public final int colorRes;

    Level(int colorRes) {
        this.colorRes = colorRes;
    }

    public static Level forLogLevel(int logLevel) {
        switch (logLevel) {
            case Log.ASSERT:
                return ASSERT;
            case Log.DEBUG:
                return DEBUG;
            case Log.ERROR:
                return ERROR;
            case Log.INFO:
                return INFO;
            case Log.VERBOSE:
                return VERBOSE;
            case Log.WARN:
                return WARN;
            default:
                throw new RuntimeException("Unknown log level " + logLevel);
        }
    }
}
