package com.willowtreeapps.hyperion.attr.design;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.Collectors;
import com.willowtreeapps.hyperion.attr.collectors.ResourceValue;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class BottomNavigationViewAttributeCollector extends TypedAttributeCollector<BottomNavigationView> {

    public BottomNavigationViewAttributeCollector() {
        super(BottomNavigationView.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(BottomNavigationView view, AttributeTranslator attributeTranslator) {
        final Context context = view.getContext();
        final Resources res = context.getResources();
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute<>("MaxItemCount", view.getMaxItemCount()));
        attributes.add(Collectors.createColorAttribute(view, "ItemTint", view.getItemIconTintList()));
        attributes.add(Collectors.createColorAttribute(view, "ItemTextColor", view.getItemTextColor()));
        attributes.add(new ViewAttribute<>("SelectedItemId", new ResourceValue(res, view.getSelectedItemId())));
        attributes.add(new ViewAttribute<>("ItemBackgroundRes", new ResourceValue(res, view.getItemBackgroundResource())));
        attributes.addAll(Collectors.createMenuAttributes(context, view.getMenu()));
        return attributes;
    }
}
