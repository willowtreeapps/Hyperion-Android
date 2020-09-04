package com.willowtreeapps.hyperion.attr.collectors;

import android.os.Build;
import androidx.annotation.NonNull;
import android.widget.RelativeLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class RelativeLayoutAttributeCollector extends TypedAttributeCollector<RelativeLayout> {

    public RelativeLayoutAttributeCollector() {
        super(RelativeLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(RelativeLayout view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            attributes.add(new ViewAttribute<>("Gravity", new GravityValue(view.getGravity())));
        }
        return attributes;
    }
}