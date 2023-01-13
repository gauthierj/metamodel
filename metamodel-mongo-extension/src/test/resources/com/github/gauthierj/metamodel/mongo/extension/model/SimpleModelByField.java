package com.github.gauthierj.metamodel.mongo.extension.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Document
public class SimpleModelByField extends SimpleModel {

    @Id
    private String anIdProperty;

    @Field(name = "someDifferentName")
    private String aPropertyWithADifferentName;

    @Field(value = "someOtherDifferentName")
    private String anotherPropertyWithADifferentName;

    @Field
    private String aPropertyThatKeepsOriginalName;

    public SimpleModelByField(String aStringProperty,
                              int anIntProperty,
                              boolean aPrimitiveBooleanProperty,
                              Boolean aBooleanProperty,
                              List<String> aStringListProperty,
                              String [] aStringArray,
                              Map<String, Object> aMapProperty) {
        super(aStringProperty, anIntProperty, aPrimitiveBooleanProperty, aBooleanProperty, aStringListProperty, aStringArray, aMapProperty);
    }
}
