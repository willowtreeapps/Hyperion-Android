package com.willowtreeapps.hyperion.attr.design;

import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.AttributeValue;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.Collectors;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class TabLayoutAttributeCollector extends TypedAttributeCollector<TabLayout> {

    public TabLayoutAttributeCollector() {
        super(TabLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(TabLayout view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute<>("SelectedTabPosition", view.getSelectedTabPosition()));
        attributes.add(new ViewAttribute<>("TabCount", view.getTabCount()));
        attributes.add(new ViewAttribute<>("TabMode", new TabModeValue(view.getTabMode())));
        attributes.add(new ViewAttribute<>("TabGravity", new TabGravityValue(view.getTabGravity())));
        attributes.add(Collectors.createColorAttribute(view, "TabTextColor", view.getTabTextColors()));
        return attributes;
    }

    private static class TabModeValue implements AttributeValue {

        @TabLayout.Mode
        private final int mode;

        private TabModeValue(@TabLayout.Mode int mode) {
            this.mode = mode;
        }

        @Override
        public CharSequence getDisplayValue() {
            switch (mode) {
                case TabLayout.MODE_SCROLLABLE:
                    return "Scrollable";
                case TabLayout.MODE_FIXED:
                    return "Fixed";
                default:
                    return "Unknown";
            }
        }
    }

    private static class TabGravityValue implements AttributeValue {

        @TabLayout.TabGravity
        private final int gravity;

        private TabGravityValue(@TabLayout.TabGravity int gravity) {
            this.gravity = gravity;
        }

        @Override
        public CharSequence getDisplayValue() {
            switch (gravity) {
                case TabLayout.GRAVITY_FILL:
                    return "Fill";
                case TabLayout.GRAVITY_CENTER:
                    return "Center";
                default:
                    return "Unknown";
            }
        }
    }

}
