package com.github.gauthierj.metamodel.generator;

import com.github.gauthierj.metamodel.generator.model.StructuredPropertyInformation;
import com.github.gauthierj.metamodel.generator.model.TypeInformation;
import com.github.gauthierj.metamodel.generator.util.PropertyUtils;
import com.github.gauthierj.metamodel.generator.util.StringUtils;
import com.github.gauthierj.metamodel.classbuilder.ClassBuilder;

public class StructuredPropertyInformationHandler extends _PropertyInformationSupport<StructuredPropertyInformation> implements PropertyInformationHandler {

    protected StructuredPropertyInformationHandler() {
        super(StructuredPropertyInformation.class);
    }

    public void _handleProperty(ClassBuilder classBuilder,
                                TypeInformation typeInformation,
                                StructuredPropertyInformation propertyInformation) {

        String staticPropertyFieldName = PropertyUtils.staticPropertyFieldName(propertyInformation.logicalName());

        classBuilder.privateStaticFinalField(
                "String",
                staticPropertyFieldName,
                StringUtils.doubleQuote(propertyInformation.name()));

        if (!typeInformation.isInSamePackage(propertyInformation.typeInformation())) {
            classBuilder.addImport(propertyInformation.typeInformation().fullyQualifiedGeneratedClassName());
        }

        String propertyTypeGeneratedClassName = propertyInformation.typeInformation().generatedClassName();

        classBuilder.publicMethod(propertyTypeGeneratedClassName, propertyInformation.logicalName())
                .body()
                .writeln(String.format("return new %s(super._rootPath().with(%s));", propertyTypeGeneratedClassName, staticPropertyFieldName))
                .build();
    }
}
