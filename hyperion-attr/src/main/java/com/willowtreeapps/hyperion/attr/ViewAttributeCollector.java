package com.willowtreeapps.hyperion.attr;

import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;


@AutoService(TypedAttributeCollector.class)
public class ViewAttributeCollector extends TypedAttributeCollector<View> {

    public ViewAttributeCollector() {
        super(View.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(View view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();

        Rect rect = new Rect();
        String value;

        attributes.add(new ViewAttribute<>("Id", new IdValue(view.getResources(), view.getId())));

        value = attributeTranslator.translatePx(view.getHeight());
        attributes.add(new ViewAttribute<>("Height", value));

        value = attributeTranslator.translatePx(view.getWidth());
        attributes.add(new ViewAttribute<>("Width", value));

        view.getGlobalVisibleRect(rect);

        attributes.add(new ViewAttribute<>("GlobalLeft", String.valueOf(rect.left)));
        attributes.add(new ViewAttribute<>("GlobalTop", String.valueOf(rect.top)));
        attributes.add(new ViewAttribute<>("GlobalRight", String.valueOf(rect.right)));
        attributes.add(new ViewAttribute<>("GlobalBottom", String.valueOf(rect.bottom)));

        view.getLocalVisibleRect(rect);

        attributes.add(new ViewAttribute<>("LocalLeft", String.valueOf(rect.left)));
        attributes.add(new ViewAttribute<>("LocalTop", String.valueOf(rect.top)));
        attributes.add(new ViewAttribute<>("LocalRight", String.valueOf(rect.right)));
        attributes.add(new ViewAttribute<>("LocalBottom", String.valueOf(rect.bottom)));

        attributes.add(new ViewAttribute<>("HasFocus", view.hasFocus()));
        attributes.add(new ViewAttribute<>("HasFocusable", view.hasFocusable()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            attributes.add(new ViewAttribute<>("HasTransientState", view.hasTransientState()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            attributes.add(new ViewAttribute<>("ForegroundGravity",
                    new GravityValue(view.getForegroundGravity())));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attributes.add(new ViewAttribute<>("TransitionName", view.getTransitionName()));
            attributes.add(new ViewAttribute<>("Elevation", view.getElevation()));
        }

        return attributes;
    }

}