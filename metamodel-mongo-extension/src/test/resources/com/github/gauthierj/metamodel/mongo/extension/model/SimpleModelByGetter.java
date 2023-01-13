package com.github.gauthierj.metamodel.mongo.extension.model;

import com.github.gauthierj.metamodel.annotation.Model;
import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Document
@Model(accessMode = PropertyAccessMode.GETTER)
public class SimpleModelByGetter extends SimpleModel {

    public SimpleModelByGetter(String aStringProperty,
                               int anIntProperty,
                               boolean aPrimitiveBooleanProperty,
                               Boolean aBooleanProperty,
                               List<String> aStringListProperty,
                               String [] aStringArray,
                               Map<String, Object> aMapProperty) {
        super(aStringProperty, anIntProperty, aPrimitiveBooleanProperty, aBooleanProperty, aStringListProperty, aStringArray, aMapProperty);
    }

    @Field(name = "someDifferentName")
    public String getAPropertyWithAnotherName() {
        return "don't care";
    }

    @Field(value = "someOtherDifferentName")
    public String getAnotherPropertyWithADifferentName() {
        return "don't care";
    }

    @Field
    public String getAPropertyThatKeepsOriginalName() {
        return "don't care";
    }

    @Id
    public String getAnIdProperty() {
        return "Don't care";
    }
}