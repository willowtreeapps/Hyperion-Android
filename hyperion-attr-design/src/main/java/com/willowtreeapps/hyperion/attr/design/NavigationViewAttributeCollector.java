package com.willowtreeapps.hyperion.attr.design;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.Collectors;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class NavigationViewAttributeCollector extends TypedAttributeCollector<NavigationView> {

    public NavigationViewAttributeCollector() {
        super(NavigationView.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(NavigationView view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute<>("HeaderCount", view.getHeaderCount()));
        attributes.add(Collectors.createColorAttribute(view, "ItemTextColor", view.getItemTextColor()));
        attributes.add(Collectors.createColorAttribute(view, "ItemIconTint", view.getItemIconTintList()));
        attributes.add(new ViewAttribute<Void>("ItemBackground", view.getItemBackground()));
        return attributes;
    }

}
