package com.github.gauthierj.metamodel.generator.model;

import org.immutables.value.Value;

/**
 * Metadata for a simple (scalar) property.
 *
 * <p>A simple property is one whose type is not itself a metamodel-annotated type —
 * for example, a {@code String}, a numeric type, a date, or any other leaf value.
 * The generated metamodel produces a {@code String}-returning method for this property,
 * which evaluates to the fully-qualified property path at runtime.
 *
 * @see PropertyInformation
 * @see StructuredPropertyInformation
 */
@Value.Immutable
@ImmutableStyle
public interface SimplePropertyInformation extends PropertyInformation {

    /**
     * {@inheritDoc}
     */
    @Override
    @Value.Parameter(order = 0)
    String name();

    /**
     * {@inheritDoc}
     */
    @Override
    @Value.Parameter(order = 1)
    String logicalName();
}
