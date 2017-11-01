package com.willowtreeapps.hyperion.attr.design;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.AttributeTranslator;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.attr.ViewAttribute;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class AppBarLayoutAttributeCollector extends TypedAttributeCollector<AppBarLayout> {

    protected AppBarLayoutAttributeCollector() {
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