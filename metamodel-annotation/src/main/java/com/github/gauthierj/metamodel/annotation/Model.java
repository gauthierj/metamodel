package com.github.gauthierj.metamodel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface Model {

    String DEFAULT_GETTER_PATTERN = "^(?:is|get)([A-Z][a-zA-Z0-9_$]*)$";
    PropertyAccessMode DEFAULT_ACCESS_MODE = PropertyAccessMode.FIELD;

    PropertyAccessMode accessMode() default PropertyAccessMode.FIELD;

    String getterPattern() default DEFAULT_GETTER_PATTERN;

}
