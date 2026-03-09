package com.github.gauthierj.metamodel.processor.model;

import com.github.gauthierj.metamodel.annotation.Model;
import com.github.gauthierj.metamodel.annotation.Property;
import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;

@Model(accessMode = PropertyAccessMode.GETTER)
public class ModelWithIgnoredGetter {

    private final String aVisibleProperty;
    private final String anIgnoredProperty;

    public ModelWithIgnoredGetter(String aVisibleProperty, String anIgnoredProperty) {
        this.aVisibleProperty = aVisibleProperty;
        this.anIgnoredProperty = anIgnoredProperty;
    }

    public String getAVisibleProperty() {
        return aVisibleProperty;
    }

    @Property(ignore = true)
    public String getAnIgnoredProperty() {
        return anIgnoredProperty;
    }
}
