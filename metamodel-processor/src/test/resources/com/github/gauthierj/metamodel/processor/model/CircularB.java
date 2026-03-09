package com.github.gauthierj.metamodel.processor.model;

import com.github.gauthierj.metamodel.annotation.Model;

@Model
public class CircularB {

    private final String anotherSimpleProperty;
    private final CircularA a;

    public CircularB(String anotherSimpleProperty, CircularA a) {
        this.anotherSimpleProperty = anotherSimpleProperty;
        this.a = a;
    }
}
