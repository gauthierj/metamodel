package com.github.gauthierj.metamodel.integration.test;

import com.github.gauthierj.metamodel.integration.test.model._ModelWithMapProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MapPropertyTest {

    @Test
    public void test_simple_property() {
        Assertions.assertEquals("aSimpleProperty", _ModelWithMapProperty.model().aSimpleProperty());
    }

    @Test
    public void test_map_property_root_path() {
        Assertions.assertEquals("aMapProperty", _ModelWithMapProperty.model().aMapProperty()._path());
    }

    @Test
    public void test_map_entry_navigation() {
        Assertions.assertEquals("aMapProperty.someKey", _ModelWithMapProperty.model().aMapProperty()._property("someKey"));
    }

    @Test
    public void test_map_entry_path() {
        Assertions.assertEquals("aMapProperty.someKey", _ModelWithMapProperty.model().aMapProperty()._propertyPath("someKey").toString());
    }
}
