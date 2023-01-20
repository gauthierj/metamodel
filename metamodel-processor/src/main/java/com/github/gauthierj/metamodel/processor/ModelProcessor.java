package com.github.gauthierj.metamodel.processor;

import com.github.gauthierj.metamodel.generator.model.TypeInformation;
import com.github.gauthierj.metamodel.processor.resolver.TypeInformationResolver;
import com.github.gauthierj.metamodel.processor.writer.GeneratedClassWriter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes({"com.github.gauthierj.metamodel.annotation.Model"})
public class ModelProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        List<TypeElement> annotatedTypeElements = annotations.stream()
                .filter(annotation -> getSupportedAnnotationTypes().contains(annotation.getQualifiedName().toString()))
                .map(roundEnv::getElementsAnnotatedWith)
                .flatMap(Collection::stream)
                .filter(TypeElement.class::isInstance)
                .map(TypeElement.class::cast)
                .collect(Collectors.toList());

        List<TypeInformation> typeInformations = new TypeInformationResolver(
                processingEnv.getTypeUtils(),
                processingEnv.getElementUtils())
                .resolveTypeInformations(annotatedTypeElements);

        new GeneratedClassWriter(processingEnv.getFiler(), processingEnv.getMessager())
                .writeGeneratedClasses(typeInformations);

        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> result = new HashSet<>();
        ServiceLoader<ModelAnnotationProvider> modelAnnotationProviders = ServiceLoader.load(ModelAnnotationProvider.class, ModelProcessor.class.getClassLoader());
        for (ModelAnnotationProvider modelAnnotationProvider : modelAnnotationProviders) {
            result.addAll(modelAnnotationProvider.getModelAnnotations());
        }
        return result;
    }
}
