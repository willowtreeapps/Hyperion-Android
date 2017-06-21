package com.willowtreeapps.hyperion.attr;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

import static android.view.inputmethod.EditorInfo.*;

@AutoService(TypedAttributeCollector.class)
public class TextViewAttributeCollector extends TypedAttributeCollector<TextView> {

    public TextViewAttributeCollector() {
        super(TextView.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(final TextView view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();

        attributes.add(new MutableStringViewAttribute("Text", view.getText().toString()) {
            @Override
            protected void mutate(CharSequence value) {
                view.setText(value);
            }
        });
        attributes.add(new MutableStringViewAttribute("Hint", view.getHint()) {
            @Override
            protected void mutate(CharSequence value) {
                view.setHint(value);
            }
        });
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
        attributes.add(new ViewAttribute<>("ImeAction", view.getImeActionId()));
        attributes.add(new ViewAttribute<>("ImeActionLabel", view.getImeActionLabel()));
        attributes.add(new ViewAttribute<>("ImeOptions", new ImeOptionsValue(view.getImeOptions())));

        return attributes;
    }

    private static class ImeOptionsValue {

        private final int imeOptions;

        private ImeOptionsValue(int imeOptions) {
            this.imeOptions = imeOptions;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (imeOptions == IME_ACTION_UNSPECIFIED) {
                sb.append("UNSPECIFIED|");
            }
            if ((imeOptions & IME_ACTION_DONE) == IME_ACTION_DONE) {
                sb.append("DONE|");
            }
            if ((imeOptions & IME_ACTION_GO) == IME_ACTION_GO) {
                sb.append("GO|");
            }
            if ((imeOptions & IME_ACTION_NEXT) == IME_ACTION_NEXT) {
                sb.append("NEXT|");
            }
            if ((imeOptions & IME_ACTION_NONE) == IME_ACTION_NONE) {
                sb.append("NONE|");
            }
            if ((imeOptions & IME_ACTION_PREVIOUS) == IME_ACTION_PREVIOUS) {
                sb.append("PREVIOUS|");
            }
            if ((imeOptions & IME_ACTION_SEARCH) == IME_ACTION_SEARCH) {
                sb.append("SEARCH|");
            }
            if ((imeOptions & IME_ACTION_SEND) == IME_ACTION_SEND) {
                sb.append("SEND|");
            }

            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        }
    }
}