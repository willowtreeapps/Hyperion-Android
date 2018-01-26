package com.willowtreeapps.hyperion.attr.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;
import com.willowtreeapps.hyperion.attr.ViewAttribute;
import com.willowtreeapps.hyperion.core.AttributeTranslator;

import java.util.ArrayList;
import java.util.List;

@AutoService(TypedAttributeCollector.class)
public class RecyclerViewAttributeCollector extends TypedAttributeCollector<RecyclerView> {

    public RecyclerViewAttributeCollector() {
        super(RecyclerView.class);
    }

    @NonNull
    @Override
    public List<ViewAttribute> collect(RecyclerView view, AttributeTranslator attributeTranslator) {
        List<ViewAttribute> attributes = new ArrayList<>();
        attributes.add(new ViewAttribute<>("LayoutManager",
                view.getLayoutManager().getClass().getSimpleName()));
        attributes.add(new ViewAttribute<>("Adapter",
                view.getAdapter().getClass().getSimpleName()));
        attributes.add(new ViewAttribute<>("FixedSize", view.hasFixedSize()));
        return attributes;
    }
}