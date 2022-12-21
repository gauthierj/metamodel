package com.github.gauthierj.metamodel.processor.util;

import com.github.gauthierj.metamodel.annotation.Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Cannot instantiate");
    }

    public static String getGetterPropertyName(String getterName, String getterPattern) {
        Pattern pattern = Pattern.compile(getterPattern);
        Matcher matcher = pattern.matcher(getterName);
        if(matcher.matches()) {
            String withoutGetOrIs = matcher.group(1);
            return Character.toLowerCase(withoutGetOrIs.charAt(0)) +
                    (withoutGetOrIs.length() > 1 ? withoutGetOrIs.substring(1) : "");
        }
        throw new IllegalStateException("Cannot happen, getter matches patter should have been checked prior");
    }
}
