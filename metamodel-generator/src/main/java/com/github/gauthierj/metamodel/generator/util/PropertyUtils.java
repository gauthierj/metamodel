package com.github.gauthierj.metamodel.generator.util;

import com.github.gauthierj.metamodel.generator.util.StringUtils;

public final class PropertyUtils {

    private PropertyUtils() {
        throw new IllegalStateException("Cannot instantiate");
    }

    public static String staticPropertyFieldName(String property) {
        return "PROPERTY_" + StringUtils.upperSnakeCase(property);
    }
}
