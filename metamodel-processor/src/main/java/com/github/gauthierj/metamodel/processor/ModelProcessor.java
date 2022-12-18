package com.github.gauthierj.metamodel.processor;

import com.github.gauthierj.metamodel.annotation.Model;
import com.github.gauthierj.metamodel.generator.model.TypeInformation;
import com.github.gauthierj.metamodel.processor.resolver.TypeInformationResolver;
import com.github.gauthierj.metamodel.processor.writer.GeneratedClassWriter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes({"com.github.gauthierj.metamodel.annotation.Model"})
public class ModelProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        List<TypeElement> annotatedTypeElements = annotations.stream()
                .filter(annotation -> annotation.getQualifiedName().contentEquals(Model.class.getCanonicalName()))
                .map(roundEnv::getElementsAnnotatedWith)
                .flatMap(Collection::stream)
                .filter(element -> element instanceof TypeElement)
                .map(element -> (TypeElement) element)
                .collect(Collectors.toList());

        List<TypeInformation> typeInformations = new TypeInformationResolver(
                processingEnv.getTypeUtils(),
                processingEnv.getElementUtils())
                .resolveTypeInformations(annotatedTypeElements);

        new GeneratedClassWriter(processingEnv.getFiler(), processingEnv.getMessager())
                .writeGeneratedClasses(typeInformations);

        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}