package com.github.gauthierj.metamodel.mongo.extension;

import com.github.gauthierj.metamodel.processor.ModelAnnotationProvider;

import java.util.Set;

/**
 * {@link ModelAnnotationProvider} implementation that triggers metamodel generation for
 * Spring Data MongoDB {@code @Document}-annotated classes.
 *
 * <p>This extension is automatically discovered via {@link java.util.ServiceLoader} when
 * the {@code metamodel-mongo-extension} module is on the annotation processor classpath.
 * No additional configuration is required: any class annotated with {@code @Document} will
 * automatically have a metamodel class generated for it.
 *
 * <p>This is typically used together with {@link IdPropertyNameResolver}, which ensures that
 * {@code @Id}-annotated fields are mapped to {@code "_id"} and {@code @Field}-annotated fields
 * use their declared MongoDB field name.
 *
 * @see IdPropertyNameResolver
 * @see ModelAnnotationProvider
 */
public class DocumentModelAnnotationProvider implements ModelAnnotationProvider {

    /**
     * Fully-qualified name of the Spring Data MongoDB {@code @Document} annotation.
     */
    public static final String DOCUMENT_ANNOTATION_QUALIFIED_NAME = "org.springframework.data.mongodb.core.mapping.Document";

    /**
     * Returns the {@code @Document} annotation as the sole trigger for metamodel generation.
     *
     * @return a singleton set containing the fully-qualified name of {@code @Document}
     */
    @Override
    public Set<String> getModelAnnotations() {
        return Set.of(DOCUMENT_ANNOTATION_QUALIFIED_NAME);
    }
}
