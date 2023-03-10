package com.github.gauthierj.metamodel.integration.test.model;

import com.github.gauthierj.metamodel.annotation.Model;
import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;

import java.util.List;

@Model(accessMode = PropertyAccessMode.FIELD)
public class SimpleModelByField extends SimpleModel {

    public SimpleModelByField(String aStringProperty,
                              int anIntProperty,
                              boolean aPrimitiveBooleanProperty,
                              Boolean aBooleanProperty,
                              List<String> aStringListProperty,
                              String [] aStringArray) {
        super(aStringProperty, anIntProperty, aPrimitiveBooleanProperty, aBooleanProperty, aStringListProperty, aStringArray);
    }
}
