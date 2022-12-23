package com.github.gauthierj.metamodel.processor.model;

import com.github.gauthierj.metamodel.annotation.Model;
import com.github.gauthierj.metamodel.annotation.Property;
import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;

import java.util.List;
import java.util.Map;

@Model(accessMode = PropertyAccessMode.GETTER, getterPattern = "(.*)")
public class SimpleModelByGetterPattern extends SimpleModel {

    public SimpleModelByGetterPattern(String aStringProperty,
                               int anIntProperty,
                               boolean aPrimitiveBooleanProperty,
                               Boolean aBooleanProperty,
                               List<String> aStringListProperty,
                               String [] aStringArray,
                               Map<String, Object> aMapProperty) {
        super(aStringProperty, anIntProperty, aPrimitiveBooleanProperty, aBooleanProperty, aStringListProperty, aStringArray, aMapProperty);
    }

    @Property(name = "someDifferentName")
    public String getAPropertyWithAnotherName() {
        return "don't care";
    }
}