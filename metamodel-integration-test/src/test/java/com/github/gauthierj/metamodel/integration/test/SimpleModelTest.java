package com.github.gauthierj.metamodel.integration.test;

import com.github.gauthierj.metamodel.integration.test.model._SimpleModelByField;
import com.github.gauthierj.metamodel.integration.test.model._SimpleModelByGetter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleModelTest {

    @Test
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

    @Test
    public void test_SimpleModelByField() {
        Assertions.assertEquals("aBooleanProperty", _SimpleModelByField.model().aBooleanProperty());
        Assertions.assertEquals("anIntProperty", _SimpleModelByField.model().anIntProperty());
        Assertions.assertEquals("aStringProperty", _SimpleModelByField.model().aStringProperty());
        Assertions.assertEquals("aPrimitiveBooleanProperty", _SimpleModelByField.model().aPrimitiveBooleanProperty());
        Assertions.assertEquals("aStringListProperty", _SimpleModelByField.model().aStringListProperty());
        Assertions.assertEquals("aStringArray", _SimpleModelByField.model().aStringArray());
    }

    @Test
    public void test_path_methods() {
        Assertions.assertEquals("", _SimpleModelByGetter.model()._path());
        Assertions.assertEquals("", _SimpleModelByGetter.model()._rootPath().toString());
        Assertions.assertEquals("someProperty", _SimpleModelByGetter.model()._propertyPath("someProperty").toString());
    }
}
