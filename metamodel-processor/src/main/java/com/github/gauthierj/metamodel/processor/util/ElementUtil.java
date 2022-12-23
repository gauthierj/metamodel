package com.github.gauthierj.metamodel.processor.util;

import com.github.gauthierj.metamodel.annotation.Model;
import com.github.gauthierj.metamodel.annotation.Property;
import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;
import com.github.gauthierj.metamodel.classbuilder.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import java.util.Optional;

public final class ElementUtil {

    private ElementUtil() {
        throw new IllegalStateException("Cannot instantiate");
    }

    public static PropertyAccessMode getPropertyAccesMode(Element e) {
        return getPropertyAccesMode(e, Model.DEFAULT_ACCESS_MODE);
    }

    public static PropertyAccessMode getPropertyAccesMode(Element e, PropertyAccessMode defaultValue) {
        return Optional.of(e)
                .map(elm -> elm.getAnnotation(Model.class))
                .map(model -> model.accessMode())
                .orElse(defaultValue);
    }

    public static String getGetterPattern(Element e) {
        return getGetterPattern(e, Model.DEFAULT_GETTER_PATTERN);
    }

    public static String getGetterPattern(Element e, String defaultValue) {
        return Optional.of(e)
                .map(elm -> elm.getAnnotation(Model.class))
                .map(model -> model.getterPattern())
                .orElse(defaultValue);
    }

    public static boolean isIgnored(Element e) {
        return Optional.of(e)
                .map(elm -> elm.getAnnotation(Property.class))
                .map(property -> property.ignore())
                .orElse(false);
    }

    public static String getGeneratedClassName(TypeElement te) {
        return Optional.of(te)
                .map(elm -> elm.getAnnotation(Model.class))
                .map(model -> model.generatedClassName())
                .filter(generatedClassName -> StringUtils.isNotBlank(generatedClassName))
                .orElseGet(() -> "_" + te.getSimpleName());
    }

    public static String getGeneratedClassName(Element e, TypeElement te) {
        return Optional.of(e)
                .map(elm -> elm.getAnnotation(Model.class))
                .map(model -> model.generatedClassName())
                .filter(generatedClassName -> StringUtils.isNotBlank(generatedClassName))
                .orElseGet(() -> "_" + te.getSimpleName());
    }

    public static boolean isGetter(ExecutableElement e, String getterPattern) {
        return e.getParameters().isEmpty()
                && e.getModifiers().contains(Modifier.PUBLIC)
                && !e.getReturnType().getKind().equals(TypeKind.VOID)
                && e.getSimpleName().toString().matches(getterPattern);
    }
}

