package com.github.gauthierj.metamodel.integration.test.model;

import com.github.gauthierj.metamodel.annotation.Model;
import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;

@Model
public class ComplexModel {

    private final SimpleModelByGetter simpleModel;

    public ComplexModel(SimpleModelByGetter simpleModel) {
        this.simpleModel = simpleModel;
    }
}
