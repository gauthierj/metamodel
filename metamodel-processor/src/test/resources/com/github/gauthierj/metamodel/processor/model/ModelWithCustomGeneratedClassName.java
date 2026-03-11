package com.github.gauthierj.metamodel.processor.model;

import com.github.gauthierj.metamodel.annotation.Model;

@Model(generatedClassName = "_CustomModelName")
public class ModelWithCustomGeneratedClassName {

    private final String aProperty;
    private final int anotherProperty;

    public ModelWithCustomGeneratedClassName(String aProperty, int anotherProperty) {
        this.aProperty = aProperty;
        this.anotherProperty = anotherProperty;
    }
}
