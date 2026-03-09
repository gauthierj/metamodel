package com.github.gauthierj.metamodel.integration.test;

import com.github.gauthierj.metamodel.integration.test.model._NestedComplexModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NestedPathTest {

    @Test
    public void test_first_level_path() {
        Assertions.assertEquals("complexModel", _NestedComplexModel.model().complexModel()._path());
    }

    @Test
    public void test_second_level_path() {
        Assertions.assertEquals("complexModel.simpleModel", _NestedComplexModel.model().complexModel().simpleModel()._path());
    }

    @Test
    public void test_three_level_property_path() {
        Assertions.assertEquals("complexModel.simpleModel.aStringProperty",
                _NestedComplexModel.model().complexModel().simpleModel().aStringProperty());
    }

    @Test
    public void test_three_level_renamed_property_path() {
        Assertions.assertEquals("complexModel.simpleModel.someDifferentName",
                _NestedComplexModel.model().complexModel().simpleModel().aPropertyWithAnotherName());
    }

    @Test
    public void test_dynamic_property_at_third_level() {
        Assertions.assertEquals("complexModel.simpleModel.anything",
                _NestedComplexModel.model().complexModel().simpleModel()._property("anything"));
    }
}
