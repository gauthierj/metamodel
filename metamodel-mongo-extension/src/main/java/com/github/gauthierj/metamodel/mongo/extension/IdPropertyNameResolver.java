package com.github.gauthierj.metamodel.mongo.extension;

import com.github.gauthierj.metamodel.classbuilder.StringUtils;
import com.github.gauthierj.metamodel.processor.resolver.PropertyNameResolver;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import java.util.Optional;

/**
 * {@link PropertyNameResolver} implementation that resolves MongoDB property names from
 * Spring Data annotations.
 *
 * <p>The following resolution rules are applied, in order of priority:
 * <ol>
 *   <li>If the field or method is annotated with {@code @Id}, the property name is resolved
 *       to {@code "_id"}, which is the MongoDB document identifier field name.</li>
 *   <li>If the field or method is annotated with {@code @Field}, the property name is resolved
 *       from the annotation's {@code value()} attribute, falling back to its {@code name()}
 *       attribute if {@code value()} is blank.</li>
 *   <li>Otherwise, {@link Optional#empty()} is returned and the default name derivation applies.</li>
 * </ol>
 *
 * <p>This resolver is automatically discovered via {@link java.util.ServiceLoader} when the
 * {@code metamodel-mongo-extension} module is on the annotation processor classpath.
 *
 * @see DocumentModelAnnotationProvider
 * @see PropertyNameResolver
 */
public class IdPropertyNameResolver implements PropertyNameResolver {

    /**
     * Fully-qualified name of the Spring Data {@code @Id} annotation.
     */
    public static final String ID_ANNOTATION_QUALIFIED_NAME = "org.springframework.data.annotation.Id";

    /**
     * Fully-qualified name of the Spring Data MongoDB {@code @Field} annotation.
     */
    public static final String FIELD_ANNOTATION_QUALIFIED_NAME = "org.springframework.data.mongodb.core.mapping.Field";

    /**
     * Resolves the MongoDB field name for the given field element.
     *
     * @param variableElement the field element being processed
     * @return {@code "_id"} if annotated with {@code @Id}, the {@code @Field} value if present,
     *         or {@link Optional#empty()} to fall back to the default name
     */
    @Override
    public Optional<String> resolve(VariableElement variableElement) {
        return doResolve(variableElement);
    }

    /**
     * Resolves the MongoDB field name for the given getter method element.
     *
     * @param executableElement the getter method element being processed
     * @return {@code "_id"} if annotated with {@code @Id}, the {@code @Field} value if present,
     *         or {@link Optional#empty()} to fall back to the default name
     */
    @Override
    public Optional<String> resolve(ExecutableElement executableElement) {
        return doResolve(executableElement);
    }

    private Optional<String> doResolve(Element element) {
        return resolveIdAnnotation(element)
                .or(() -> resolveFieldAnnotation(element));
    }

    private Optional<String> resolveIdAnnotation(Element element) {
        return Optional.of(element)
                .filter(e -> isAnnotatedWith(e, ID_ANNOTATION_QUALIFIED_NAME))
                .map(e -> "_id");
    }

    private Optional<String> resolveFieldAnnotation(Element element) {
        return element.getAnnotationMirrors().stream()
                .filter(annotationMirror -> isAnnotation(annotationMirror, FIELD_ANNOTATION_QUALIFIED_NAME))
                .findFirst()
                .flatMap(annotationMirror -> {
                    Optional<String> value = getStringValue(annotationMirror, "value");
                    Optional<String> name = getStringValue(annotationMirror, "name");
                    return value.or(() -> name);
                });
    }

    private boolean isAnnotatedWith(Element element, String annotationFullyQualifiedName) {
        return element.getAnnotationMirrors().stream()
                .anyMatch(annotationMirror -> isAnnotation(annotationMirror, annotationFullyQualifiedName));
    }

    private boolean isAnnotation(AnnotationMirror annotationMirror, String annotationFullyQualifiedName) {
        return Optional.of(annotationMirror)
                .map(AnnotationMirror::getAnnotationType)
                .map(DeclaredType::asElement)
                .filter(TypeElement.class::isInstance)
                .map(TypeElement.class::cast)
                .map(typeElement -> annotationFullyQualifiedName.equals(typeElement.getQualifiedName().toString()))
                .orElse(false);
    }

    private Optional<String> getStringValue(AnnotationMirror annotationMirror, String name) {
        return  getValue(annotationMirror, name, String.class)
                .filter(StringUtils::isNotBlank);
    }

    private <T> Optional<T> getValue(AnnotationMirror annotationMirror, String name, Class<T> targetClass) {
        return annotationMirror.getAnnotationType().asElement().getEnclosedElements()
                .stream()
                .filter(e -> e.getSimpleName().toString().equals(name))
                .findFirst()
                .map(element -> annotationMirror.getElementValues().get(element))
                .map(AnnotationValue::getValue)
                .filter(targetClass::isInstance)
                .map(targetClass::cast);
    }
}
