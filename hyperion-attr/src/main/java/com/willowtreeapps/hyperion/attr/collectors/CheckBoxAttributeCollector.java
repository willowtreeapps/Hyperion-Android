package com.willowtreeapps.hyperion.attr.collectors;

import android.support.annotation.NonNull;
import android.widget.CheckBox;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.Collections;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class CheckBoxAttributeCollector extends TypedAttributeCollector<CheckBox> {

    public CheckBoxAttributeCollector() {
        super(CheckBox.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(CheckBox view, AttributeTranslator attributeTranslator) {
        return Collections.emptyList();
    }
}