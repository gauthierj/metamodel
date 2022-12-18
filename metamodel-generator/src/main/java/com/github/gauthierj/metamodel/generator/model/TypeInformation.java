package com.github.gauthierj.metamodel.generator.model;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@ImmutableStyle
public interface TypeInformation {

    @Value.Parameter(order = 0)
    String packageName();
    @Value.Parameter(order = 0)
    String className();
    List<PropertyInformation> properties();

    default String fullyQualifiedName() {
        return this.packageName() + "." + this.className();
    }

    default boolean isInSamePackage(TypeInformation typeInformation) {
        return this.packageName().equals(typeInformation.packageName());
    }

    default String generatedClassName() {
        return "_" + this.className();
    }

    default String fullyQualifiedGeneratedClassName() {
        return this.packageName() + "." + this.generatedClassName();
    }
}
