package com.willowtreeapps.hyperion.attr.design;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.AttributeValue;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.ColorValue;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class FloatingActionButtonAttributeCollector extends TypedAttributeCollector<FloatingActionButton> {

    public FloatingActionButtonAttributeCollector() {
        super(FloatingActionButton.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(FloatingActionButton view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute<>("RippleColor", new ColorValue(view.getRippleColor())));
        attributes.add(new ViewAttribute<>("UseCompatPadding", view.getUseCompatPadding()));
        attributes.add(new ViewAttribute<>("Size", new SizeValue(view.getSize())));
        attributes.add(new ViewAttribute<Void>("ContentBackground", view.getContentBackground()));
        attributes.add(new ViewAttribute<>("CompatElevation", view.getCompatElevation()));
        return attributes;
    }

    private static class SizeValue implements AttributeValue {

        @FloatingActionButton.Size
        private final int size;

        private SizeValue(@FloatingActionButton.Size int size) {
            this.size = size;
        }

        @Override
        public CharSequence getDisplayValue() {
            switch (size) {
                case FloatingActionButton.SIZE_NORMAL:
                    return "Normal";
                case FloatingActionButton.SIZE_AUTO:
                    return "Auto";
                case FloatingActionButton.SIZE_MINI:
                    return "Mini";
                default:
                    return "Unknown";
            }
        }
    }
}