package com.github.gauthierj.metamodel.classbuilder;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {

    @Test
    void isNotBlank_null() {
        assertFalse(StringUtils.isNotBlank(null));
    }

    @Test
    void isNotBlank_empty() {
        assertFalse(StringUtils.isNotBlank(""));
    }

    @Test
    void isNotBlank_whitespaceOnly() {
        assertFalse(StringUtils.isNotBlank("   "));
    }

    @Test
    void isNotBlank_nonBlank() {
        assertTrue(StringUtils.isNotBlank("hello"));
    }

    @Test
    void isNotBlank_whitespaceAndContent() {
        assertTrue(StringUtils.isNotBlank("  hello  "));
    }

    @Test
    void toString_emptyCollection() {
        assertEquals("", StringUtils.toString(List.of(), ", "));
    }

    @Test
    void toString_singleElement() {
        assertEquals("foo", StringUtils.toString(List.of("foo"), ", "));
    }

    @Test
    void toString_multipleElements() {
        assertEquals("foo, bar, baz", StringUtils.toString(List.of("foo", "bar", "baz"), ", "));
    }

    @Test
    void toString_customDelimiter() {
        assertEquals("a|b|c", StringUtils.toString(List.of("a", "b", "c"), "|"));
    }
}
