package com.github.gauthierj.metamodel.processor.model;

import com.github.gauthierj.metamodel.annotation.Model;

@Model
public class CircularA {

    private final String aSimpleProperty;
    private final CircularB b;

    public CircularA(String aSimpleProperty, CircularB b) {
        this.aSimpleProperty = aSimpleProperty;
        this.b = b;
    }
}
