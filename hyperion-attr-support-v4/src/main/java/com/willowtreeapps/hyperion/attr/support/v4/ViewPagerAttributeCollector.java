package com.willowtreeapps.hyperion.attr.support.v4;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class ViewPagerAttributeCollector extends TypedAttributeCollector<ViewPager> {

    public ViewPagerAttributeCollector() {
        super(ViewPager.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(ViewPager view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        // TODO fill
        return attributes;
    }
}