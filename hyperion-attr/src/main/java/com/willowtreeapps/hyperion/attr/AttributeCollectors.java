package com.willowtreeapps.hyperion.attr;

import com.willowtreeapps.hyperion.attr.collectors.TypedAttributeCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

final class AttributeCollectors {

    private static List<TypedAttributeCollector> collectors;

    static List<TypedAttributeCollector> get() {
        synchronized (AttributeCollectors.class) {
            if (collectors == null) {
                synchronized (AttributeCollectors.class) {
                    collectors = load();
                }
            }
        }
        return collectors;
    }

    private static List<TypedAttributeCollector> load() {
        ServiceLoader<TypedAttributeCollector> loader =
                ServiceLoader.load(TypedAttributeCollector.class);
        List<TypedAttributeCollector> collectors = new ArrayList<>(10);
        for (TypedAttributeCollector collector : loader) {
            collectors.add(collector);
        }
        return collectors;
    }

    AttributeCollectors() {
        throw new AssertionError("No instances.");
    }

}