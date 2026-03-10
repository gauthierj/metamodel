package com.github.gauthierj.metamodel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks a type for metamodel class generation.
 *
 * <p>When placed on a class or interface, the annotation processor generates a corresponding
 * {@code _ClassName} class in the same package. The generated class provides type-safe,
 * refactor-safe access to property names and paths, useful for example in MongoDB criteria
 * queries or validation error reporting.
 *
 * <p>Example:
 * <pre>{@code
 * @Model
 * public class Person {
 *     private String firstName;
 *     private Address address;
 * }
 *
 * // Generated: _Person.model().firstName()         -> "firstName"
 * //            _Person.model().address().street()  -> "address.street"
 * }</pre>
 *
 * <p>When placed on a field or method, it overrides the access mode and getter pattern
 * configured on the enclosing type for that specific property.
 *
 * @see Property
 * @see PropertyAccessMode
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface Model {

    /**
     * Default regex pattern used to identify getter methods.
     * Matches methods named {@code getXxx} or {@code isXxx}.
     */
    String DEFAULT_GETTER_PATTERN = "^(?:is|get)([A-Z][a-zA-Z0-9_$]*)$";

    /**
     * Sentinel value for {@link #generatedClassName()} indicating that the default
     * naming convention ({@code _ClassName}) should be used.
     */
    String DEFAULT_GENERATED_CLASS_NAME = "##default";

    /**
     * Default property access mode ({@link PropertyAccessMode#FIELD}).
     */
    PropertyAccessMode DEFAULT_ACCESS_MODE = PropertyAccessMode.FIELD;

    /**
     * Controls whether properties are discovered from fields or getter methods.
     *
     * <p>Defaults to {@link PropertyAccessMode#FIELD}.
     *
     * @return the property access mode
     */
    PropertyAccessMode accessMode() default PropertyAccessMode.FIELD;

    /**
     * Regex pattern used to identify getter methods when {@link #accessMode()} is
     * {@link PropertyAccessMode#GETTER}.
     *
     * <p>The first capture group must match the property name with an uppercase first letter,
     * which is then decapitalized to form the property name. Defaults to
     * {@link #DEFAULT_GETTER_PATTERN}, which matches {@code getXxx} and {@code isXxx}.
     *
     * @return the getter detection pattern
     */
    String getterPattern() default DEFAULT_GETTER_PATTERN;

    /**
     * Custom name for the generated metamodel class.
     *
     * <p>When set to {@link #DEFAULT_GENERATED_CLASS_NAME} (the default), the generated class
     * is named {@code _ClassName} in the same package as the annotated type. Set this attribute
     * to override that name.
     *
     * @return the generated class name, or {@link #DEFAULT_GENERATED_CLASS_NAME} for the default
     */
    String generatedClassName() default DEFAULT_GENERATED_CLASS_NAME;
}
