package com.willowtreeapps.hyperion.plugin.v1;

import android.support.annotation.NonNull;

public interface HyperionMenu {

    MenuState getMenuState();

    /**
     * @deprecated This never made the ui to match the given menu state.
     */
    @Deprecated
    void setMenuState(MenuState menuState);

    void addOnMenuStateChangedListener(@NonNull OnMenuStateChangedListener listener);

    boolean removeOnMenuStateChangedListener(@NonNull OnMenuStateChangedListener listener);

    /**
     * Request to open the menu.
     *
     * @return true if the menu was opened
     */
    boolean expand();

    /**
     * Request the to close the menu.
     *
     * @return true if the menu was closed
     */
    boolean collapse();

}
