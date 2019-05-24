package com.willowtreeapps.hyperion.attr.collectors;

import androidx.annotation.NonNull;
import androidx.core.widget.CompoundButtonCompat;
import android.widget.CompoundButton;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.MutableBooleanViewAttribute;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class CompoundButtonAttributeCollector extends TypedAttributeCollector<CompoundButton> {

    public CompoundButtonAttributeCollector() {
        super(CompoundButton.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(final CompoundButton view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();

        attributes.add(new MutableBooleanViewAttribute("Checked", view.isChecked()) {
            @Override
            protected void mutate(Boolean value) {
                view.setChecked(value);
            }
        });
        attributes.add(new ViewAttribute<Void>("ButtonDrawable",
                CompoundButtonCompat.getButtonDrawable(view)));

        return attributes;
    }
}