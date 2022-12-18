package com.github.gauthierj.metamodel.generator.model;

import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface SimplePropertyInformation extends PropertyInformation {

    @Override
    @Value.Parameter(order = 0)
    String name();

    @Override
    @Value.Parameter(order = 1)
    String logicalName();
}
