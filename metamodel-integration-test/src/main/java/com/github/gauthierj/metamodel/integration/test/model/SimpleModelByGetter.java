package com.github.gauthierj.metamodel.integration.test.model;

import com.github.gauthierj.metamodel.annotation.Model;
import com.github.gauthierj.metamodel.annotation.Property;
import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;

import java.util.List;

@Model(accessMode = PropertyAccessMode.GETTER)
public class SimpleModelByGetter extends SimpleModel {

    public SimpleModelByGetter(String aStringProperty,
                               int anIntProperty,
                               boolean aPrimitiveBooleanProperty,
                               Boolean aBooleanProperty,
                               List<String> aStringListProperty,
                               String [] aStringArray) {
        super(aStringProperty, anIntProperty, aPrimitiveBooleanProperty, aBooleanProperty, aStringListProperty, aStringArray);
    }

    @Property(name = "someDifferentName")
    public String getAPropertyWithAnotherName() {
        return "don't care";
    }
}