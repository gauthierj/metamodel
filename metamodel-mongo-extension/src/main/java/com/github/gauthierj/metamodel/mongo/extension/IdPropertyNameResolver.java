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

public class IdPropertyNameResolver implements PropertyNameResolver {

    public static final String ID_ANNOTATION_QUALIFIED_NAME = "org.springframework.data.annotation.Id";
    public static final String FIELD_ANNOTATION_QUALIFIED_NAME = "org.springframework.data.mongodb.core.mapping.Field";

    @Override
    public Optional<String> resolve(VariableElement variableElement) {
        return doResolve(variableElement);
    }

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
        Element element = annotationMirror.getAnnotationType().asElement().getEnclosedElements()
                .stream()
                .filter(e -> e.getSimpleName().toString().equals(name))
                .findFirst()
                .orElseThrow();

        return Optional.ofNullable(annotationMirror.getElementValues().get(element))
                .map(AnnotationValue::getValue)
                .filter(targetClass::isInstance)
                .map(targetClass::cast);
    }
}
