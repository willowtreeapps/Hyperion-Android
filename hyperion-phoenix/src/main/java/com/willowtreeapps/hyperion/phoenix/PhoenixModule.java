package com.willowtreeapps.hyperion.phoenix;

import android.app.Activity;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

class PhoenixModule extends PluginModule {

    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        final View view = layoutInflater.inflate(R.layout.hp_item_plugin, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Activity activity = getExtension().getActivity();
                confirmRebirth(activity);
            }
        });
        return view;
    }


    private void confirmRebirth(final Activity activity) {
        new AlertDialog.Builder(activity, R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle(R.string.hp_pheonix_options_title)
                .setMultiChoiceItems(R.array.hp_phoenix_options,
                        loadPhoenixOptions(),
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                setOptionSelection(which, isChecked);
                            }
                        })
                .setPositiveButton(R.string.hp_dialog_confirmation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProcessPhoenix.triggerRebirth(activity);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    private boolean[] loadPhoenixOptions() {
        final RebirthOptions options = ProcessPhoenix.getOptions();
        return new boolean[]{options.shouldClearCache(),
                options.shouldClearData(),
                options.shouldRestartSelf()};
    }

    private void setOptionSelection(int selectionIndex, boolean selected) {
        final RebirthOptions.Builder optionsBuilder
                = ProcessPhoenix.getOptions().builder();
        switch (selectionIndex) {
            case 0:
                optionsBuilder.clearCache(selected);
                break;
            case 1:
                optionsBuilder.clearData(selected);
                break;
            case 2:
                optionsBuilder.restartSelf(selected);
                break;
        }
        ProcessPhoenix.setOptions(optionsBuilder.build());
    }
}