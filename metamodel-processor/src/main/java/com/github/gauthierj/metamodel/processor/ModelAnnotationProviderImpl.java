package com.github.gauthierj.metamodel.processor;

import com.github.gauthierj.metamodel.annotation.Model;

import java.util.Set;

public class ModelAnnotationProviderImpl implements ModelAnnotationProvider {

    @Override
    public Set<String> getModelAnnotations() {
        return Set.of(Model.class.getCanonicalName());
    }
}
