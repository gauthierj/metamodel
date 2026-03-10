package com.github.gauthierj.metamodel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Customizes how a field or method is handled during metamodel generation.
 *
 * <p>This annotation can be placed on a field or getter method within a {@link Model}-annotated
 * type to override the property name used in the generated metamodel, or to exclude the property
 * from generation entirely.
 *
 * <p>Example — renaming a property:
 * <pre>{@code
 * @Model
 * public class Person {
 *
 *     @Property(name = "first_name")
 *     private String firstName;
 * }
 *
 * // _Person.model().firstName() -> "first_name"
 * }</pre>
 *
 * <p>Example — ignoring a property:
 * <pre>{@code
 * @Model
 * public class Person {
 *
 *     @Property(ignore = true)
 *     private transient String cachedValue;
 * }
 * }</pre>
 *
 * @see Model
 */
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Property {

    /**
     * Overrides the property name used in the generated metamodel.
     *
     * <p>When empty (the default), the property name is derived from the field name or
     * getter method name. Useful when the serialized name differs from the Java field name,
     * for example with a custom MongoDB {@code @Field} mapping.
     *
     * @return the overridden property name, or an empty string to use the default
     */
    String name() default "";

    /**
     * When {@code true}, excludes this field or method from the generated metamodel.
     *
     * <p>Defaults to {@code false}.
     *
     * @return {@code true} if this property should be ignored during generation
     */
    boolean ignore() default false;
}
