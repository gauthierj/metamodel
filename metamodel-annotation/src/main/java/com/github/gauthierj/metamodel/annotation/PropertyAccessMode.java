package com.github.gauthierj.metamodel.annotation;

/**
 * Defines how properties are discovered on a {@link Model}-annotated type.
 *
 * @see Model#accessMode()
 */
public enum PropertyAccessMode {

    /**
     * Properties are discovered from the declared fields of the class.
     *
     * <p>This is the default mode. All non-static, non-ignored fields are included
     * unless excluded via {@link Property#ignore()}.
     */
    FIELD,

    /**
     * Properties are discovered from getter methods of the class.
     *
     * <p>Methods are matched against the pattern defined by {@link Model#getterPattern()}.
     * The default pattern matches {@code getXxx()} and {@code isXxx()} methods.
     * The property name is derived from the first capture group of the pattern,
     * with the first character decapitalized.
     */
    GETTER
}
