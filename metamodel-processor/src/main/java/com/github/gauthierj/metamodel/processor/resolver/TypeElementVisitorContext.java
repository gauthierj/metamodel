package com.github.gauthierj.metamodel.processor.resolver;

import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;

import java.util.Map;

public class TypeElementVisitorContext {

    private final Map<TypeInformationKey, MutableTypeInformation> resolvedTypes;
    private final PropertyAccessMode propertyAccessMode;
    private final String getterPattern;

    private TypeElementVisitorContext(Map<TypeInformationKey, MutableTypeInformation> resolvedTypes,
                                      PropertyAccessMode propertyAccessMode,
                                      String getterPattern) {
        this.propertyAccessMode = propertyAccessMode;
        this.resolvedTypes = Map.copyOf(resolvedTypes);
        this.getterPattern = getterPattern;
    }

    public static TypeElementVisitorContext of(Map<TypeInformationKey, MutableTypeInformation> resolvedTypes,
                                               PropertyAccessMode propertyAccessMode,
                                               String getterPattern) {
        return new TypeElementVisitorContext(resolvedTypes, propertyAccessMode, getterPattern);
    }

    public Map<TypeInformationKey, MutableTypeInformation> getResolvedTypes() {
        return resolvedTypes;
    }

    public String getGetterPattern() {
        return getterPattern;
    }

    public PropertyAccessMode propertyAccessMode() {
        return propertyAccessMode;
    }
}
