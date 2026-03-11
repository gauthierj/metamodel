package com.github.gauthierj.metamodel.generator.model;

import org.immutables.value.Value;

/**
 * Metadata for a property whose type is not supported by the metamodel generator.
 *
 * <p>Properties of unsupported types — such as collections, maps, or types that are
 * neither simple scalars nor metamodel-annotated types — are represented by this class.
 * The generator skips these properties and emits a compile-time warning indicating the
 * unsupported type.
 *
 * <p>Extension authors implementing {@link com.github.gauthierj.metamodel.generator.PropertyInformationHandler}
 * can choose to handle these properties by checking for this type in their {@code supports()} method.
 *
 * @see PropertyInformation
 */
@Value.Immutable
@ImmutableStyle
public interface UnsupportedPropertyInformation extends PropertyInformation {

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

    /**
     * Returns the fully-qualified type name of the unsupported property.
     *
     * @return the fully-qualified type name
     */
    @Value.Parameter(order = 2)
    String type();
}
