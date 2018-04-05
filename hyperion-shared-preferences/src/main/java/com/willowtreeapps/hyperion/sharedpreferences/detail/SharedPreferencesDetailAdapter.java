package com.willowtreeapps.hyperion.sharedpreferences.detail;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.sharedpreferences.R;
import com.willowtreeapps.hyperion.sharedpreferences.detail.viewholder.BooleanPreferenceViewHolder;
import com.willowtreeapps.hyperion.sharedpreferences.detail.viewholder.FloatPreferenceViewHolder;
import com.willowtreeapps.hyperion.sharedpreferences.detail.viewholder.IntPreferenceViewHolder;
import com.willowtreeapps.hyperion.sharedpreferences.detail.viewholder.LongPreferenceViewHolder;
import com.willowtreeapps.hyperion.sharedpreferences.detail.viewholder.PreferenceViewHolder;
import com.willowtreeapps.hyperion.sharedpreferences.detail.viewholder.StringPreferenceViewHolder;
import com.willowtreeapps.hyperion.sharedpreferences.detail.viewholder.StringSetPreferenceViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

class SharedPreferencesDetailAdapter extends RecyclerView.Adapter<PreferenceViewHolder> {

    private static final int VIEW_TYPE_INTEGER = 1;
    private static final int VIEW_TYPE_LONG = 2;
    private static final int VIEW_TYPE_FLOAT = 3;
    private static final int VIEW_TYPE_BOOLEAN = 4;
    private static final int VIEW_TYPE_STRING = 5;
    private static final int VIEW_TYPE_STRING_SET = 6;

    private final SharedPreferences sharedPreferences;

    SharedPreferencesDetailAdapter(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public PreferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_INTEGER:
                return new IntPreferenceViewHolder(
                        inflater.inflate(R.layout.hsp_navigation_preference_number_viewholder, parent, false),
                        sharedPreferences);
            case VIEW_TYPE_LONG:
                return new LongPreferenceViewHolder(
                        inflater.inflate(R.layout.hsp_navigation_preference_number_viewholder, parent, false),
                        sharedPreferences);
            case VIEW_TYPE_FLOAT:
                return new FloatPreferenceViewHolder(
                        inflater.inflate(R.layout.hsp_navigation_preference_number_viewholder, parent, false),
                        sharedPreferences);
            case VIEW_TYPE_BOOLEAN:
                return new BooleanPreferenceViewHolder(
                        inflater.inflate(R.layout.hsp_navigation_preference_boolean_viewholder, parent, false),
                        sharedPreferences);
            case VIEW_TYPE_STRING:
                return new StringPreferenceViewHolder(
                        inflater.inflate(R.layout.hsp_navigation_preference_string_viewholder, parent, false),
                        sharedPreferences);
            case VIEW_TYPE_STRING_SET:
                return new StringSetPreferenceViewHolder(
                        inflater.inflate(R.layout.hsp_navigation_preference_string_viewholder, parent, false));
            default:
                throw new RuntimeException("Unknown view type " + viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object preferenceValue = getValue(position);
        if (isObjectOfClass(preferenceValue, Integer.class)) {
            return VIEW_TYPE_INTEGER;
        } else if (isObjectOfClass(preferenceValue, Long.class)) {
            return VIEW_TYPE_LONG;
        } else if (isObjectOfClass(preferenceValue, Float.class)) {
            return VIEW_TYPE_FLOAT;
        } else if (isObjectOfClass(preferenceValue, Boolean.class)) {
            return VIEW_TYPE_BOOLEAN;
        } else if (isObjectOfClass(preferenceValue, String.class)) {
            return VIEW_TYPE_STRING;
        } else if (isObjectOfClass(preferenceValue, Set.class)) {
            return VIEW_TYPE_STRING_SET;
        } else {
            throw new RuntimeException("Unknown preference type: " + preferenceValue.getClass().getSimpleName());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder, int position) {
        String key = getKey(position);
        Object preferenceValue = getSharedPreferencesMap().get(key);
        holder.bind(key, preferenceValue);
    }

    @Override
    public int getItemCount() {
        return getKeys().size();
    }

    @SuppressWarnings("unchecked")
    private <T> T getValue(int position) {
        String key = getKey(position);
        return (T) getSharedPreferencesMap().get(key);
    }

    public void notifyKeyChanged(String key) {
        int index = getKeysSorted().indexOf(key);
        notifyItemChanged(index);
    }

    private String getKey(int position) {
        return getKeysSorted().get(position);
    }

    private Boolean isObjectOfClass(Object object, Class<?> clazz) {
        return clazz.isAssignableFrom(object.getClass());
    }

    private Map<String, ?> getSharedPreferencesMap() {
        return sharedPreferences.getAll();
    }

    private Set<String> getKeys() {
        return getSharedPreferencesMap().keySet();
    }

    private List<String> getKeysSorted() {
        List<String> sortedList = new ArrayList<>(getKeys());
        Collections.sort(sortedList);
        return sortedList;
    }

}
