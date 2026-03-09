package com.github.gauthierj.metamodel.generator.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PropertyUtilsTest {

    @Test
    void staticPropertyFieldName_camelCase() {
        assertEquals("PROPERTY_A_PROPERTY_NAME", PropertyUtils.staticPropertyFieldName("aPropertyName"));
    }

    @Test
    void staticPropertyFieldName_singleWord() {
        assertEquals("PROPERTY_NAME", PropertyUtils.staticPropertyFieldName("name"));
    }

    @Test
    void staticPropertyFieldName_alreadyUpper() {
        assertEquals("PROPERTY__I_D", PropertyUtils.staticPropertyFieldName("ID"));
    }
}
