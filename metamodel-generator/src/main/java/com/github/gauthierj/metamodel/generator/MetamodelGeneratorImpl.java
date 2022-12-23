package com.github.gauthierj.metamodel.generator;

import com.github.gauthierj.metamodel.classbuilder.ClassBuilder;
import com.github.gauthierj.metamodel.generator.model.TypeInformation;
import com.github.gauthierj.metamodel.model._Model;
import com.github.gauthierj.metamodel.model._Path;

import javax.annotation.processing.Messager;
import java.util.List;

import static com.github.gauthierj.metamodel.classbuilder.Modifier.PUBLIC;

public class MetamodelGeneratorImpl implements MetamodelGenerator {

    private final List<PropertyInformationHandler> propertyInformationHandlers;

    public MetamodelGeneratorImpl(Messager messager) {
        this.propertyInformationHandlers = List.of(
                new SimplePropertyInformationHandler(),
                new StructuredPropertyInformationHandler(),
                new UnsupportedPropertyInformationHandler(messager));
    }

    @Override
    public String generateMetaModel(TypeInformation typeInformation) {
        ClassBuilder classBuilder = buildGeneratedClassStructure(typeInformation);

        typeInformation
                .properties()
                .forEach(propertyInformation -> propertyInformationHandlers.stream()
                        .filter(propertyInformationHandler -> propertyInformationHandler.supports(propertyInformation))
                        .forEach(propertyInformationHandler -> propertyInformationHandler.handleProperty(classBuilder, typeInformation, propertyInformation)));

        return classBuilder.build();
    }

    private static ClassBuilder buildGeneratedClassStructure(TypeInformation typeInformation) {
        String generatedClassName = typeInformation.generatedClassName();

        ClassBuilder classBuilder = ClassBuilder.of(
                List.of(PUBLIC),
                typeInformation.packageName(),
                generatedClassName,
                _Model.class.getSimpleName());

        classBuilder.addImport(_Model.class);
        classBuilder.addImport(_Path.class);

        classBuilder.privateStaticFinalField(
                generatedClassName,
                "MODEL",
                String.format("new %s(%s.of())", generatedClassName, _Path.class.getSimpleName()));

        classBuilder.publicConstructor()
                .parameter(_Path.class, "rootPath")
                .body()
                .writeln("super(rootPath);")
                .build();

        classBuilder.publicStaticMethod(generatedClassName, "model")
                .body()
                .writeln("return MODEL;");
        return classBuilder;
    }
}
