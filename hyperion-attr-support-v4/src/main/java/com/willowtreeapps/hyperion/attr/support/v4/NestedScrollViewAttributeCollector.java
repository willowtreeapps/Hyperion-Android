package com.willowtreeapps.hyperion.attr.support.v4;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class NestedScrollViewAttributeCollector extends TypedAttributeCollector<NestedScrollView> {

    public NestedScrollViewAttributeCollector() {
        super(NestedScrollView.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(NestedScrollView view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        // TODO fill
        return attributes;
    }
}