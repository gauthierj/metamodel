package com.github.gauthierj.metamodel.processor.model;

import com.github.gauthierj.metamodel.annotation.Model;

import java.util.Optional;

@Model
public class ModelWithOptionalProperty {

    private final Optional<String> anOptionalStringProperty;
    private final Optional<SimpleModel> anOptionalStructuredProperty;

    public ModelWithOptionalProperty(Optional<String> anOptionalStringProperty,
                                     Optional<SimpleModel> anOptionalStructuredProperty) {
        this.anOptionalStringProperty = anOptionalStringProperty;
        this.anOptionalStructuredProperty = anOptionalStructuredProperty;
    }
}
