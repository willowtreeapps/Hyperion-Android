package com.willowtreeapps.hyperion.attr;

import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Aggregates TypedAttributeCollectors using ServiceLoader.
 * <p>
 * For ServiceLoader to find the appropriate Service classes, AttributeCollectors must have
 * access to the class loader, so it is instantiated as a singleton.
 */
final class AttributeCollectors {

    private static AttributeCollectors INSTANCE;
    private List<TypedAttributeCollector> collectors;

    private AttributeCollectors() {
        collectors = load();
    }

    private List<TypedAttributeCollector> load() {
        ServiceLoader<TypedAttributeCollector> loader =
                ServiceLoader.load(TypedAttributeCollector.class, getClass().getClassLoader());
        List<TypedAttributeCollector> collectors = new ArrayList<>(10);
        for (TypedAttributeCollector collector : loader) {
            collectors.add(collector);
        }
        return collectors;
    }

    List<TypedAttributeCollector> getCollectors() {
        return INSTANCE.collectors;
    }

    synchronized static AttributeCollectors getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AttributeCollectors();
        }

        return INSTANCE;
    }

}