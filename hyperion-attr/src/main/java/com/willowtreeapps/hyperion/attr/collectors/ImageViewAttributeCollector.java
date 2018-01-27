package com.willowtreeapps.hyperion.attr.collectors;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class ImageViewAttributeCollector extends TypedAttributeCollector<ImageView> {

    public ImageViewAttributeCollector() {
        super(ImageView.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(ImageView view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute("Image", view.getDrawable()));
        attributes.add(new ViewAttribute<>("ScaleType", view.getScaleType().toString()));

        return attributes;
    }
}