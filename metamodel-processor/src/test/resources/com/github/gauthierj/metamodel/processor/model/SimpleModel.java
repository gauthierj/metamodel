package com.github.gauthierj.metamodel.processor.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimpleModel {

    private final String aStringProperty;
    private final int anIntProperty;
    private final boolean aPrimitiveBooleanProperty;
    private final Boolean aBooleanProperty;
    private final List<String> aStringListProperty;
    private final String[] aStringArray;

    private final Map<String, Object> aMapProperty = new HashMap<>();

    public SimpleModel(String aStringProperty,
                       int anIntProperty,
                       boolean aPrimitiveBooleanProperty,
                       Boolean aBooleanProperty,
                       List<String> aStringListProperty,
                       String[] aStringArray,
                       Map<String, Object> aMapProperty) {
        this.aStringProperty = aStringProperty;
        this.anIntProperty = anIntProperty;
        this.aPrimitiveBooleanProperty = aPrimitiveBooleanProperty;
        this.aBooleanProperty = aBooleanProperty;
        this.aStringListProperty = aStringListProperty;
        this.aStringArray = aStringArray;
        Optional.ofNullable(aMapProperty).ifPresent(this.aMapProperty::putAll);
    }

    public String getAStringProperty() {
        return aStringProperty;
    }

    public int getAnIntProperty() {
        return anIntProperty;
    }

    public boolean isAPrimitiveBooleanProperty() {
        return aPrimitiveBooleanProperty;
    }

    public Boolean getABooleanProperty() {
        return aBooleanProperty;
    }

    public List<String> getAStringListProperty() {
        return aStringListProperty;
    }

    public String[] getAStringArray() {
        return aStringArray;
    }

    public Map<String, Object> getAMapProperty() {
        return Map.copyOf(aMapProperty);
    }
}
