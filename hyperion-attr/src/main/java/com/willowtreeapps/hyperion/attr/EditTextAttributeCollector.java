package com.willowtreeapps.hyperion.attr;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class EditTextAttributeCollector extends TypedAttributeCollector<EditText> {

    public EditTextAttributeCollector() {
        super(EditText.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(EditText view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();

        attributes.add(new ViewAttribute<>("FreezesText", view.getFreezesText()));

        return attributes;
    }
}