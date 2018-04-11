package com.willowtreeapps.hyperion.phoenix;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.plugin.v1.HyperionMenu;
import com.willowtreeapps.hyperion.plugin.v1.MenuState;
import com.willowtreeapps.hyperion.plugin.v1.OnMenuStateChangedListener;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

class PhoenixModule extends PluginModule implements OnMenuStateChangedListener {

    private HyperionMenu hyperionMenu;
    private PopupMenu popupMenu;

    @Override
    protected void onCreate() {
        hyperionMenu = getExtension().getHyperionMenu();
        if (hyperionMenu != null) {
            hyperionMenu.addOnMenuStateChangedListener(this);
        }
    }

    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        final View view = layoutInflater.inflate(R.layout.hp_item_plugin, parent, false);
        final View optionsButton = view.findViewById(R.id.button_more);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

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

    @Override
    protected void onDestroy() {
        hyperionMenu.removeOnMenuStateChangedListener(this);
    }

    private void showMenu(View anchor) {
        final Context context = anchor.getContext();
        if (popupMenu != null) {
            popupMenu.dismiss();
        }
        popupMenu = new PopupMenu(context, anchor);
        popupMenu.inflate(R.menu.hp_menu_options);

        final Menu menu = popupMenu.getMenu();
        final MenuItem clearCacheItem = menu.findItem(R.id.menu_item_clear_cache);
        final MenuItem clearDataItem = menu.findItem(R.id.menu_item_clear_data);
        final MenuItem restartSelfItem = menu.findItem(R.id.menu_item_restart_self);

        final RebirthOptions options = ProcessPhoenix.getOptions();
        clearCacheItem.setChecked(options.shouldClearCache());
        clearDataItem.setChecked(options.shouldClearData());
        restartSelfItem.setChecked(options.shouldRestartSelf());

        clearCacheItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(!item.isChecked());
                final RebirthOptions options = ProcessPhoenix.getOptions().builder()
                        .clearCache(item.isChecked())
                        .build();
                ProcessPhoenix.setOptions(options);
                return true;
            }
        });

        clearDataItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(!item.isChecked());
                final RebirthOptions options = ProcessPhoenix.getOptions().builder()
                        .clearData(item.isChecked())
                        .build();
                ProcessPhoenix.setOptions(options);
                return true;
            }
        });

        restartSelfItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(!item.isChecked());
                final RebirthOptions options = ProcessPhoenix.getOptions().builder()
                        .restartSelf(item.isChecked())
                        .build();
                ProcessPhoenix.setOptions(options);
                return true;
            }
        });

        popupMenu.show();
    }

    @Override
    public void onMenuStateChanged(@NonNull MenuState menuState) {
        if (menuState == MenuState.CLOSING && popupMenu != null) {
            popupMenu.dismiss();
        }
    }
}