package com.willowtreeapps.hyperion.attr.design;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.attr.collectors.GravityValue;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.attr.collectors.TypefaceValue;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class CollapsingToolbarLayoutAttributeCollector extends TypedAttributeCollector<CollapsingToolbarLayout> {

    public CollapsingToolbarLayoutAttributeCollector() {
        super(CollapsingToolbarLayout.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(CollapsingToolbarLayout view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute<>("Title", view.getTitle()));
        attributes.add(new ViewAttribute<>("TitleEnabled", view.isTitleEnabled()));
        attributes.add(new ViewAttribute<>("TitleGravityCollapsed", new GravityValue(view.getCollapsedTitleGravity())));
        attributes.add(new ViewAttribute<>("TitleTypefaceCollapsed", new TypefaceValue(view.getCollapsedTitleTypeface())));
        attributes.add(new ViewAttribute<>("TitleGravityExpanded", new GravityValue(view.getExpandedTitleGravity())));
        attributes.add(new ViewAttribute<>("TitleTypefaceExpanded", new TypefaceValue(view.getExpandedTitleTypeface())));
        attributes.add(new ViewAttribute<>("TitleMarginStartExpanded",
                attributeTranslator.translatePx(view.getExpandedTitleMarginStart())));
        attributes.add(new ViewAttribute<>("TitleMarginTopExpanded",
                attributeTranslator.translatePx(view.getExpandedTitleMarginTop())));
        attributes.add(new ViewAttribute<>("TitleMarginEndExpanded",
                attributeTranslator.translatePx(view.getExpandedTitleMarginEnd())));
        attributes.add(new ViewAttribute<>("TitleMarginBottomExpanded",
                attributeTranslator.translatePx(view.getExpandedTitleMarginBottom())));
        attributes.add(new ViewAttribute<>("ScrimVisibleHeightTrigger", view.getScrimVisibleHeightTrigger()));
        attributes.add(new ViewAttribute<>("ScrimAnimationDuration", view.getScrimAnimationDuration()));
        attributes.add(new ViewAttribute<Void>("ContentScrim", view.getContentScrim()));
        attributes.add(new ViewAttribute<Void>("StatusBarScrim", view.getStatusBarScrim()));
        return attributes;
    }
}