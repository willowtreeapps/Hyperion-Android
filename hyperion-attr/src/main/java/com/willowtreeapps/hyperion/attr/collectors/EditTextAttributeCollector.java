package com.willowtreeapps.hyperion.attr.collectors;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.MutableBooleanViewAttribute;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class EditTextAttributeCollector extends TypedAttributeCollector<EditText> {

    public EditTextAttributeCollector() {
        super(EditText.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(final EditText view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();

        attributes.add(new MutableBooleanViewAttribute("FreezesText", view.getFreezesText()) {
            @Override
            protected void mutate(Boolean value) {
                view.setFreezesText(value);
            }
        });

        return attributes;
    }
}