package com.github.gauthierj.metamodel.integration.test.model;

import com.github.gauthierj.metamodel.annotation.Model;

import java.util.Map;

@Model
public class ModelWithMapProperty {

    private final String aSimpleProperty;
    private final Map<String, Object> aMapProperty;

    public ModelWithMapProperty(String aSimpleProperty, Map<String, Object> aMapProperty) {
        this.aSimpleProperty = aSimpleProperty;
        this.aMapProperty = aMapProperty;
    }

    public String getASimpleProperty() {
        return aSimpleProperty;
    }

    public Map<String, Object> getAMapProperty() {
        return aMapProperty;
    }
}
