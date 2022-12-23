package com.github.gauthierj.metamodel.processor.resolver;

import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;
import com.github.gauthierj.metamodel.generator.model.PropertyInformation;
import com.github.gauthierj.metamodel.generator.model.TypeInformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeInformationImpl implements TypeInformation {

    private final String packageName;
    private final String className;
    private final String generatedClassName;
    private final PropertyAccessMode propertyAccessMode;
    private final List<PropertyInformation> propertyInformations = new ArrayList<>();

    private TypeInformationImpl(String packageName,
                                String className,
                                String generatedClassName,
                                PropertyAccessMode propertyAccessMode) {
        this.packageName = packageName;
        this.className = className;
        this.generatedClassName = generatedClassName;
        this.propertyAccessMode = propertyAccessMode;
    }

    public static TypeInformationImpl of(String packageName,
                                         String className,
                                         String generatedClassName,
                                         PropertyAccessMode propertyAccessMode) {
        return new TypeInformationImpl(packageName, className, generatedClassName, propertyAccessMode);
    }

    @Override
    public String packageName() {
        return packageName;
    }

    @Override
    public String className() {
        return className;
    }

    @Override
    public List<PropertyInformation> properties() {
        return List.copyOf(propertyInformations);
    }

    @Override
    public String generatedClassName() {
        return generatedClassName;
    }

    public PropertyAccessMode propertyAccessMode() {
        return propertyAccessMode;
    }

    public boolean hasUnresolvedPropertyInformation() {
        return propertyInformations.stream()
                .anyMatch(propertyInformation -> propertyInformation instanceof UnresolvedTypePropertyInformation);
    }

    public TypeInformationImpl withProperties(Collection<PropertyInformation> properties) {
        this.propertyInformations.clear();
        this.propertyInformations.addAll(properties);
        return this;
    }
}
