package com.willowtreeapps.hyperion.attr.support.v4;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class SwipeRefreshLayoutAttributeCollector extends TypedAttributeCollector<SwipeRefreshLayout> {

    public SwipeRefreshLayoutAttributeCollector() {
        super(SwipeRefreshLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(SwipeRefreshLayout view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        // TODO fill
        return attributes;
    }
}