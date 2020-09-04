package com.willowtreeapps.hyperion.timber.list;

import android.content.res.Resources;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.willowtreeapps.hyperion.timber.R;
import com.willowtreeapps.hyperion.timber.model.LogItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

class TimberLogViewHolder extends RecyclerView.ViewHolder {

    private final Resources resources;
    private final View logLevelView;
    private final TextView logDateTextView;
    private final TextView logMsgTextView;
    private final DateFormat dateFormat;

    TimberLogViewHolder(View itemView) {
        super(itemView);
        resources = itemView.getResources();
        logLevelView = itemView.findViewById(R.id.tmb_log_level);
        logDateTextView = itemView.findViewById(R.id.tmb_log_date);
        logMsgTextView = itemView.findViewById(R.id.tmb_log_msg);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.ENGLISH);
    }

    void bind(LogItem logItem) {
        int logLevelColor = ResourcesCompat.getColor(resources, logItem.level.colorRes, null);
        logLevelView.setBackgroundColor(logLevelColor);
        logDateTextView.setText(dateFormat.format(logItem.date));
        logMsgTextView.setText(logItem.message);
        Linkify.addLinks(logMsgTextView, Linkify.ALL);
    }

}
