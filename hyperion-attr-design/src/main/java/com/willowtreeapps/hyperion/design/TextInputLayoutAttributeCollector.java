package com.willowtreeapps.hyperion.design;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.TypedAttributeCollector;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class TextInputLayoutAttributeCollector extends TypedAttributeCollector<TextInputLayout> {

    protected TextInputLayoutAttributeCollector() {
        super(TextInputLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(TextInputLayout view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute<>("PasswordToggleDrawable",
                view.getPasswordVisibilityToggleDrawable()));
        attributes.add(new ViewAttribute<>("PasswordToggleContentDisc",
                nonNull(view.getPasswordVisibilityToggleContentDescription())));
        attributes.add(new ViewAttribute<>("CounterMaxLength", view.getCounterMaxLength()));
        attributes.add(new ViewAttribute<>("Hint", nonNull(view.getHint())));
        attributes.add(new ViewAttribute<>("CounterEnabled", view.isCounterEnabled()));
        attributes.add(new ViewAttribute<>("ErrorEnabled", view.isErrorEnabled()));
        attributes.add(new ViewAttribute<>("HintAnimationEnabled", view.isHintAnimationEnabled()));
        attributes.add(new ViewAttribute<>("HintEnabled", view.isHintEnabled()));
        attributes.add(new ViewAttribute<>("PasswordToggleEnabled",
                view.isPasswordVisibilityToggleEnabled()));
        return attributes;
    }
}