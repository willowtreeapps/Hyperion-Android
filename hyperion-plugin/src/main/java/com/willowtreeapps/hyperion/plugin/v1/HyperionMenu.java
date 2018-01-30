package com.willowtreeapps.hyperion.plugin.v1;

import android.support.annotation.NonNull;

public interface HyperionMenu {

    MenuState getMenuState();

    void setMenuState(MenuState menuState);

    void addOnMenuStateChangedListener(@NonNull OnMenuStateChangedListener listener);

    boolean removeOnMenuStateChangedListener(@NonNull OnMenuStateChangedListener listener);
}