package com.willowtreeapps.hyperion.geigercounter;

import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.willowtreeapps.hyperion.plugin.v1.HyperionMenu;
import com.willowtreeapps.hyperion.plugin.v1.MenuState;
import com.willowtreeapps.hyperion.plugin.v1.OnMenuStateChangedListener;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

import static android.content.Context.AUDIO_SERVICE;
import static android.widget.Toast.LENGTH_SHORT;

@RequiresApi(GeigerCounterPlugin.API_VERSION)
class GeigerCounterModule extends PluginModule implements View.OnClickListener, OnMenuStateChangedListener, DroppedFrameDetectorObserver {

    private final DroppedFrameDetector detector;

    private View view;
    private HyperionMenu hyperionMenu;
    private PopupMenu popupMenu;

    GeigerCounterModule(DroppedFrameDetector detector) {
        this.detector = detector;
    }

    // Helpers

    private void updateView(boolean detectorIsEnabled) {
        if (view != null) {
            view.setSelected(detectorIsEnabled);
        }
    }

    private void showMenu(View anchor) {
        final Context context = anchor.getContext();
        if (popupMenu != null) {
            popupMenu.dismiss();
        }

        popupMenu = new PopupMenu(context, anchor);
        popupMenu.inflate(R.menu.hgc_menu_options);

        final Menu menu = popupMenu.getMenu();
        final MenuItem areHapticsEnabledItem = menu.findItem(R.id.menu_item_use_haptics);

        areHapticsEnabledItem.setChecked(detector.areHapticsEnabled());

        areHapticsEnabledItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean areHapticsEnabled = !item.isChecked();
                item.setChecked(areHapticsEnabled);
                detector.setHapticsEnabled(areHapticsEnabled);
                return true;
            }
        });

        popupMenu.show();
    }

    // PluginModule

    @Override
    protected void onCreate() {
        super.onCreate();

        hyperionMenu = getExtension().getHyperionMenu();
        if (hyperionMenu != null) {
            hyperionMenu.addOnMenuStateChangedListener(this);
        }

        detector.addObserver(this);
    }

    @Nullable
    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.hgc_item_plugin, parent, false);
        view.setOnClickListener(this);

        View optionsButton = view.findViewById(R.id.button_more);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });

        updateView(detector.isEnabled());

        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        hyperionMenu.removeOnMenuStateChangedListener(this);
        detector.removeObserver(this);
    }

    // OnClickListener

    @Override
    public void onClick(View v) {
        boolean isEnabled = detector.isEnabled();

        // When enabling while the speaker is muted and haptics are off, remind the user to unmute.
        if (!isEnabled && !detector.areHapticsEnabled()) {
            AudioManager audioManager = (AudioManager) getContext().getSystemService(AUDIO_SERVICE);
            switch (audioManager.getRingerMode()) {
                case AudioManager.RINGER_MODE_SILENT:
                case AudioManager.RINGER_MODE_VIBRATE:
                    Toast.makeText(getContext(), R.string.hgc_volume_warning, LENGTH_SHORT).show();
                    break;
                case AudioManager.RINGER_MODE_NORMAL:
                    break;
            }
        }

        detector.setEnabled(!isEnabled);
    }

    // OnMenuStateChangedListener

    @Override
    public void onMenuStateChanged(@NonNull MenuState menuState) {
        if (menuState == MenuState.CLOSING && popupMenu != null) {
            popupMenu.dismiss();
        }
    }

    // DroppedFrameDetectorObserver

    @Override
    public void droppedFrameDetectorIsEnabledDidChange(DroppedFrameDetector detector, boolean isEnabled) {
        updateView(isEnabled);
    }

    @Override
    @Nullable
    public View getHostViewForDroppedFrameHapticFeedback() {
        return view;
    }
}
