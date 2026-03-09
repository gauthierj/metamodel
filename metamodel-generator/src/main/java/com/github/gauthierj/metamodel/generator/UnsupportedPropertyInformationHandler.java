package com.github.gauthierj.metamodel.generator;

import com.github.gauthierj.metamodel.classbuilder.ClassBuilder;
import com.github.gauthierj.metamodel.generator.model.TypeInformation;
import com.github.gauthierj.metamodel.generator.model.UnsupportedPropertyInformation;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class UnsupportedPropertyInformationHandler extends _PropertyInformationSupport<UnsupportedPropertyInformation> implements PropertyInformationHandler {

    private final Messager messager;

    protected UnsupportedPropertyInformationHandler(Messager messager) {
        super(UnsupportedPropertyInformation.class);
        this.messager = messager;
    }

    @Override
    public void _handleProperty(ClassBuilder classBuilder, TypeInformation typeInformation, UnsupportedPropertyInformation propertyInformation) {
        messager.printMessage(Diagnostic.Kind.WARNING, String.format("Could handle property %s from type %s", propertyInformation.name(), propertyInformation.type()));
    }
}
