package com.willowtreeapps.hyperion.phoenix;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
        new AlertDialog.Builder(activity)
                .setTitle(R.string.hp_pheonix_options_title)
                .setMultiChoiceItems(R.array.hp_phoenix_options,
                        loadPhoenixOptions(activity.getResources()),
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

    private boolean[] loadPhoenixOptions(@NonNull Resources resources) {
        final String[] selection
                = resources.getStringArray(R.array.hp_phoenix_options);
        final boolean[] options = new boolean[selection.length];

        for (int i = 0; i < selection.length; i++) {
            switch (i) {
                case 0:
                    options[i] = ProcessPhoenix.getOptions().shouldClearCache();
                    break;
                case 1:
                    options[i] = ProcessPhoenix.getOptions().shouldClearData();
                    break;
                case 2:
                    options[i] = ProcessPhoenix.getOptions().shouldRestartSelf();
                    break;
            }
        }
        return options;
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