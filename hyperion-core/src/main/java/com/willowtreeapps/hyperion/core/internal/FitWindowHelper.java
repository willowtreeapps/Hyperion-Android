package com.willowtreeapps.hyperion.core.internal;

import android.content.Context;
import android.content.res.Resources;
import android.util.Pair;
import android.util.TypedValue;

/**
 * Figures out the margins needed to fit the system windows
 */
public class FitWindowHelper {

    /**
     * Figures out margins needed to fit system windows
     * Defaults to 0
     *
     * @return first = status bar height, second = nav bar height
     */
    static Pair<Integer, Integer> getFitWindowMargins(Context context) {
        int navBarHeight = 0;
        int statusBarHeight = 0;
        TypedValue fitsSystemVal = new TypedValue();
        boolean fitsSystemFound = context.getTheme().resolveAttribute(android.R.attr.fitsSystemWindows, fitsSystemVal, true);
        if (fitsSystemFound && fitsSystemVal.data == 0) {   //if it found the attribute and fitsSystemWindows == false
            Resources resources = context.getResources();

            int resId = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (resId > 0) {
                statusBarHeight = resources.getDimensionPixelSize(resId);
            }

            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                navBarHeight = resources.getDimensionPixelSize(resourceId);
            }
        }

        return Pair.create(statusBarHeight, navBarHeight);
    }
}
