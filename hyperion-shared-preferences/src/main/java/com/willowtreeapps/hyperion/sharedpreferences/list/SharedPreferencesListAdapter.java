package com.willowtreeapps.hyperion.sharedpreferences.list;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.sharedpreferences.R;

import java.util.ArrayList;
import java.util.List;

class SharedPreferencesListAdapter extends RecyclerView.Adapter<SharedPreferencesFileViewHolder> {

    private final List<String> preferences;

    SharedPreferencesListAdapter(List<String> preferences) {
        this.preferences = new ArrayList<>(preferences);
    }

    @NonNull
    @Override
    public SharedPreferencesFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hsp_navigation_preference_file_viewholder, parent, false);
        return new SharedPreferencesFileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SharedPreferencesFileViewHolder holder, int position) {
        holder.bind(preferences.get(position));
    }

    @Override
    public int getItemCount() {
        return preferences.size();
    }
}