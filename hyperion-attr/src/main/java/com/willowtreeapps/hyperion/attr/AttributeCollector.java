package com.willowtreeapps.hyperion.attr;

import android.view.View;

import com.willowtreeapps.hyperion.core.AttributeTranslator;
import com.willowtreeapps.hyperion.core.PluginScope;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@PluginScope
class AttributeCollector {

    private final AttributeTranslator attributeTranslator;
    private final List<TypedAttributeCollector> typedCollectors;

    @Inject
    AttributeCollector(AttributeTranslator attributeTranslator) {
        this.attributeTranslator = attributeTranslator;
        this.typedCollectors = AttributeCollectors.get();
    }

    @SuppressWarnings("unchecked")
    List<Section<ViewAttribute>> collect(View view) {
        List<Section<ViewAttribute>> sections = new ArrayList<>(12);
        for (TypedAttributeCollector aggregator : typedCollectors) {
            if (aggregator.acceptsType(view.getClass())) {
                List<ViewAttribute> attributes = aggregator.collect(view, attributeTranslator);
                Section<ViewAttribute> section = new Section<>(aggregator.name(), attributes);
                sections.add(section);
            }
        }
        return sections;
    }

}