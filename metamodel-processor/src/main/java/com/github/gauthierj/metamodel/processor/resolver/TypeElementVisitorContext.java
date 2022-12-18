package com.github.gauthierj.metamodel.processor.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TypeElementVisitorContext {

    private final Map<String, MutableTypeInformation> resolvedTypes = new HashMap<>();
    private final String getterPattern;

    private TypeElementVisitorContext(Map<String, MutableTypeInformation> resolvedTypes, String getterPattern) {
        Optional.ofNullable(resolvedTypes).ifPresent(this.resolvedTypes::putAll);
        this.getterPattern = getterPattern;
    }

    public static TypeElementVisitorContext of(Map<String, MutableTypeInformation> resolvedTypes, String getterPattern) {
        return new TypeElementVisitorContext(resolvedTypes, getterPattern);
    }

    public Map<String, MutableTypeInformation> getResolvedTypes() {
        return resolvedTypes;
    }

    public String getGetterPattern() {
        return getterPattern;
    }
}
