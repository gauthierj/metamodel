package com.github.gauthierj.metamodel.generator.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

    @Test
    void upperSnakeCase_null() {
        assertEquals("", StringUtils.upperSnakeCase(null));
    }

    @Test
    void upperSnakeCase_empty() {
        assertEquals("", StringUtils.upperSnakeCase(""));
    }

    @Test
    void upperSnakeCase_allLowercase() {
        assertEquals("SIMPLE", StringUtils.upperSnakeCase("simple"));
    }

    @Test
    void upperSnakeCase_camelCase() {
        assertEquals("A_PROPERTY_NAME", StringUtils.upperSnakeCase("aPropertyName"));
    }

    @Test
    void upperSnakeCase_singleUpperPrefix() {
        assertEquals("_MY_FIELD", StringUtils.upperSnakeCase("MyField"));
    }

    @Test
    void upperSnakeCase_consecutiveUppercase() {
        assertEquals("_A_B_C", StringUtils.upperSnakeCase("ABC"));
    }

    @Test
    void doubleQuote_normalString() {
        assertEquals("\"hello\"", StringUtils.doubleQuote("hello"));
    }

    @Test
    void doubleQuote_emptyString() {
        assertEquals("\"\"", StringUtils.doubleQuote(""));
    }
}
