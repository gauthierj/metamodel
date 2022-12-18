package com.github.gauthierj.metamodel.classbuilder;

import java.util.Collection;
import java.util.stream.Collectors;

public final class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Cannot instantiate");
    }

    public static String toString(Collection<?> collection, String delimiter) {
        return collection.stream()
                .map(Object::toString)
                .collect(Collectors.joining(delimiter));
    }

    public static boolean isNotBlank(String s) {
        return s != null && s.trim().length() > 0;
    }
}
