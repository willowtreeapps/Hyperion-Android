package com.willowtreeapps.hyperion.attr.design;

import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class AppBarLayoutAttributeCollector extends TypedAttributeCollector<AppBarLayout> {

    public AppBarLayoutAttributeCollector() {
        super(AppBarLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(AppBarLayout view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute<>("TotalScrollRange", view.getTotalScrollRange()));
        return attributes;
    }
}