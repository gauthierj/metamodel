package com.github.gauthierj.metamodel.generator.model;

import org.immutables.value.Value;

/**
 * Metadata for a structured (nested object) property.
 *
 * <p>A structured property is one whose type is itself a metamodel-annotated type.
 * The generated metamodel produces a method that returns a sub-metamodel instance
 * rooted at this property's path, enabling further navigation into nested properties.
 *
 * <p>For example, if {@code Person} has an {@code Address address} field and {@code Address}
 * is also annotated with {@code @Model}, then:
 * <pre>{@code
 * _Person.model().address()          // returns _Address rooted at "address"
 * _Person.model().address().street() // -> "address.street"
 * }</pre>
 *
 * @see PropertyInformation
 * @see SimplePropertyInformation
 */
@Value.Immutable
@ImmutableStyle
public interface StructuredPropertyInformation extends PropertyInformation {

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
     * Returns the {@link TypeInformation} of the nested type.
     *
     * <p>Used during code generation to determine which generated metamodel class
     * to instantiate for this property.
     *
     * @return the type information of the nested type
     */
    @Value.Parameter(order = 2)
    TypeInformation typeInformation();
}
