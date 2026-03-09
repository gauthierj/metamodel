package com.github.gauthierj.metamodel.integration.test.model;

import com.github.gauthierj.metamodel.annotation.Model;

@Model
public class NestedComplexModel {

    private final ComplexModel complexModel;

    public NestedComplexModel(ComplexModel complexModel) {
        this.complexModel = complexModel;
    }

    public ComplexModel getComplexModel() {
        return complexModel;
    }
}
