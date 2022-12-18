package com.github.gauthierj.metamodel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Property {

    String name() default "";

    boolean ignore() default false;
}
