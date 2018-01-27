package com.willowtreeapps.hyperion.attr.collectors;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.Switch;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.MutableBooleanViewAttribute;
import com.willowtreeapps.hyperion.attr.MutableStringViewAttribute;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.plugin.v1.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class SwitchAttributeCollector extends TypedAttributeCollector<Switch> {

    public SwitchAttributeCollector() {
        super(Switch.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(final Switch view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new MutableStringViewAttribute("TextOn", view.getTextOn()) {
            @Override
            protected void mutate(CharSequence value) {
                view.setTextOn(value);
            }
        });
        attributes.add(new MutableStringViewAttribute("TextOff", view.getTextOff()) {
            @Override
            protected void mutate(CharSequence value) {
                view.setTextOff(value);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attributes.add(new MutableBooleanViewAttribute("ShowText", view.getShowText()) {
                @Override
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                protected void mutate(Boolean value) {
                    view.setShowText(value);
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            attributes.add(new ViewAttribute<>("SwitchPadding", attributeTranslator.translatePx(view.getSwitchPadding())));
            attributes.add(new ViewAttribute<>("SwitchMinWidth", attributeTranslator.translatePx(view.getSwitchMinWidth())));
            attributes.add(new ViewAttribute<>("ThumbTextPadding", attributeTranslator.translatePx(view.getThumbTextPadding())));
            attributes.add(new ViewAttribute<Void>("ThumbDrawable", view.getThumbDrawable()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            attributes.add(Collectors.createColorAttribute(view, "ThumbTint", view.getThumbTintList()));
            attributes.add(new ViewAttribute<>("ThumbTintMode", new PorterDuffModeValue(view.getThumbTintMode())));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            attributes.add(new ViewAttribute<Void>("TrackDrawable", view.getTrackDrawable()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            attributes.add(Collectors.createColorAttribute(view, "TrackTint", view.getTrackTintList()));
            attributes.add(new ViewAttribute<>("TrackTintMode", new PorterDuffModeValue(view.getTrackTintMode())));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attributes.add(new MutableBooleanViewAttribute("SplitTrack", view.getSplitTrack()) {
                @Override
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                protected void mutate(Boolean value) {
                    view.setSplitTrack(value);
                }
            });
        }


        return attributes;
    }
}