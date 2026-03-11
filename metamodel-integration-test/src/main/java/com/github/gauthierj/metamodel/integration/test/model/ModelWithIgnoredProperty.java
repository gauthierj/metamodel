package com.github.gauthierj.metamodel.integration.test.model;

import com.github.gauthierj.metamodel.annotation.Model;
import com.github.gauthierj.metamodel.annotation.Property;

@Model
public class ModelWithIgnoredProperty {

    private final String aVisibleProperty;

    @Property(ignore = true)
    private final String anIgnoredProperty;

    public ModelWithIgnoredProperty(String aVisibleProperty, String anIgnoredProperty) {
        this.aVisibleProperty = aVisibleProperty;
        this.anIgnoredProperty = anIgnoredProperty;
    }

    public String getAVisibleProperty() {
        return aVisibleProperty;
    }

    public String getAnIgnoredProperty() {
        return anIgnoredProperty;
    }
}
