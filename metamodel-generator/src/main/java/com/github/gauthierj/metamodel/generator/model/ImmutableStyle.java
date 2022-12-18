package com.github.gauthierj.metamodel.generator.model;

import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.PACKAGE, ElementType.ANNOTATION_TYPE})
@Value.Style(get = "*", jdkOnly = true, optionalAcceptNullable = true, typeImmutable = "*Impl")
public @interface ImmutableStyle {
}
