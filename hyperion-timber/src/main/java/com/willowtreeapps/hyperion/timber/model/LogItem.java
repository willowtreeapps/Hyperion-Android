package com.willowtreeapps.hyperion.timber.model;

import android.os.Parcel;

import java.util.Date;

/**
 * Log item model representing a displayable row.
 */
public final class LogItem {

    public final Level level;
    public final Date date;
    public final String message;

    public LogItem(Level level, Date date, String message) {
        this.level = level;
        this.date = date;
        this.message = message;
    }

    private LogItem(Parcel in) {
        level = Level.valueOf(in.readString());
        date = new Date(in.readLong());
        message = in.readString();
    }

}
