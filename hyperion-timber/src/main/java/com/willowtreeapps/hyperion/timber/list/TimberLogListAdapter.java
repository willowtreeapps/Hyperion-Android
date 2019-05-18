package com.willowtreeapps.hyperion.timber.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.timber.R;
import com.willowtreeapps.hyperion.timber.model.CircularBuffer;
import com.willowtreeapps.hyperion.timber.model.LogItem;

import java.util.ArrayList;
import java.util.List;

class TimberLogListAdapter extends RecyclerView.Adapter<TimberLogViewHolder> {

    private final CircularBuffer<LogItem> logItems;
    private String filter;
    private List<LogItem> filteredLogItems;

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
        LogItem logItem = filter == null ? logItems.getItem(position) : filteredLogItems.get(position);
        holder.bind(logItem);
    }

    @Override
    public int getItemCount() {
        return filter == null ? logItems.size() : filteredLogItems.size();
    }

    public void applyFilter(@Nullable String filter) {
        this.filter = filter;
        filteredLogItems = new ArrayList<>();

        if (filter != null) {
            for (int i = 0; i < logItems.size(); i++) {
                LogItem item = logItems.getItem(i);
                if (containsIgnoreCase(item.message, filter)) {
                    filteredLogItems.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    private boolean containsIgnoreCase(String str, String query) {
        if (str != null && query != null) {
            int limit = str.length() - query.length() + 1;
            for (int i = 0; i < limit; i++) {
                if (matchesIgnoreCase(str, query, i)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesIgnoreCase(String str, String query, int startingAt) {
        int len = query.length();
        for (int i = 0; i < len; i++) {
            if (Character.toUpperCase(query.charAt(i)) != Character.toUpperCase(str.charAt(startingAt + i))) {
                return false;
            }
        }
        return true;
    }
}
