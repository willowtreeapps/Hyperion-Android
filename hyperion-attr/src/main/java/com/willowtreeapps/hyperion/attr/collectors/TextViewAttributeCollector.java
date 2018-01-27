package com.willowtreeapps.hyperion.attr.collectors;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.widget.TextView;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.AttributeValue;
import com.willowtreeapps.hyperion.attr.MutableStringViewAttribute;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

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
                attributeTranslator.translatePxToSp((int) view.getTextSize())));
        attributes.add(new ViewAttribute<>("AutoSizeMaxTextSize",
                TextViewCompat.getAutoSizeMaxTextSize(view)));
        attributes.add(new ViewAttribute<>("AutoSizeMinTextSize",
                TextViewCompat.getAutoSizeMinTextSize(view)));
        attributes.add(new ViewAttribute<>("AutoSizeStepGranularity",
                TextViewCompat.getAutoSizeStepGranularity(view)));
        attributes.add(new ViewAttribute<>("Gravity", new GravityValue(view.getGravity())));

        attributes.add(new ViewAttribute<>("ImeAction", view.getImeActionId()));
        attributes.add(new ViewAttribute<>("ImeActionLabel", view.getImeActionLabel()));
        attributes.add(new ViewAttribute<>("ImeOptions", new ImeOptionsValue(view.getImeOptions())));

        attributes.add(new ViewAttribute<>("CompoundPaddingLeft",
                attributeTranslator.translatePx(view.getCompoundPaddingLeft())));
        attributes.add(new ViewAttribute<>("CompoundPaddingTop",
                attributeTranslator.translatePx(view.getCompoundPaddingTop())));
        attributes.add(new ViewAttribute<>("CompoundPaddingRight",
                attributeTranslator.translatePx(view.getCompoundPaddingRight())));
        attributes.add(new ViewAttribute<>("CompoundPaddingBottom",
                attributeTranslator.translatePx(view.getCompoundPaddingBottom())));

        attributes.add(new ViewAttribute<>("CompoundDrawable",
                attributeTranslator.translatePx(view.getCompoundDrawablePadding())));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            attributes.add(Collectors.createColorAttribute(view, "CompoundDrawableTint",
                    view.getCompoundDrawableTintList()));
            attributes.add(new ViewAttribute<>("CompoundDrawableTintMode",
                    new PorterDuffModeValue(view.getCompoundDrawableTintMode())));

        }

        return attributes;
    }

    private static class ImeOptionsValue implements AttributeValue {

        private final int imeOptions;

        private ImeOptionsValue(int imeOptions) {
            this.imeOptions = imeOptions;
        }

        @Override
        public CharSequence getDisplayValue() {
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