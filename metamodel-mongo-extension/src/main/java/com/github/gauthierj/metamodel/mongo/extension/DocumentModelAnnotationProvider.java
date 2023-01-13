package com.github.gauthierj.metamodel.mongo.extension;

import com.github.gauthierj.metamodel.processor.ModelAnnotationProvider;

import java.util.Set;

public class DocumentModelAnnotationProvider implements ModelAnnotationProvider {

    public static final String DOCUMENT_ANNOTATION_QUALIFIED_NAME = "org.springframework.data.mongodb.core.mapping.Document";

    @Override
    public Set<String> getModelAnnotations() {
        return Set.of(DOCUMENT_ANNOTATION_QUALIFIED_NAME);
    }
}
