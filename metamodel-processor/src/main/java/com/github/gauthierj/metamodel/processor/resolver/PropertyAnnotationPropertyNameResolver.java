package com.github.gauthierj.metamodel.processor.resolver;

import com.github.gauthierj.metamodel.annotation.Property;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.Optional;

public class PropertyAnnotationPropertyNameResolver implements PropertyNameResolver {
    @Override
    public Optional<String> resolve(VariableElement element) {
        return Optional.ofNullable(element)
                .map(elm -> elm.getAnnotation(Property.class))
                .map(Property::name)
                .filter(name -> name != null && name.trim().length() > 0);
    }

    @Override
    public Optional<String> resolve(ExecutableElement element) {
        return Optional.ofNullable(element)
                .map(elm -> elm.getAnnotation(Property.class))
                .map(Property::name)
                .filter(name -> name != null && name.trim().length() > 0);
    }
}
