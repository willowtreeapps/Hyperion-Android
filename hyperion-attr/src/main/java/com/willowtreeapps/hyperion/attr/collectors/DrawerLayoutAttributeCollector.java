package com.willowtreeapps.hyperion.attr.collectors;

import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.AttributeValue;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class DrawerLayoutAttributeCollector extends TypedAttributeCollector<DrawerLayout> {

    public DrawerLayoutAttributeCollector() {
        super(DrawerLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(DrawerLayout view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();

        attributes.add(new ViewAttribute<>("DrawerElevation", view.getDrawerElevation()));
        attributes.add(new ViewAttribute<>("DrawerTitleStart",
                view.getDrawerTitle(Gravity.START)));
        attributes.add(new ViewAttribute<>("DrawerTitleEnd",
                view.isDrawerVisible(Gravity.END)));
        attributes.add(new ViewAttribute<>("DrawerLockModeStart",
                new DrawerLayoutLockModeValue(view.getDrawerLockMode(Gravity.START))));
        attributes.add(new ViewAttribute<>("DrawerLockModeEnd",
                new DrawerLayoutLockModeValue(view.getDrawerLockMode(Gravity.END))));
        attributes.add(new ViewAttribute<>("DrawerOpenStart",
                view.isDrawerOpen(Gravity.START)));
        attributes.add(new ViewAttribute<>("DrawerOpenEnd",
                view.isDrawerOpen(Gravity.END)));
        attributes.add(new ViewAttribute<>("DrawerVisibleStart",
                view.isDrawerVisible(Gravity.START)));
        attributes.add(new ViewAttribute<>("DrawerVisibleEnd",
                view.isDrawerVisible(Gravity.END)));
        attributes.add(new ViewAttribute<Void>("StatusBarBackgroundDrawable",
                view.getStatusBarBackgroundDrawable()));

        return attributes;
    }

    private static class DrawerLayoutLockModeValue implements AttributeValue {

        private final int mode;

        public DrawerLayoutLockModeValue(int mode) {
            this.mode = mode;
        }

        @Override
        public CharSequence getDisplayValue() {
            switch (mode) {
                case DrawerLayout.LOCK_MODE_UNLOCKED:
                    return "Unlocked";
                case DrawerLayout.LOCK_MODE_UNDEFINED:
                    return "Undefined";
                case DrawerLayout.LOCK_MODE_LOCKED_OPEN:
                    return "Locked Open";
                case DrawerLayout.LOCK_MODE_LOCKED_CLOSED:
                    return "Locked Closed";
                default:
                    return "Unknown";
            }
        }
    }
}