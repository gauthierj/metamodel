package com.github.gauthierj.metamodel.processor.model;

import com.github.gauthierj.metamodel.annotation.Model;

import java.util.List;

@Model
public class ComplexModelByField extends ComplexModel {

    public ComplexModelByField(String aSimpleProperty,
                               SimpleModel aStructuredProperty,
                               List<SimpleModel> aListStructuredProperty) {
        super(aSimpleProperty, aStructuredProperty, aListStructuredProperty);
    }
}