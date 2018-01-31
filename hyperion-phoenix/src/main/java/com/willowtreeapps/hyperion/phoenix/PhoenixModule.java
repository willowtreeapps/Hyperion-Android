package com.willowtreeapps.hyperion.phoenix;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
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
                ProcessPhoenix.triggerRebirth(activity);
            }
        });
        return view;
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