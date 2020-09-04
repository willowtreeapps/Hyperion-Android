package com.willowtreeapps.hyperion.attr.collectors;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.willowtreeapps.hyperion.attr.ViewAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Collectors {

    public static ViewAttribute<ColorValue> createColorAttribute(@NonNull String name, @Nullable ColorStateList color) {
        return createColorAttribute(null, name, color);
    }

    public static ViewAttribute<ColorValue> createColorAttribute(@Nullable View view, @NonNull String name, @Nullable ColorStateList color) {
        int tintColorInt;

        if (color == null) {
            tintColorInt = 0x000;
        } else {
            int[] state = view == null ? new int[] { } : view.getDrawableState();
            tintColorInt = color.getColorForState(state, color.getDefaultColor());
        }

        return new ViewAttribute<>(name, new ColorValue(tintColorInt), new ColorDrawable(tintColorInt));
    }

    public static Collection<ViewAttribute> createMenuAttributes(@NonNull Context context, @NonNull Menu menu) {
        final Resources res = context.getResources();
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute<>("MenuSize", menu.size()));
        attributes.add(new ViewAttribute<>("MenuHasVisibleItems", menu.hasVisibleItems()));
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            String prefix = "Item " + i + ": ";
            attributes.add(new ViewAttribute<>(prefix + "Title", item.getTitle()));
            attributes.add(new ViewAttribute<>(prefix + "ContentDescription",
                    MenuItemCompat.getContentDescription(item)));
            attributes.add(new ViewAttribute<>(prefix + "TooltipText", MenuItemCompat.getTooltipText(item)));
            attributes.add(new ViewAttribute<>(prefix + "Checkable", item.isCheckable()));
            attributes.add(new ViewAttribute<>(prefix + "Checked", item.isChecked()));
            attributes.add(new ViewAttribute<>(prefix + "Enabled", item.isEnabled()));
            attributes.add(new ViewAttribute<>(prefix + "Visible", item.isVisible()));
            attributes.add(new ViewAttribute<>(prefix + "Order", item.getOrder()));
            attributes.add(new ViewAttribute<Void>(prefix + "Icon", item.getIcon()));
            attributes.add(createColorAttribute("IconTint",
                    MenuItemCompat.getIconTintList(item)));
            attributes.add(new ViewAttribute<>("IconTintMode",
                    new PorterDuffModeValue(MenuItemCompat.getIconTintMode(item))));
            attributes.add(new ViewAttribute<>(prefix + "Id", new ResourceValue(res, item.getItemId())));
            attributes.add(new ViewAttribute<>(prefix + "GroupId", new ResourceValue(res, item.getGroupId())));
        }
        return attributes;
    }

    private Collectors() {
        throw new AssertionError("No instances");
    }
}