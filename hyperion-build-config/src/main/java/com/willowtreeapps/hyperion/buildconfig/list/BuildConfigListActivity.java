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
import java.util.LinkedList;
import java.util.List;

@HyperionIgnore
public class BuildConfigListActivity extends AppCompatActivity {

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

    private List<BuildConfigValue> getBuildConfigValues() {
        List<BuildConfigValue> buildConfigValues = new LinkedList<>();
        try {
            Class<?> buildConfigClass = Class.forName(getPackageName() + ".BuildConfig");
            Log.d("BuildConfig", "Checking BuildConfig " + buildConfigClass.getName());
            Field[] declaredFields = buildConfigClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Log.d("BuildConfig", "Inspecting " + declaredField.toString());
                if (Modifier.isStatic(declaredField.getModifiers())) {
                    Class<?> fieldType = declaredField.getType();
                    String name = declaredField.getName() + " (" + fieldType.getSimpleName() + ")";
                    String value = declaredField.get(null).toString();
                    buildConfigValues.add(new BuildConfigValue(name, value));
                }
            }
        } catch (Exception e) {
            Log.e("BuildConfig", "Failed to read BuildConfig");
            e.printStackTrace();
        }

        return buildConfigValues;
    }

}