package com.github.gauthierj.metamodel.processor.model;

import java.util.List;

public class SimpleModel {

    private final String aStringProperty;
    private final int anIntProperty;
    private final boolean aPrimitiveBooleanProperty;
    private final Boolean aBooleanProperty;
    private final List<String> aStringListProperty;
    private final String[] aStringArray;

    public SimpleModel(String aStringProperty,
                       int anIntProperty,
                       boolean aPrimitiveBooleanProperty,
                       Boolean aBooleanProperty,
                       List<String> aStringListProperty,
                       String[] aStringArray) {
        this.aStringProperty = aStringProperty;
        this.anIntProperty = anIntProperty;
        this.aPrimitiveBooleanProperty = aPrimitiveBooleanProperty;
        this.aBooleanProperty = aBooleanProperty;
        this.aStringListProperty = aStringListProperty;
        this.aStringArray = aStringArray;
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
}
