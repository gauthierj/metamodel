package org.springframework.data.mongodb.core.mapping;

public @interface Field {

    String value() default "";

    String name() default "";
}