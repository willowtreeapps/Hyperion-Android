package com.willowtreeapps.hyperion.timber.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.timber.R;
import com.willowtreeapps.hyperion.timber.model.CircularBuffer;
import com.willowtreeapps.hyperion.timber.model.LogItem;

class TimberLogListAdapter extends RecyclerView.Adapter<TimberLogViewHolder> {

    private final CircularBuffer<LogItem> logItems;

    TimberLogListAdapter(CircularBuffer<LogItem> logItems) {
        this.logItems = logItems;
    }

    @NonNull
    @Override
    public TimberLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tmb_timber_log_row, parent, false);
        return new TimberLogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TimberLogViewHolder holder, int position) {
        LogItem logItem = logItems.getItem(position);
        holder.bind(logItem);
    }

    @Override
    public int getItemCount() {
        return logItems.size();
    }
}