package com.willowtreeapps.hyperion.attr.design;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class CoordinatorLayoutAttributeCollector extends TypedAttributeCollector<CoordinatorLayout> {

    public CoordinatorLayoutAttributeCollector() {
        super(CoordinatorLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(CoordinatorLayout view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute<>("StatusBarBackground", view.getStatusBarBackground()));
        return attributes;
    }
}