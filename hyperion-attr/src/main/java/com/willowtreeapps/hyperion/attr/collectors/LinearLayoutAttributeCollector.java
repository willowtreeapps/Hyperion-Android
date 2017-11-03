package com.willowtreeapps.hyperion.attr.collectors;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.LinearLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.AttributeValue;
import com.willowtreeapps.hyperion.attr.MutableBooleanViewAttribute;
import com.willowtreeapps.hyperion.attr.MutableStringViewAttribute;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class LinearLayoutAttributeCollector extends TypedAttributeCollector<LinearLayout> {

    public LinearLayoutAttributeCollector() {
        super(LinearLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(final LinearLayout view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();

        attributes.add(new ViewAttribute<>("Orientation", new OrientationValue(view.getOrientation())));
        attributes.add(new ViewAttribute<>("WeightSum", view.getWeightSum()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            attributes.add(new ViewAttribute<>("Gravity", new GravityValue(view.getGravity())));
        }

        attributes.add(new ViewAttribute<>("ShowDividers", new DividerModeValue(view.getShowDividers())));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            attributes.add(new ViewAttribute<Void>("Divider", view.getDividerDrawable()));
        }

        attributes.add(new ViewAttribute<>("DividerPadding", attributeTranslator.translatePx(view.getDividerPadding())));

        attributes.add(new MutableBooleanViewAttribute("MeasureWithLargestChild", view.isMeasureWithLargestChildEnabled()) {
            @Override
            protected void mutate(Boolean value) throws Exception {
                view.setMeasureWithLargestChildEnabled(value);
            }
        });

        attributes.add(new MutableBooleanViewAttribute("BaselineAligned", view.isBaselineAligned()) {
            @Override
            protected void mutate(Boolean value) throws Exception {
                view.setBaselineAligned(value);
            }
        });

        attributes.add(new MutableStringViewAttribute(
                "BaselineAlignedChildIndex",
                String.valueOf(view.getBaselineAlignedChildIndex())) {
            @Override
            protected void mutate(CharSequence value) throws Exception {
                int index = Integer.parseInt(value.toString());
                view.setBaselineAlignedChildIndex(index);
            }
        });

        return attributes;
    }

    private static class OrientationValue implements AttributeValue {

        @LinearLayoutCompat.OrientationMode
        private final int mode;

        private OrientationValue(@LinearLayoutCompat.OrientationMode int mode) {
            this.mode = mode;
        }

        @Override
        public CharSequence getDisplayValue() {
            switch (mode) {
                case LinearLayout.HORIZONTAL:
                    return "Horizontal";
                case LinearLayout.VERTICAL:
                    return "Vertical";
                default:
                    return "Unknown";
            }
        }
    }

    private static class DividerModeValue implements AttributeValue {

        @LinearLayoutCompat.DividerMode
        private final int mode;

        private DividerModeValue(@LinearLayoutCompat.DividerMode int mode) {
            this.mode = mode;
        }

        @Override
        public CharSequence getDisplayValue() {
            switch (mode) {
                case LinearLayout.SHOW_DIVIDER_NONE:
                    return "None";
                case LinearLayout.SHOW_DIVIDER_BEGINNING:
                    return "Beginning";
                case LinearLayout.SHOW_DIVIDER_MIDDLE:
                    return "Middle";
                case LinearLayout.SHOW_DIVIDER_END:
                    return "End";
                default:
                    return "Unknown";
            }
        }
    }
}