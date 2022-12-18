package com.github.gauthierj.metamodel.processor.model;

import com.github.gauthierj.metamodel.annotation.Model;
import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;

import java.util.List;

@Model(accessMode = PropertyAccessMode.GETTER)
public class ComplexModelByGetter extends ComplexModel {

    public ComplexModelByGetter(String aSimpleProperty,
                               SimpleModel aStructuredProperty,
                               List<SimpleModel> aListStructuredProperty) {
        super(aSimpleProperty, aStructuredProperty, aListStructuredProperty);
    }
}