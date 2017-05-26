package com.willowtreeapps.hyperion.attr;

import android.os.Build;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class ViewGroupAttributeCollector extends TypedAttributeCollector<ViewGroup> {

    public ViewGroupAttributeCollector() {
        super(ViewGroup.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(ViewGroup view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute<>("ChildCount", view.getChildCount()));
        attributes.add(new ViewAttribute<>("MotionEventSplittingEnabled",
                view.isMotionEventSplittingEnabled()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            attributes.add(new ViewAttribute<>("LayoutMode",
                    new LayoutModeValue(view.getLayoutMode())));
            attributes.add(new ViewAttribute<>("ClipChildren", view.getClipChildren()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attributes.add(new ViewAttribute<>("ClipToPadding", view.getClipToPadding()));
            attributes.add(new ViewAttribute<>("TouchscreenBlocksFocus",
                    view.getTouchscreenBlocksFocus()));
            attributes.add(new ViewAttribute<>("IsTransitionGroup", view.isTransitionGroup()));
        }
        return attributes;
    }
}