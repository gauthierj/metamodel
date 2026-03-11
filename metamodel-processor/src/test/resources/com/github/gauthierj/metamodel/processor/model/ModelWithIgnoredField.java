package com.github.gauthierj.metamodel.processor.model;

import com.github.gauthierj.metamodel.annotation.Model;
import com.github.gauthierj.metamodel.annotation.Property;

@Model
public class ModelWithIgnoredField {

    private final String aVisibleProperty;

    @Property(ignore = true)
    private final String anIgnoredProperty;

    public ModelWithIgnoredField(String aVisibleProperty, String anIgnoredProperty) {
        this.aVisibleProperty = aVisibleProperty;
        this.anIgnoredProperty = anIgnoredProperty;
    }
}
