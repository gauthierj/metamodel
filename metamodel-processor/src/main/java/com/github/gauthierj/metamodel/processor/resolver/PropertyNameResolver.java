package com.github.gauthierj.metamodel.processor.resolver;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.Optional;

/**
 * SPI for customizing how property names are resolved during metamodel generation.
 *
 * <p>Implementations are discovered at compile time via {@link java.util.ServiceLoader}.
 * When resolving the name of a property, all registered resolvers are consulted in order.
 * The first non-empty result wins. If no resolver provides a name, the default name is
 * derived from the field name or getter method name.
 *
 * <p>This SPI is useful for frameworks that remap property names via their own annotations.
 * For example, the {@code metamodel-mongo-extension} module provides an implementation that
 * reads Spring Data MongoDB's {@code @Field} and {@code @Id} annotations to resolve the
 * actual MongoDB field name.
 *
 * <p>To register a custom implementation, create a file at:
 * <pre>
 * META-INF/services/com.github.gauthierj.metamodel.processor.resolver.PropertyNameResolver
 * </pre>
 * containing the fully-qualified class name of your implementation.
 *
 * <p>Example:
 * <pre>{@code
 * public class MyPropertyNameResolver implements PropertyNameResolver {
 *
 *     @Override
 *     public Optional<String> resolve(VariableElement field) {
 *         MyAnnotation ann = field.getAnnotation(MyAnnotation.class);
 *         if (ann != null && !ann.value().isEmpty()) {
 *             return Optional.of(ann.value());
 *         }
 *         return Optional.empty();
 *     }
 *
 *     @Override
 *     public Optional<String> resolve(ExecutableElement method) {
 *         return Optional.empty();
 *     }
 * }
 * }</pre>
 */
public interface PropertyNameResolver {

    /**
     * Resolves the property name for the given field element.
     *
     * @param variableElement the field element being processed
     * @return the resolved property name, or {@link Optional#empty()} to defer to the next resolver
     */
    Optional<String> resolve(VariableElement variableElement);

    /**
     * Resolves the property name for the given getter method element.
     *
     * @param variableElement the getter method element being processed
     * @return the resolved property name, or {@link Optional#empty()} to defer to the next resolver
     */
    Optional<String> resolve(ExecutableElement variableElement);
}
