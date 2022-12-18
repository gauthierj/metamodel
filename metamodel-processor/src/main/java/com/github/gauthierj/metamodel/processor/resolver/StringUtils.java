package com.github.gauthierj.metamodel.processor.resolver;

public final class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Cannot instantiate");
    }

    public static String getGetterPropertyName(String getterName) {
        String withoutGetOrIs = getterName.replaceFirst("^(?:is|get)", "");
        return Character.toLowerCase(withoutGetOrIs.charAt(0)) +
                (withoutGetOrIs.length() > 1 ? withoutGetOrIs.substring(1) : "");
    }
}
