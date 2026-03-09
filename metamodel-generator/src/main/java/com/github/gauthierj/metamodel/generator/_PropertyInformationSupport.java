package com.github.gauthierj.metamodel.generator;

import com.github.gauthierj.metamodel.classbuilder.ClassBuilder;
import com.github.gauthierj.metamodel.generator.model.PropertyInformation;
import com.github.gauthierj.metamodel.generator.model.TypeInformation;

public abstract class _PropertyInformationSupport<T extends PropertyInformation> implements PropertyInformationHandler{

    private final Class<T> supportedClass;

    protected _PropertyInformationSupport(Class<T> supportedClass) {
        this.supportedClass = supportedClass;
    }

    public boolean supports(PropertyInformation propertyInformation) {
        return supportedClass.isAssignableFrom(propertyInformation.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleProperty(ClassBuilder classBuilder,
                               TypeInformation typeInformation,
                               PropertyInformation propertyInformation) {
        if (this.supports(propertyInformation)) {
            _handleProperty(classBuilder, typeInformation, (T) propertyInformation);
        }
    }

    public abstract void _handleProperty(ClassBuilder classBuilder, TypeInformation typeInformation, T propertyInformation);
}
