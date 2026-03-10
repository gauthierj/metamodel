package com.github.gauthierj.metamodel.generator.model;

import org.immutables.value.Value;

import java.util.List;

/**
 * Immutable metadata describing a type for which a metamodel class will be generated.
 *
 * <p>This interface is part of the extension SPI. Implementations of
 * {@link com.github.gauthierj.metamodel.processor.ModelAnnotationProvider} and
 * {@link com.github.gauthierj.metamodel.generator.PropertyInformationHandler} receive
 * {@code TypeInformation} instances to inspect the type being processed and guide
 * code generation.
 *
 * @see PropertyInformation
 */
@Value.Immutable
@ImmutableStyle
public interface TypeInformation {

    /**
     * Returns the package name of the annotated type.
     *
     * @return the package name, e.g. {@code "com.example.model"}
     */
    @Value.Parameter(order = 0)
    String packageName();

    /**
     * Returns the simple class name of the annotated type.
     *
     * @return the simple class name, e.g. {@code "Person"}
     */
    @Value.Parameter(order = 1)
    String className();

    /**
     * Returns the properties discovered on the annotated type.
     *
     * <p>Each entry is one of {@link SimplePropertyInformation}, {@link StructuredPropertyInformation},
     * or {@link UnsupportedPropertyInformation}.
     *
     * @return the list of properties, in discovery order
     */
    List<PropertyInformation> properties();

    /**
     * Returns the fully-qualified name of the annotated type.
     *
     * @return {@code packageName() + "." + className()}
     */
    default String fullyQualifiedName() {
        return this.packageName() + "." + this.className();
    }

    /**
     * Returns {@code true} if the given type resides in the same package as this type.
     *
     * @param typeInformation the type to compare against
     * @return {@code true} if both types share the same package
     */
    default boolean isInSamePackage(TypeInformation typeInformation) {
        return this.packageName().equals(typeInformation.packageName());
    }

    /**
     * Returns the simple name of the generated metamodel class.
     *
     * <p>Defaults to {@code "_" + className()}. Can be overridden via
     * {@link com.github.gauthierj.metamodel.annotation.Model#generatedClassName()}.
     *
     * @return the simple name of the generated class, e.g. {@code "_Person"}
     */
    @Value.Default
    default String generatedClassName() {
        return "_" + this.className();
    }

    /**
     * Returns the fully-qualified name of the generated metamodel class.
     *
     * @return {@code packageName() + "." + generatedClassName()}
     */
    default String fullyQualifiedGeneratedClassName() {
        return this.packageName() + "." + this.generatedClassName();
    }
}
