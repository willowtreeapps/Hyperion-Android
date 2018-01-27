package com.willowtreeapps.hyperion.attr.support.v4;

import android.support.annotation.NonNull;
import android.support.v4.widget.SlidingPaneLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class SlidingPaneLayoutAttributeCollector extends TypedAttributeCollector<SlidingPaneLayout> {

    public SlidingPaneLayoutAttributeCollector() {
        super(SlidingPaneLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(SlidingPaneLayout view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        // TODO fill
        return attributes;
    }
}