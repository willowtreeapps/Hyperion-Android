package com.willowtreeapps.hyperion.attr;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
    public List<ViewAttribute> collect(final ViewGroup view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute<>("ChildCount", view.getChildCount()));
        attributes.add(new MutableBooleanViewAttribute("MotionEventSplittingEnabled",
                view.isMotionEventSplittingEnabled()) {
            @Override
            protected void mutate(Boolean value) {
                view.setMotionEventSplittingEnabled(value);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            attributes.add(new ViewAttribute<>("LayoutMode",
                    new LayoutModeValue(view.getLayoutMode())));
            attributes.add(new MutableBooleanViewAttribute("ClipChildren", view.getClipChildren()) {
                @Override
                protected void mutate(Boolean value) {
                    view.setClipChildren(value);
                }
            });
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attributes.add(new MutableBooleanViewAttribute("ClipToPadding", view.getClipToPadding()) {
                @Override
                protected void mutate(Boolean value) {
                    view.setClipToPadding(value);
                }
            });
            attributes.add(new ViewAttribute<>("TouchscreenBlocksFocus",
                    view.getTouchscreenBlocksFocus()));
            attributes.add(new MutableBooleanViewAttribute("IsTransitionGroup", view.isTransitionGroup()) {
                @Override
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                protected void mutate(Boolean value) {
                    view.setTransitionGroup(value);
                }
            });
        }
        return attributes;
    }
}