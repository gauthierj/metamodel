package com.github.gauthierj.metamodel.processor.model;

import java.util.List;

public class ComplexModel {

    private final String aSimpleProperty;

    private final SimpleModel aStructuredProperty;

    private final List<SimpleModel> aListStructuredProperty;

    public ComplexModel(String aSimpleProperty,
                        SimpleModel aStructuredProperty,
                        List<SimpleModel> aListStructuredProperty) {
        this.aSimpleProperty = aSimpleProperty;
        this.aStructuredProperty = aStructuredProperty;
        this.aListStructuredProperty = aListStructuredProperty;
    }

    public String getASimpleProperty() {
        return aSimpleProperty;
    }

    public SimpleModel getAStructuredProperty() {
        return aStructuredProperty;
    }

    public List<SimpleModel> getAListStructuredProperty() {
        return aListStructuredProperty;
    }
}