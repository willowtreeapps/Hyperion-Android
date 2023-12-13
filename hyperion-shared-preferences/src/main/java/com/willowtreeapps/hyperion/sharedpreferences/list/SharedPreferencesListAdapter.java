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
    private List<String> filteredPreferences;
    private boolean isFilterApplied;

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
        String pref = isFilterApplied ? filteredPreferences.get(position) : preferences.get(position);
        holder.bind(pref);
    }

    @Override
    public int getItemCount() {
        return isFilterApplied ? filteredPreferences.size() : preferences.size();
    }

    void filter(String query) {
        String filterQuery = query == null ? "" : query.toLowerCase();
        isFilterApplied = !filterQuery.isEmpty();
        this.filteredPreferences = new ArrayList<>();
        if (isFilterApplied) {
            for (String pref : preferences) {
                if (pref.toLowerCase().contains(filterQuery)) {
                    filteredPreferences.add(pref);
                }
            }
        } else {
            filteredPreferences.addAll(preferences);
        }
        notifyDataSetChanged();
    }

}