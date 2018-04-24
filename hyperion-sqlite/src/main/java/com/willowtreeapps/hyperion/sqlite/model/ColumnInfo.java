package com.willowtreeapps.hyperion.sqlite.model;

public class ColumnInfo {

    public final String name;
    public final String type;
    public final boolean nullable;
    public final String defaultValue;
    public final boolean isPrimaryKey;

    public ColumnInfo(String name, String type, boolean nullable, String defaultValue,
                      boolean isPrimaryKey) {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.defaultValue = defaultValue;
        this.isPrimaryKey = isPrimaryKey;
    }


}
