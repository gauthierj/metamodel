package com.github.gauthierj.metamodel.integration.test;

import com.github.gauthierj.metamodel.integration.test.model._ComplexModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ComplexModelTest {

    @Test
    public void test_structured_property_path() {
        Assertions.assertEquals("simpleModel", _ComplexModel.model().simpleModel()._path());
    }

    @Test
    public void test_structured_property_navigation() {
        Assertions.assertEquals("simpleModel.aStringProperty", _ComplexModel.model().simpleModel().aStringProperty());
        Assertions.assertEquals("simpleModel.aBooleanProperty", _ComplexModel.model().simpleModel().aBooleanProperty());
        Assertions.assertEquals("simpleModel.anIntProperty", _ComplexModel.model().simpleModel().anIntProperty());
        Assertions.assertEquals("simpleModel.aStringListProperty", _ComplexModel.model().simpleModel().aStringListProperty());
        Assertions.assertEquals("simpleModel.aStringArray", _ComplexModel.model().simpleModel().aStringArray());
        Assertions.assertEquals("simpleModel.aPrimitiveBooleanProperty", _ComplexModel.model().simpleModel().aPrimitiveBooleanProperty());
    }

    @Test
    public void test_renamed_property_through_structured_property() {
        Assertions.assertEquals("simpleModel.someDifferentName", _ComplexModel.model().simpleModel().aPropertyWithAnotherName());
    }

    @Test
    public void test_dynamic_property_on_structured_path() {
        Assertions.assertEquals("simpleModel.anyProp", _ComplexModel.model().simpleModel()._property("anyProp"));
        Assertions.assertEquals("simpleModel.anyProp", _ComplexModel.model().simpleModel()._propertyPath("anyProp").toString());
    }
}
