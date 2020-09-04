package com.willowtreeapps.hyperion.attr.support.v7;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ActionMenuView;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class ActionMenuViewAttributeCollector extends TypedAttributeCollector<ActionMenuView> {

    public ActionMenuViewAttributeCollector() {
        super(ActionMenuView.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(ActionMenuView view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        // TODO fill
        return attributes;
    }
}