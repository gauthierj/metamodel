package com.github.gauthierj.metamodel.processor;

import java.util.Set;

/**
 * SPI for declaring which annotations trigger metamodel class generation.
 *
 * <p>Implementations are discovered at compile time via {@link java.util.ServiceLoader}.
 * Each implementation declares one or more fully-qualified annotation class names.
 * When the annotation processor encounters a type annotated with any of these annotations,
 * it generates a corresponding metamodel class for that type.
 *
 * <p>The built-in implementation handles {@code @Model}. The {@code metamodel-mongo-extension}
 * module provides an implementation for Spring Data MongoDB's {@code @Document}.
 *
 * <p>To register a custom implementation, create a file at:
 * <pre>
 * META-INF/services/com.github.gauthierj.metamodel.processor.ModelAnnotationProvider
 * </pre>
 * containing the fully-qualified class name of your implementation.
 *
 * <p>Example:
 * <pre>{@code
 * public class MyAnnotationProvider implements ModelAnnotationProvider {
 *     @Override
 *     public Set<String> getModelAnnotations() {
 *         return Set.of("com.example.MyTriggerAnnotation");
 *     }
 * }
 * }</pre>
 */
public interface ModelAnnotationProvider {

    /**
     * Returns the fully-qualified names of the annotations that should trigger metamodel generation.
     *
     * @return a non-null, non-empty set of fully-qualified annotation class names
     */
    Set<String> getModelAnnotations();
}
