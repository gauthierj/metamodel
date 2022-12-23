package com.github.gauthierj.metamodel.integration.test;

import com.github.gauthierj.metamodel.integration.test.model._SimpleModelByGetter;
import org.junit.jupiter.api.Assertions;

public class SimpleModelTest {

    @org.junit.jupiter.api.Test
    public void test_SimpleModelByGetter() {
        Assertions.assertEquals("SomeProperty", _SimpleModelByGetter.model()._property("SomeProperty"));
        Assertions.assertEquals("aBooleanProperty", _SimpleModelByGetter.model().aBooleanProperty());
        Assertions.assertEquals("anIntProperty", _SimpleModelByGetter.model().anIntProperty());
        Assertions.assertEquals("aStringProperty", _SimpleModelByGetter.model().aStringProperty());
        Assertions.assertEquals("aPrimitiveBooleanProperty", _SimpleModelByGetter.model().aPrimitiveBooleanProperty());
        Assertions.assertEquals("aStringListProperty", _SimpleModelByGetter.model().aStringListProperty());
        Assertions.assertEquals("aStringArray", _SimpleModelByGetter.model().aStringArray());
        Assertions.assertEquals("someDifferentName", _SimpleModelByGetter.model().aPropertyWithAnotherName());
    }
}
