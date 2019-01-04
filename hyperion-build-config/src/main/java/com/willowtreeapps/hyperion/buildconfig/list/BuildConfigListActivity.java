package com.willowtreeapps.hyperion.buildconfig.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.willowtreeapps.hyperion.buildconfig.R;
import com.willowtreeapps.hyperion.buildconfig.model.BuildConfigValue;
import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@HyperionIgnore
public class BuildConfigListActivity extends AppCompatActivity {

    private static final String TAG = "BuildConfig";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hbc_activity_build_config_list);
        setSupportActionBar((Toolbar) findViewById(R.id.tmb_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.hbc_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BuildConfigListAdapter(getBuildConfigValues()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private String buildConfigName() {
        String resString = getString(R.string.hbc_target_build_config_name);
        if (resString.isEmpty()) {
            return getPackageName() + ".BuildConfig";
        } else {
            return resString;
        }
    }

    private List<BuildConfigValue> getBuildConfigValues() {
        List<BuildConfigValue> buildConfigValues = new LinkedList<>();
        try {
            Class<?> buildConfigClass = Class.forName(buildConfigName());
            Log.d(TAG, "Checking BuildConfig " + buildConfigClass.getName());
            Field[] declaredFields = buildConfigClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Log.d(TAG, "Inspecting " + declaredField.toString());
                if (Modifier.isStatic(declaredField.getModifiers())) {
                    try {
                        if (!declaredField.isAccessible()) declaredField.setAccessible(true);
                        Class<?> fieldType = declaredField.getType();
                        String name = declaredField.getName() + " (" + fieldType.getSimpleName() + ")";
                        String value = objectToString(declaredField.get(null));
                        buildConfigValues.add(new BuildConfigValue(name, value));
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to process field from config.", e);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to read BuildConfig", e);
        }

        return buildConfigValues;
    }

    private String objectToString(Object obj) {
        if (obj.getClass().isArray()) {
            if (obj instanceof boolean[]) return Arrays.toString((boolean[]) obj);
            if (obj instanceof byte[]) return Arrays.toString((byte[]) obj);
            if (obj instanceof int[]) return Arrays.toString((int[]) obj);
            if (obj instanceof short[]) return Arrays.toString((short[]) obj);
            if (obj instanceof long[]) return Arrays.toString((long[]) obj);
            if (obj instanceof float[]) return Arrays.toString((float[]) obj);
            if (obj instanceof double[]) return Arrays.toString((double[]) obj);
            if (obj instanceof char[]) return Arrays.toString((char[]) obj);
            return Arrays.toString((Object[]) obj);
        } else return obj.toString();
    }

}