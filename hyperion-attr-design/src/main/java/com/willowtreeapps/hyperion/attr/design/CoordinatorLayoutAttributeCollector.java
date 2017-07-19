package com.willowtreeapps.hyperion.attr.design;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.AttributeTranslator;
import com.willowtreeapps.hyperion.attr.TypedAttributeCollector;
import com.willowtreeapps.hyperion.attr.ViewAttribute;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class CoordinatorLayoutAttributeCollector extends TypedAttributeCollector<CoordinatorLayout> {

    protected CoordinatorLayoutAttributeCollector() {
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