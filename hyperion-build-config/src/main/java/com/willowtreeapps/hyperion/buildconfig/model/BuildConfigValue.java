package com.willowtreeapps.hyperion.buildconfig.model;

public class BuildConfigValue {

    private final String name;
    private final String value;

    public BuildConfigValue(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "BuildConfigValue{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
