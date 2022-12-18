package com.github.gauthierj.metamodel.processor.resolver;

import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;
import com.github.gauthierj.metamodel.generator.model.PropertyInformation;
import com.github.gauthierj.metamodel.generator.model.TypeInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MutableTypeInformation implements TypeInformation {

    private final String packageName;
    private final String className;

    private final PropertyAccessMode propertyAccessMode;
    private final List<PropertyInformation> propertyInformations = new ArrayList<>();

    private MutableTypeInformation(String packageName,
                                   String className,
                                   PropertyAccessMode propertyAccessMode) {
        this.packageName = packageName;
        this.className = className;
        this.propertyAccessMode = propertyAccessMode;
    }

    public static MutableTypeInformation of(String packageName,
                                            String className,
                                            PropertyAccessMode propertyAccessMode) {
        return new MutableTypeInformation(packageName, className, propertyAccessMode);
    }

    @Override
    public String packageName() {
        return packageName;
    }

    @Override
    public String className() {
        return className;
    }

    public PropertyAccessMode propertyAccessMode() {
        return propertyAccessMode;
    }
    @Override
    public List<PropertyInformation> properties() {
        return List.copyOf(propertyInformations);
    }

    public boolean hasUnresolvedPropertyInformation() {
        return propertyInformations.stream()
                .anyMatch(propertyInformation -> propertyInformation instanceof UnresolvedTypePropertyInformation);
    }

    public MutableTypeInformation withProperties(Collection<PropertyInformation> properties) {
        this.propertyInformations.clear();
        this.propertyInformations.addAll(properties);
        return this;
    }
}
