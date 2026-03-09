package com.github.gauthierj.metamodel.integration.test;

import com.github.gauthierj.metamodel.integration.test.model._ModelWithIgnoredProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IgnoredPropertyTest {

    @Test
    public void test_visible_property_is_accessible() {
        Assertions.assertEquals("aVisibleProperty", _ModelWithIgnoredProperty.model().aVisibleProperty());
    }

    @Test
    public void test_ignored_property_has_no_metamodel_method() {
        Assertions.assertThrows(NoSuchMethodException.class, () ->
                _ModelWithIgnoredProperty.class.getMethod("anIgnoredProperty"));
    }
}
