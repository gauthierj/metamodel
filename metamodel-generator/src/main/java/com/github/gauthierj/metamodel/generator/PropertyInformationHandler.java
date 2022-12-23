package com.github.gauthierj.metamodel.generator;

import com.github.gauthierj.metamodel.classbuilder.ClassBuilder;
import com.github.gauthierj.metamodel.generator.model.PropertyInformation;
import com.github.gauthierj.metamodel.generator.model.TypeInformation;

public interface PropertyInformationHandler {

    boolean supports(PropertyInformation propertyInformation);

    void handleProperty(ClassBuilder classBuilder, TypeInformation typeInformation, PropertyInformation propertyInformation);

}
