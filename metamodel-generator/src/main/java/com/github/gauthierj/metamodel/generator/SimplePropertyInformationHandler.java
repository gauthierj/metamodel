package com.github.gauthierj.metamodel.generator;

import com.github.gauthierj.metamodel.classbuilder.ClassBuilder;
import com.github.gauthierj.metamodel.generator.model.SimplePropertyInformation;
import com.github.gauthierj.metamodel.generator.model.TypeInformation;
import com.github.gauthierj.metamodel.generator.util.PropertyUtils;
import com.github.gauthierj.metamodel.generator.util.StringUtils;

public class SimplePropertyInformationHandler extends _PropertyInformationSupport<SimplePropertyInformation> implements PropertyInformationHandler {

    protected SimplePropertyInformationHandler() {
        super(SimplePropertyInformation.class);
    }

    @Override
    public void _handleProperty(ClassBuilder classBuilder, TypeInformation typeInformation, SimplePropertyInformation propertyInformation) {
        String staticPropertyFieldName = PropertyUtils.staticPropertyFieldName(propertyInformation.logicalName());

        classBuilder.privateStaticFinalField(
                "String",
                staticPropertyFieldName,
                StringUtils.doubleQuote(propertyInformation.name()));

        classBuilder.publicMethod("String", propertyInformation.logicalName())
                .body()
                .writeln(String.format("return _property(%s);", staticPropertyFieldName));
    }
}
