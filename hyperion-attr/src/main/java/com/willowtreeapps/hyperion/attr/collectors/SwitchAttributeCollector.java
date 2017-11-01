package com.willowtreeapps.hyperion.attr.collectors;

import android.support.annotation.NonNull;
import android.widget.Switch;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.Collections;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class SwitchAttributeCollector extends TypedAttributeCollector<Switch> {

    public SwitchAttributeCollector() {
        super(Switch.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(Switch view, AttributeTranslator attributeTranslator) {
        return Collections.emptyList();
    }
}