package com.github.gauthierj.metamodel.processor.resolver;

import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;
import com.github.gauthierj.metamodel.generator.model.PropertyInformation;

public class UnresolvedTypePropertyInformation implements PropertyInformation {

    private final String type;
    private final String name;
    private final String logicalName;
    private final PropertyAccessMode propertyAccesMode;
    private final String getterPattern;

    private UnresolvedTypePropertyInformation(String type,
                                              String name,
                                              String logicalName,
                                              PropertyAccessMode propertyAccesMode,
                                              String getterPattern) {
        this.type = type;
        this.name = name;
        this.logicalName = logicalName;
        this.propertyAccesMode = propertyAccesMode;
        this.getterPattern = getterPattern;
    }

    public String type() {
        return type;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String logicalName() {
        return logicalName;
    }

    public PropertyAccessMode propertyAccessMode() {
        return propertyAccesMode;
    }

    public String getterPattern() {
        return getterPattern;
    }

    public static PropertyInformation of(String type,
                                         String name,
                                         String logicalName,
                                         PropertyAccessMode propertyAccessMode,
                                         String getterPattern) {
        return new UnresolvedTypePropertyInformation(type, name, logicalName, propertyAccessMode, getterPattern);
    }

    @Override
    public String toString() {
        return "UnresolvedTypePropertyInformation{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnresolvedTypePropertyInformation)) return false;

        UnresolvedTypePropertyInformation that = (UnresolvedTypePropertyInformation) o;

        if (!type.equals(that.type)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
