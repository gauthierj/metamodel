package com.github.gauthierj.test;

import com.github.gauthierj.metamodel.model._Model;
import com.github.gauthierj.metamodel.model._Path;
import com.github.gauthierj.test.subpackage._AnotherTestClass;

public class _TestClass extends _Model {

    private static final _TestClass MODEL = new _TestClass(_Path.of());
    private static final String PROPERTY_A_SIMPLE_PROPERTY = "aSimpleProperty";
    private static final String PROPERTY_ANOTHER_SIMPLE_PROPERTY = "anotherSimpleProperty";
    private static final String PROPERTY_A_STRUCTURED_PROPERTY = "aStructuredProperty";

    public _TestClass(_Path rootPath) {
        super(rootPath);
    }

    public static _TestClass model() {
        return MODEL;
    }

    public String aSimpleProperty() {
        return _property(PROPERTY_A_SIMPLE_PROPERTY);
    }

    public String anotherSimpleProperty() {
        return _property(PROPERTY_ANOTHER_SIMPLE_PROPERTY);
    }

    public _AnotherTestClass aStructuredProperty() {
        return new _AnotherTestClass(super._rootPath().with(PROPERTY_A_STRUCTURED_PROPERTY));
    }
}
