package com.github.gauthierj.metamodel.generator.util;

import java.util.Optional;

public final class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Cannot instantiate");
    }

    public static String upperSnakeCase(String s) {
        return Optional.ofNullable(s)
                .map(str -> str.replaceAll("([A-Z])", "_$1").toUpperCase())
                .orElse("");
    }

    public static String doubleQuote(String s) {
        return String.format("\"%s\"", s);
    }
}
