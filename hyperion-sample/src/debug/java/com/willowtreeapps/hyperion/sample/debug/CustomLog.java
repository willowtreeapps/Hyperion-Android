package com.willowtreeapps.hyperion.sample.debug;

import com.willowtreeapps.hyperion.timber.TimberPlugin;
import com.willowtreeapps.hyperion.timber.model.Level;
import com.willowtreeapps.hyperion.timber.model.LogItem;

import java.util.Date;

public class CustomLog {

    private CustomLog() {
    }

    public static void debug(String message) {
        TimberPlugin.logItemBuffer
                .enqueue(new LogItem(Level.DEBUG, new Date(), message));
    }

}
