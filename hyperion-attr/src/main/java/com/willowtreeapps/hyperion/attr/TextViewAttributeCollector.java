package com.willowtreeapps.hyperion.attr;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class TextViewAttributeCollector extends TypedAttributeCollector<TextView> {

    public TextViewAttributeCollector() {
        super(TextView.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(TextView view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();

        attributes.add(new ViewAttribute<>("Text", view.getText().toString()));
        attributes.add(new ViewAttribute<>("Hint", view.getHint()));
        attributes.add(new ViewAttribute<>("TextColor",
                new ColorValue(view.getCurrentTextColor()),
                new ColorDrawable(view.getCurrentTextColor())));
        attributes.add(new ViewAttribute<>("HintColor",
                new ColorValue(view.getCurrentHintTextColor()),
                new ColorDrawable(view.getCurrentHintTextColor())));
        attributes.add(new ViewAttribute<>("Typeface", view.getTypeface()));
        attributes.add(new ViewAttribute<>("TextSize",
                attributeTranslator.translatePxToSp(view.getTextSize())));
        attributes.add(new ViewAttribute<>("Gravity", new GravityValue(view.getGravity())));

        return attributes;
    }
}