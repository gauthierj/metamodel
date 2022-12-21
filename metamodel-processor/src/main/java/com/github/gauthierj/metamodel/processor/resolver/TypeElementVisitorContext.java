package com.github.gauthierj.metamodel.processor.resolver;

import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TypeElementVisitorContext {

    private final Map<String, MutableTypeInformation> resolvedTypes = new HashMap<>();
    private final PropertyAccessMode propertyAccessMode;
    private final String getterPattern;

    private TypeElementVisitorContext(Map<String, MutableTypeInformation> resolvedTypes,
                                      PropertyAccessMode propertyAccessMode,
                                      String getterPattern) {
        this.propertyAccessMode = propertyAccessMode;
        Optional.ofNullable(resolvedTypes).ifPresent(this.resolvedTypes::putAll);
        this.getterPattern = getterPattern;
    }

    public static TypeElementVisitorContext of(Map<String, MutableTypeInformation> resolvedTypes,
                                               PropertyAccessMode propertyAccessMode,
                                               String getterPattern) {
        return new TypeElementVisitorContext(resolvedTypes, propertyAccessMode, getterPattern);
    }

    public Map<String, MutableTypeInformation> getResolvedTypes() {
        return resolvedTypes;
    }

    public String getGetterPattern() {
        return getterPattern;
    }

    public PropertyAccessMode propertyAccessMode() {
        return propertyAccessMode;
    }
}
