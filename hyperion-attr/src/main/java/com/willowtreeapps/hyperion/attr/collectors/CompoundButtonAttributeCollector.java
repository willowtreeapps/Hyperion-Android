package com.willowtreeapps.hyperion.attr.collectors;

import android.support.annotation.NonNull;
import android.support.v4.widget.CompoundButtonCompat;
import android.widget.CompoundButton;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class CompoundButtonAttributeCollector extends TypedAttributeCollector<CompoundButton> {

    public CompoundButtonAttributeCollector() {
        super(CompoundButton.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(CompoundButton view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();

        attributes.add(new ViewAttribute<>("Checked", view.isChecked()));
        attributes.add(new ViewAttribute<Void>("ButtonDrawable",
                CompoundButtonCompat.getButtonDrawable(view)));

        return attributes;
    }
}