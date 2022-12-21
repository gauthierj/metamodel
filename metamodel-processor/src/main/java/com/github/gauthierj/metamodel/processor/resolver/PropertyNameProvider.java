package com.github.gauthierj.metamodel.processor.resolver;

import com.github.gauthierj.metamodel.processor.ModelProcessor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyNameProvider {

    private final ServiceLoader<PropertyNameResolver> resolvers;

    public PropertyNameProvider() {
        resolvers = ServiceLoader.load(PropertyNameResolver.class, ModelProcessor.class.getClassLoader());
    }

    public String getPropertyName(VariableElement element) {
        for (PropertyNameResolver resolver : resolvers) {
            Optional<String> resolve = resolver.resolve(element);
            if(resolve.isPresent()) {
                return resolve.get();
            }
        }
        return element.getSimpleName().toString();
    }

    public String getPropertyName(ExecutableElement element, String getterPattern) {
        for (PropertyNameResolver resolver : resolvers) {
            System.out.println("Resolver: " + resolver.getClass().getCanonicalName());
            Optional<String> resolve = resolver.resolve(element);
            if(resolve.isPresent()) {
                return resolve.get();
            }
        }
        return StringUtils.getGetterPropertyName(element.getSimpleName().toString(), getterPattern);
    }
}
