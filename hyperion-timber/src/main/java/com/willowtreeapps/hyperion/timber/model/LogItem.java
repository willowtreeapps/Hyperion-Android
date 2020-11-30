package com.willowtreeapps.hyperion.timber.model;

import android.os.Parcel;

import androidx.annotation.Nullable;

import java.util.Date;

/**
 * Log item model representing a displayable row.
 */
public final class LogItem {

    public final Level level;
    public final Date date;
    @Nullable public final String tag;
    public final String message;

    public LogItem(Level level, Date date, @Nullable String tag, String message) {
        this.level = level;
        this.date = date;
        this.tag = tag;
        this.message = message;
    }

    public LogItem(Level level, Date date, String message) {
        this(level, date, null, message);
    }

    private LogItem(Parcel in) {
        level = Level.valueOf(in.readString());
        date = new Date(in.readLong());
        tag = in.readString();
        message = in.readString();
    }

}
